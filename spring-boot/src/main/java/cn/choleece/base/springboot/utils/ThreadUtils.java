package cn.choleece.base.springboot.utils;

/**
 * @author choleece
 * @Description: the util of thread
 * @Date 2020-09-05 21:40
 **/
public class ThreadUtils {

    /**
     * 通过内核数，算出合适的线程数；1.5-2倍cpu内核数
     *
     * @return thread count
     */
    public static int getSuitableThreadCount() {
        final int coreCount = Runtime.getRuntime().availableProcessors();
        int workerCount = 1;
        while (workerCount < coreCount * THREAD_MULTIPLIER) {
            workerCount <<= 1;
        }
        return workerCount;
    }

    private final static int THREAD_MULTIPLIER = 2;

}
