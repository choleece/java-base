package cn.choleece.base.structure.speed;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 20:27
 **/
public class TaskSingleton {

    public static TaskSingleton singleton = new TaskSingleton();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    Lock readLock = readWriteLock.readLock();

    Lock writeLock = readWriteLock.writeLock();

    private Map<String, SpeedTaskState> taskMap = new HashMap<String, SpeedTaskState>(256);

    public void putTask(String taskName, SpeedTaskState task) {
        try {
            writeLock.lock();

            taskMap.put(taskName, task);
        } finally {
            writeLock.unlock();
        }
    }

    public SpeedTaskState getTask(String taskName) {
        try {
            readLock.lock();

            return taskMap.get(taskName);
        } finally {
            readLock.unlock();
        }
    }
}
