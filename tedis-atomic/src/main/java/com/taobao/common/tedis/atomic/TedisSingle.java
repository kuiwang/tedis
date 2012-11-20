/**
 * (C) 2011-2012 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 */
package com.taobao.common.tedis.atomic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.common.tedis.Single;
import com.taobao.common.tedis.binary.RedisCommands;
import com.taobao.common.tedis.config.HAConfig.ServerProperties;

/**
 * @author jianxing <jianxing.qx@taobao.com>
 */
@SuppressWarnings("unchecked")
public class TedisSingle implements Single {

    static final Log logger = LogFactory.getLog(TedisSingle.class);
    // private TedisPool pool;
    private AtomicInteger errorCount = new AtomicInteger(0);
    private ServerProperties prop;
    RedisCommands tedis;
    int pool_size;
    // int max_batch_size = 100;
    int requestQueueLimit = 10000;
    List<BatchThread> threadPool = new ArrayList<BatchThread>();
    Class pipeline;
    final ArrayBlockingQueue<Request> requestQueue = new ArrayBlockingQueue(requestQueueLimit);

    @Override
    public ServerProperties getProperties() {
        return this.prop;
    }

    @Override
    public AtomicInteger getErrorCount() {
        return this.errorCount;
    }

    public TedisSingle(ServerProperties prop) {
        this.prop = prop;

        this.pool_size = prop.pool_size;
        this.tedis = (RedisCommands) Proxy.newProxyInstance(RedisCommands.class.getClassLoader(), new Class[] { RedisCommands.class }, new TedisInvocationHandler());

        for (int i = 0; i < pool_size; i++) {
            Tedis tedis = new Tedis(prop.server.addr, prop.server.port, prop.timeout);
            if (null != prop.password && !"".equals(prop.password)) {
                tedis.auth(prop.password);
            } else {
                tedis.ping();
            }

            BatchThread thread = new BatchThread(i, tedis);
            threadPool.add(thread);
            thread.start();
        }
    }

    @Override
    public RedisCommands getTedis() {
        return tedis;
    }

    public class Request {

        Method method;
        Object[] args;
        BatchFuture result;
    }

    private class TedisInvocationHandler implements InvocationHandler {

        public Object batch(Object proxy, Method method, Object[] args) throws Throwable {
            Request req = new Request();
            req.method = method;
            req.args = args;
            req.result = new BatchFuture();

            try {
                requestQueue.add(req);
            } catch (Throwable t) {
                req.result.setException(t);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(prop + ",method:" + method.getName());
            }
            Object result = req.result.get(prop.timeout, TimeUnit.MILLISECONDS);
            if (logger.isDebugEnabled()) {
                logger.debug("result:" + (result == null ? "ok" : result.toString()));
            }
            return result;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            return batch(proxy, method, args);
        }
    }

    private class BatchThread extends Thread {

        Tedis tedis;
        int i;
        volatile boolean stop;

        public BatchThread(int i, Tedis tedis) {
            this.i = i;
            this.tedis = tedis;
        }

        @Override
        public void run() {
            Thread.currentThread().setName("BatchThread-" + i);
            while (!stop) {
                Request r = null;
                try {
                    while (!stop && r == null) {
                        r = requestQueue.poll(200, TimeUnit.SECONDS);
                        if (r == null) {
                            tedis.ping();
                        }
                    }
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage());
                    break;
                } catch (Exception e) {
                    logger.error("Request poll error:", e);
                }
                if (r != null) {
                    try {
                        r.result.setResult(r.method.invoke(tedis, r.args));
                    } catch (Throwable t) {
                        r.result.setException(t);
                    }
                }

            }
            try {
                tedis.disconnect();
            } catch (Exception e1) {
                logger.warn("断开连接失败", e1);
            }
        }

        public void stop1() {
            stop = true;
        }
    }

    public void destroy() {
        for (BatchThread thread : threadPool) {
            thread.stop1();
            thread.interrupt();
        }
        // threadPool.clear();

        Request r = null;
        while ((r = requestQueue.poll()) != null) {
            r.result.setException(new Exception("Single实例不可用,销毁请求队列"));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TedisSingle other = (TedisSingle) obj;
        if (this.prop != other.prop && (this.prop == null || !this.prop.equals(other.prop))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public String toString() {
        return "Single{" + "errorCount=" + errorCount + ", prop=" + prop + '}';
    }

    private class BatchFuture<T> implements Future<T> {

        CountDownLatch cdl = new CountDownLatch(1);
        volatile T result;
        volatile Throwable t;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isCancelled() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isDone() {
            return cdl.getCount() <= 0;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            boolean to = cdl.await(timeout, unit);
            if (!to) {
                throw new TimeoutException("future cdl timeout");
            }
            if (t != null) {
                throw new ExecutionException(t);
            }
            return result;
        }

        public void setResult(T result) {
            this.result = result;
            cdl.countDown();
        }

        public void setException(Throwable t) {
            this.t = t;
            cdl.countDown();
        }
    }
}
