package com.easymeeting.utils;

/**
 * 雪花算法 ID 生成器，默认 5 位机房 + 5 位机器 + 12 位序列。
 */
public class SnowFlakeUtils {

    private static final long START_TIMESTAMP = 1704067200000L; // 2024-01-01

    private static final long SEQUENCE_BITS = 12L;
    private static final long MACHINE_BITS = 5L;
    private static final long DATACENTER_BITS = 5L;

    private static final long MAX_DATACENTER = ~(-1L << DATACENTER_BITS);
    private static final long MAX_MACHINE = ~(-1L << MACHINE_BITS);

    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    private static final long MACHINE_LEFT = SEQUENCE_BITS;
    private static final long DATACENTER_LEFT = SEQUENCE_BITS + MACHINE_BITS;
    private static final long TIMESTAMP_LEFT = SEQUENCE_BITS + MACHINE_BITS + DATACENTER_BITS;

    private static final long DATA_CENTER_ID = resolveBoundedId(System.getProperty("snowflake.datacenterId"), MAX_DATACENTER);
    private static final long MACHINE_ID = resolveBoundedId(System.getProperty("snowflake.machineId"), MAX_MACHINE);

    private static long sequence = 0L;
    private static long lastTimestamp = -1L;

    private SnowFlakeUtils() {
    }

    public static synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards, refuse to generate id");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0L) {
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT)
                | (DATA_CENTER_ID << DATACENTER_LEFT)
                | (MACHINE_ID << MACHINE_LEFT)
                | sequence;
    }

    private static long waitNextMillis(long lastTime) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTime) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    private static long resolveBoundedId(String raw, long max) {
        long parsed;
        try {
            parsed = raw == null ? 0L : Long.parseLong(raw);
        } catch (NumberFormatException ex) {
            parsed = 0L;
        }
        if (parsed < 0L) {
            parsed = 0L;
        }
        if (parsed > max) {
            parsed = max;
        }
        return parsed;
    }
}

