package com.graduation.oa.support;

public class SnowFlake {
    private static final long START_STMP = 1480166465631L;
    private static final long SEQUENCE_BIT = 12L;
    private static final long MACHINE_BIT = 5L;
    private static final long DATACENTER_BIT = 5L;
    private static final long MAX_DATACENTER_NUM = 31L;
    private static final long MAX_MACHINE_NUM = 31L;
    private static final long MAX_SEQUENCE = 4095L;
    private static final long MACHINE_LEFT = 12L;
    private static final long DATACENTER_LEFT = 17L;
    private static final long TIMESTMP_LEFT = 22L;
    private long datacenterId;
    private long machineId;
    private long sequence = 0L;
    private long lastStmp = -1L;
    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId <= 31L && datacenterId >= 0L) {
            if (machineId <= 31L && machineId >= 0L) {
                this.datacenterId = datacenterId;
                this.machineId = machineId;
            } else {
                throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
            }
        } else {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
    }

    public synchronized long nextId() {
        long currStmp = this.getNewstmp();
        if (currStmp < this.lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        } else {
            if (currStmp == this.lastStmp) {
                this.sequence = this.sequence + 1L & 4095L;
                if (this.sequence == 0L) {
                    currStmp = this.getNextMill();
                }
            } else {
                this.sequence = 0L;
            }

            this.lastStmp = currStmp;
            return currStmp - 1480166465631L << 22 | this.datacenterId << 17 | this.machineId << 12 | this.sequence;
        }
    }

    private long getNextMill() {
        long mill;
        for(mill = this.getNewstmp(); mill <= this.lastStmp; mill = this.getNewstmp()) {
        }

        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public String toOtherNumberSystem(long number, int seed) {
        if (number < 0L) {
            number = 4294967294L + number + 2L;
        }

        char[] buf = new char[32];

        int charPos;
        for(charPos = 32; number / (long)seed > 0L; number /= (long)seed) {
            --charPos;
            buf[charPos] = digits[(int)(number % (long)seed)];
        }

        --charPos;
        buf[charPos] = digits[(int)(number % (long)seed)];
        return new String(buf, charPos, 32 - charPos);
    }

    public long toDecimalNumber(String number, int seed) {
        char[] charBuf = number.toCharArray();
        if (seed == 10) {
            return Long.parseLong(number);
        } else {
            long result = 0L;
            long base = 1L;

            for(int i = charBuf.length - 1; i >= 0; --i) {
                int index = 0;
                int j = 0;

                for(int length = digits.length; j < length; ++j) {
                    if (digits[j] == charBuf[i]) {
                        index = j;
                    }
                }

                result += (long)index * base;
                base *= (long)seed;
            }

            return result;
        }
    }
}
