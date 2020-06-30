package cn.choleece.base.jdk.collection.thread;

import java.util.concurrent.*;

/**
 * @author choleece
 * @Description: future task test
 * @Date 2020-06-20 22:17
 **/
public class FutureTaskClient {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> future = new FutureTask<String>(() -> {

            Thread.sleep(2000);
            return "hello world";
        });

        new Thread(future).start();

        System.out.println("我在这里阔以做很多事情，，，，，future只是等待返回结果");

        System.out.println(future.get());

        System.out.println("我会被阻塞嘛...");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<String> future1 = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello world";
            }
        });

        System.out.println(future1.get());
    }

}
