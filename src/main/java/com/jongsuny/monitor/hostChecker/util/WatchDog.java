package com.jongsuny.monitor.hostChecker.util;

/**
 * Created by jongsuny on 17/10/30.
 */
public class WatchDog {
    private long start = 0;
    private long elapsed = 0;
    /**
     * start stop watch
     */
    public void start() {
        start = System.currentTimeMillis();
    }
    public long getStart(){
        return start;
    }
    /**
     * end stop watch
     */
    public long end() {
        long current = System.currentTimeMillis();
        elapsed = current - start;
        return elapsed;
    }

    /**
     * return elapsed time millisec
     */
    public long elapsed() {
        return elapsed;
    }

    /**
     * return elapsed time nanosec
     */
    public long getElapsedSnapshot() {
        return System.currentTimeMillis() - start;
    }
}
