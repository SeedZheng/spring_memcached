package com.test.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by Seed on 2017/9/14.
 */
public class DummyReadWriteLock implements ReadWriteLock {


    private Lock lock = new DummyLock();

    @Override
    public Lock readLock() {
        return lock;
    }

    @Override
    public Lock writeLock() {
        return lock;
    }

    static class DummyLock implements Lock {

        @Override
        public void lock() {
            // Not Implemented
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            // Not Implemented
        }

        @Override
        public boolean tryLock() {
            return true;
        }

        @Override
        public boolean tryLock(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
            return true;
        }

        @Override
        public void unlock() {
            // Not Implemented
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}
