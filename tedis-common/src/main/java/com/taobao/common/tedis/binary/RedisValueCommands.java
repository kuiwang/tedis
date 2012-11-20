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
import java.util.Map;

import com.taobao.common.tedis.config.Process;
import com.taobao.common.tedis.config.Process.Policy;


/**
 * @author juxin.zj E-mail:juxin.zj@taobao.com
 * @since 2011-7-25 上午11:41:46
 * @version 1.0
 */
public interface RedisValueCommands {

    @Process(Policy.READ)
    byte[] get(byte[] key);

    @Process(Policy.WRITE)
    byte[] getSet(byte[] key, byte[] value);

    @Process(Policy.READ)
    List<byte[]> mGet(byte[]... keys);

    @Process(Policy.WRITE)
    Boolean set(byte[] key, byte[] value);

    @Process(Policy.WRITE)
    Boolean setNX(byte[] key, byte[] value);

    @Process(Policy.WRITE)
    Boolean setEx(byte[] key, long seconds, byte[] value);

    @Process(Policy.WRITE)
    Boolean mSet(Map<byte[], byte[]> tuple);

    @Process(Policy.WRITE)
    Boolean mSetNX(Map<byte[], byte[]> tuple);

    @Process(Policy.WRITE)
    Long incr(byte[] key);

    @Process(Policy.WRITE)
    Long incrBy(byte[] key, long value);

    @Process(Policy.WRITE)
    Long decr(byte[] key);

    @Process(Policy.WRITE)
    Long decrBy(byte[] key, long value);

    @Process(Policy.WRITE)
    Long append(byte[] key, byte[] value);

    @Process(Policy.READ)
    byte[] getRange(byte[] key, long begin, long end);

    @Process(Policy.WRITE)
    Long setRange(byte[] key, byte[] value, long offset);

    @Process(Policy.READ)
    Boolean getBit(byte[] key, long offset);

    @Process(Policy.WRITE)
    Long setBit(byte[] key, long offset, boolean value);

    @Process(Policy.READ)
    Long strLen(byte[] key);

}
