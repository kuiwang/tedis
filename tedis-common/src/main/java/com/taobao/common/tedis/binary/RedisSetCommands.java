/**
 * (C) 2011-2012 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 */
package com.taobao.common.tedis.binary;

import java.util.Set;

import com.taobao.common.tedis.config.Process;
import com.taobao.common.tedis.config.Process.Policy;

/**
 * @author juxin.zj E-mail:juxin.zj@taobao.com
 * @since 2011-7-25 09:11:26
 * @version 1.0
 */
public interface RedisSetCommands {

    @Process(Policy.WRITE)
    Long sAdd(byte[] key, byte[]... value);

    @Process(Policy.WRITE)
    Long sRem(byte[] key, byte[]... value);

    @Process(Policy.READ)
    byte[] sPop(byte[] key);

    @Process(Policy.WRITE)
    Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value);

    @Process(Policy.READ)
    Long sCard(byte[] key);

    @Process(Policy.READ)
    Boolean sIsMember(byte[] key, byte[] value);

    @Process(Policy.READ)
    Set<byte[]> sInter(byte[]... keys);

    @Process(Policy.WRITE)
    Long sInterStore(byte[] destKey, byte[]... keys);

    @Process(Policy.READ)
    Set<byte[]> sUnion(byte[]... keys);

    @Process(Policy.WRITE)
    Long sUnionStore(byte[] destKey, byte[]... keys);

    @Process(Policy.READ)
    Set<byte[]> sDiff(byte[]... keys);

    @Process(Policy.WRITE)
    Long sDiffStore(byte[] destKey, byte[]... keys);

    @Process(Policy.READ)
    Set<byte[]> sMembers(byte[] key);

    @Process(Policy.READ)
    byte[] sRandMember(byte[] key);
}
