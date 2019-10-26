package cn.choleece.base.jvm.hotload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: 热加载 原理是通过自定义class loader，判断文件修改时间，如果跟开始的时间不一致，代表文件被修改过，那么就会去执行class loader操作，加载新的类进来
 * @Date 2019-10-23 23:04
 *
 * 参考：https://mp.weixin.qq.com/s/CrDADe0u3aa7Nwu4cH86Zg
 **/
public class HotLoadTest {

    public static Map<String, FileDefine> fileDefineMap = new HashMap();

    public static void main(String[] args) {
        initMap();
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.scheduleAtFixedRate(new WatchDog(fileDefineMap), 0,500, TimeUnit.MICROSECONDS);
    }

    static void initMap() {
        File file = new File(FileDefine.WATCH_PACKAGE);
        File[] files = file.listFiles();
        for (File watchFile : files){
            long l = watchFile.lastModified();
            String name = watchFile.getName();
            FileDefine fileDefine = new FileDefine(name, l);
            fileDefineMap.put(name,fileDefine);
        }
    }
}
