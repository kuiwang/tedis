/**
 * (C) 2011-2012 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 */
package com.taobao.common.tedis.binary;

import java.util.List;

import com.taobao.common.tedis.config.Process;
import com.taobao.common.tedis.config.Process.Policy;
import com.taobao.common.tedis.util.SafeEncoder;

/**
 * @author juxin.zj E-mail:juxin.zj@taobao.com
 * @since 2011-7-25 09:09:39
 * @version 1.0
 */
public interface RedisListCommands {

    public enum Position {
        BEFORE, AFTER;
        public final byte[] raw;

        private Position() {
            raw = SafeEncoder.encode(name());
        }
    }

    @Process(Policy.WRITE)
    Long rPush(byte[] key, byte[]... value);

    @Process(Policy.WRITE)
    Long lPush(byte[] key, byte[]... value);

    @Process(Policy.WRITE)
    Long rPushX(byte[] key, byte[] value);

    @Process(Policy.WRITE)
    Long lPushX(byte[] key, byte[] value);

    @Process(Policy.READ)
    Long lLen(byte[] key);

    @Process(Policy.READ)
    List<byte[]> lRange(byte[] key, long begin, long end);

    @Process(Policy.WRITE)
    Boolean lTrim(byte[] key, long begin, long end);

    @Process(Policy.READ)
    byte[] lIndex(byte[] key, long index);

    @Process(Policy.WRITE)
    Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value);

    @Process(Policy.WRITE)
    Boolean lSet(byte[] key, long index, byte[] value);

    @Process(Policy.WRITE)
    Long lRem(byte[] key, long count, byte[] value);

    @Process(Policy.WRITE)
    byte[] lPop(byte[] key);

    @Process(Policy.WRITE)
    byte[] rPop(byte[] key);

    @Process(Policy.WRITE)
    List<byte[]> bLPop(int timeout, byte[]... keys);

    @Process(Policy.WRITE)
    List<byte[]> bRPop(int timeout, byte[]... keys);

    @Process(Policy.WRITE)
    byte[] rPopLPush(byte[] srcKey, byte[] dstKey);

    @Process(Policy.WRITE)
    byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey);

}
