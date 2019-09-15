package cn.choleece.base.pattern.abserver;

/**
 * 具体主题（Concrete Subject）角色：也叫具体目标类，它实现抽象目标中的通知方法，当具体主题的内部状态发生改变时，通知所有注册过的观察者对象。
 * @author choleece
 */
public class ConcreteSubject extends Subject {

    private String msg = "hello,every one";

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void changeMsg(String msg) {
        setMsg(msg);

        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        if (observers != null && !observers.isEmpty()) {
            observers.forEach(observer -> observer.update(this.msg));
        }
    }
}
