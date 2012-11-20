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
import java.util.Set;

import com.taobao.common.tedis.config.Process;
import com.taobao.common.tedis.config.Process.Policy;

/**
 *
 * @author juxin.zj E-mail:juxin.zj@taobao.com
 * @since 2011-7-25 09:08:49
 * @version 1.0
 */
public interface RedisHashCommands {

    @Process(Policy.WRITE)
    Boolean hSet(byte[] key, byte[] field, byte[] value);

    @Process(Policy.WRITE)
    Boolean hSetNX(byte[] key, byte[] field, byte[] value);

    @Process(Policy.READ)
    byte[] hGet(byte[] key, byte[] field);

    @Process(Policy.READ)
    List<byte[]> hMGet(byte[] key, byte[]... fields);

    @Process(Policy.WRITE)
    Boolean hMSet(byte[] key, Map<byte[], byte[]> hashes);

    @Process(Policy.WRITE)
    Long hIncrBy(byte[] key, byte[] field, long delta);

    @Process(Policy.READ)
    Boolean hExists(byte[] key, byte[] field);

    @Process(Policy.WRITE)
    Long hDel(byte[] key, byte[]... field);

    @Process(Policy.READ)
    Long hLen(byte[] key);

    @Process(Policy.READ)
    Set<byte[]> hKeys(byte[] key);

    @Process(Policy.READ)
    List<byte[]> hVals(byte[] key);

    @Process(Policy.READ)
    Map<byte[], byte[]> hGetAll(byte[] key);

}
