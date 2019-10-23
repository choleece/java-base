package cn.choleece.base.jvm.hotload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-23 23:05
 **/
public class CusClassLoader extends ClassLoader {

    private static final String CLASS_FILE_SUFFIX = ".class";

    private ClassLoader extClassLoader;

    public CusClassLoader() {
        ClassLoader j = String.class.getClassLoader();

        if (j == null) {
            // 一直往下找，直到找到Bootstrap Classloader
            j = getSystemClassLoader();
            while (j.getParent() != null) {
                j = j.getParent();
            }
        }

        this.extClassLoader = j;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class clazz = null;

        clazz = findLoadedClass(name);

        if (clazz != null) {
            return clazz;
        }

        // 获取ext class loader
        ClassLoader extClassLoader = getExtClassLoader();

        // 确保自定义的类不会覆盖Java的核心类
        clazz = extClassLoader.loadClass(name);
        if (clazz != null) {
            return clazz;
        }

        clazz = findClass(name);

        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bt = loadClassData(name);
        return defineClass(name, bt, 0, bt.length);
    }

    private byte[] loadClassData(String className) {
        System.out.println(className);
        InputStream is = getClass().getClassLoader().getResourceAsStream(className.replace(".", "/") + CLASS_FILE_SUFFIX);

        ByteArrayOutputStream byteSt = new ByteArrayOutputStream();

        int len = 0;

        try {
            while ((len = is.read()) != -1) {
                byteSt.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteSt.toByteArray();
    }

    public ClassLoader getExtClassLoader() {
        return extClassLoader;
    }
}
