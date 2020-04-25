package cn.choleece.base.pattern.facade;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-23 23:31
 **/
public class Light {

    private static Light light = new Light();

    public static Light getInstance() {
        return light;
    }

    public void on() {
        System.out.println("light is on...");
    }

    public void off() {
        System.out.println("light is off...");
    }

}
