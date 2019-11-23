package cn.choleece.base.framework.spring.aop.entity;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-23 22:36
 **/
public class User {
    private String firstName;

    private String lastName;

    private int age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
