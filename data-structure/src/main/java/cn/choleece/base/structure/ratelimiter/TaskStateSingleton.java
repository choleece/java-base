package cn.choleece.base.structure.ratelimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 19:47
 **/
public class TaskStateSingleton {

    // 统计某一任务类型的访问情况
    private Map<String, TaskStateDO> taskStateMap = new HashMap<String, TaskStateDO>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Lock read = lock.readLock();

    private Lock write = lock.writeLock();

    private TaskStateSingleton() {
    }

    // 获取实例
    public static TaskStateSingleton getInstance() {
        return SingletonHolder.taskStateSingleton;
    }

    private static class SingletonHolder {
        private static final TaskStateSingleton taskStateSingleton = new TaskStateSingleton();
    }

    /**
     * 新增一个任务统计信息
     */
    public void putTaskStateDO(String taskType, TaskStateDO taskStateDO) {
        try {
            write.lock();
            taskStateMap.put(taskType, taskStateDO);
        } finally {
            write.unlock();
        }
    }

    /**
     * 查询一个任务统计信息
     */
    public TaskStateDO getTaskStateDO(String taskType) {
        try {
            read.lock();
            return taskStateMap.get(taskType);
        } finally {
            read.unlock();
        }
    }
}
