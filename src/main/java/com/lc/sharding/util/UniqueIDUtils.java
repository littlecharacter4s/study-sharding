package com.lc.sharding.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public final class UniqueIDUtils {
    private static final Logger logger = LoggerFactory.getLogger(UniqueIDUtils.class);
    // 取IP地址最后一段作为多机分隔段
    private static long SERVER_ID = 255L;

    /**
     * 数据库id
     */
    private static long DB_ID = 63;

    /**
     * 序列号
     */
    private long sequence;

    /**
     * 开始时间戳
     */
    private final long beginTime = 1514736000000L;

    /**
     * 服务器个数比特位个数
     */
    private final long serverBits = 8L;

    /**
     * 数据库个数比特位个数
     */
    private final long dbBits = 6L;
    /**
     * 序列号比特位个数
     */
    private final long sequenceBits = 5L;

    /**
     * 服务器最大个数
     */
    private final long maxServerCount = -1L ^ (-1L << serverBits);
    /**
     * 数据库最大个数
     */
    private final long maxDbCount = -1L ^ (-1L << dbBits);


    private final long sequenceMask = -1 ^ (-1L << sequenceBits);

    /**
     * 数据库位移量
     */
    private final long dbShift = sequenceBits;

    /**
     * 服务器位移量
     */
    private final long serverShift = dbShift + dbBits;

    /**
     * 时间戳位移量
     */
    private final long timeStampLeftShift = serverShift + serverBits;

    /**
     * 上次生成id的时间戳
     */
    private long lastTimeStamp = -1L;

    static {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String[] nums = hostAddress.split("\\.");
            SERVER_ID = Long.parseLong(nums[3]);
        } catch (Exception ex) {
            logger.error("UniqueIDUtils init Exception, Default SERVER_ID = " + SERVER_ID, ex);
        }
    }


    public synchronized long nextId() {
        long timeStamp = timeGen();
        //当前时间小于上次生成id的时间戳，说明系统时钟回退,更新当前时间，保证id生成器可用
        if (timeStamp < lastTimeStamp) {
            timeStamp = tilNextMills(lastTimeStamp);
        }
        //序号与当前时间戳无关，每次递增
        sequence = (sequence + 1) & sequenceMask;
        //当同一时间戳内的并发量大于序号的最大值，则时间戳向后增加
        if (sequence == 0L && timeStamp == lastTimeStamp) {
            timeStamp = tilNextMills(timeStamp);
        }
        return (timeStamp - beginTime) << timeStampLeftShift | SERVER_ID << serverShift | DB_ID << dbShift | sequence;
    }

    protected long tilNextMills(long lastTimeStamp) {
        long timeStamp = timeGen();
        while (timeStamp < lastTimeStamp) {
            timeStamp = timeGen();
        }
        return timeStamp;
    }

    /**
     * 返回当前时间，单位毫秒
     *
     * @return 当前时间（毫秒）
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }


    public long bulidOrderId(long userId) {
        //取用户id后4位
        userId = userId & 15;
        //先取60位唯一id
        long uniqueId = this.nextId();
        //唯一id左移4位、拼接userId后4位
        return (uniqueId << 4) | userId;
    }
}


