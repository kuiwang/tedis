/**
 * (C) 2011-2012 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 */
package com.taobao.common.tedis.commands.benchmark;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.taobao.common.tedis.commands.TedisManagerFactory;
import com.taobao.common.tedis.core.TedisManager;

public class Main {
    public static void main(String[] args) {
//        System.out.println("============start tair benchmark=================");
//        Result result = JUnitCore.runClasses(TairBenchMark.class);
//        System.out.println("runtime:" + result.getRunTime());
//        System.out.println("runcount:" + result.getRunCount());
//        System.out.println("failurecout:" + result.getFailureCount());
//        System.out.println("============ended tair benchmark=================");

//        System.out.println("============start tedis benchmark================");
//        Result result = JUnitCore.runClasses(BenchmarkTest.class);
//        System.out.println("runtime:" + result.getRunTime());
//        System.out.println("runcount:" + result.getRunCount());
//        System.out.println("failurecout:" + result.getFailureCount());
//        System.out.println("============ended tedis benchmark================");


        TedisManager tedisManager = TedisManagerFactory.create("juxin", "v0");
        while(true) {
            tedisManager.getAtomicCommands().increment(1, "test", 1);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
