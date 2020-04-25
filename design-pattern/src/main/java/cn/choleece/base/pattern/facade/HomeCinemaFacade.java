package cn.choleece.base.pattern.facade;

/**
 * @author choleece
 * @Description: 电影院门面设计模式
 * @Date 2020-04-23 23:30
 **/
public class HomeCinemaFacade {

    private Light light;

    private Video video;

    public HomeCinemaFacade() {
        light = Light.getInstance();
        video = Video.getInstance();
    }

    public void on() {
        light.on();
        video.on();
    }

    public void off() {
        light.off();
        video.off();
    }

    public static void main(String[] args) {
        HomeCinemaFacade facade = new HomeCinemaFacade();

        facade.on();
        facade.off();
    }
}
