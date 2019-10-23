package cn.choleece.base.jvm.hotload;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-23 23:24
 **/
public class WatchDog implements Runnable {

    private Map<String, FileDefine> fileDefineMap;

    public WatchDog(Map<String, FileDefine> fileDefineMap) {
        this.fileDefineMap = fileDefineMap;
    }

    @Override
    public void run() {

        File file = new File(FileDefine.WATCH_PACKAGE);

        File[] files = file.listFiles();

        for (File watchFile : files) {
            long newTime = watchFile.lastModified();
            FileDefine fileDefine = fileDefineMap.get(watchFile.getName());
            long oldTime = fileDefine.getLastDefine();
            System.out.println("我在执行没,," + newTime + "   " + oldTime);
            // 如果被修改了
            if (newTime != oldTime) {
                loadMyClass();
            }
        }
    }

    public void loadMyClass() {
        try {
            CusClassLoader cusClassLoader = new CusClassLoader();

            Class<?> clazz = cusClassLoader.loadClass("cn.choleece.base.jvm.hotload.VersionTest.class", false);

            Object test = clazz.newInstance();

            Method method = clazz.getMethod("printVersion");

            method.invoke(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
