package cn.choleece.base.jdk.collection.aqs;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2020-06-11 17:24
 */
public class ReentrantReadWriteLockClient {

    public static void main(String[] args) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        lock.readLock().lock();

    }

}

interface A {

}

interface B {

}

interface C extends A, B {

}
