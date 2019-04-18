package com.an.bitcoin.protocol;

/**
 * @ClassName VersionGt106
 * @Description VersionGt106
 * @Author an
 * @Date 2019/4/18 上午9:09
 * @Version 1.0
 */
public class VersionGt106 extends Version {

    public Address addrFrom;
    public long nonce;

    public VersionGt106(int version, long service, long timestamp) {
        super(version, service, timestamp);
    }
}
