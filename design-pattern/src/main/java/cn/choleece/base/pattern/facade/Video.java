package cn.choleece.base.pattern.facade;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-23 23:31
 **/
public class Video {

    private static Video video = new Video();

    public static Video getInstance() {
        return video;
    }

    public void on() {
        System.out.println("video is on...");
    }

    public void off() {
        System.out.println("video is off...");
    }
}
