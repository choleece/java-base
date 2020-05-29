package cn.choleece.base.jdk.collection.aqs;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author choleece
 * @Description: 写时复制list
 * @Date 2020-05-25 21:20
 **/
public class CopyOnWriteArrayListClient {

    public static void main(String[] args) {
        CopyOnWriteArrayList<String> strings = new CopyOnWriteArrayList<>();

        strings.add("444");
    }
}
