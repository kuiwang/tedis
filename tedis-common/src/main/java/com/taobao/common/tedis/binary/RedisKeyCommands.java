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
import java.util.Set;

import com.taobao.common.tedis.config.Process;
import com.taobao.common.tedis.config.Process.Policy;
import com.taobao.common.tedis.util.SortParams;

/**
 * @author juxin.zj E-mail:juxin.zj@taobao.com
 * @since 2011-7-25 上午11:56:15
 * @version 1.0
 */
public interface RedisKeyCommands {

    @Process(Policy.READ)
    Boolean exists(byte[] key);

    @Process(Policy.WRITE)
    Long del(byte[]... keys);

    @Process(Policy.READ)
    String type(byte[] key);

    @Process(Policy.READ)
    Set<byte[]> keys(byte[] pattern);

    @Process(Policy.READ)
    byte[] randomKey();

    @Process(Policy.WRITE)
    Boolean rename(byte[] oldName, byte[] newName);

    @Process(Policy.WRITE)
    Boolean renameNX(byte[] oldName, byte[] newName);

    @Process(Policy.WRITE)
    Boolean expire(byte[] key, long seconds);

    @Process(Policy.WRITE)
    Boolean expireAt(byte[] key, long unixTime);

    @Process(Policy.WRITE)
    Boolean persist(byte[] key);

    @Process(Policy.WRITE)
    Boolean move(byte[] key, int dbIndex);

    @Process(Policy.READ)
    Long ttl(byte[] key);

    @Process(Policy.READ)
    List<byte[]> sort(byte[] key, SortParams params);

    @Process(Policy.READ)
    Long sort(byte[] key, SortParams params, byte[] storeKey);
}
