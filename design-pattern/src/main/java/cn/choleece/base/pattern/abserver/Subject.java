package cn.choleece.base.pattern.abserver;

import java.util.LinkedList;
import java.util.List;

/**
 * 抽象主题（Subject）角色：也叫抽象目标类，它提供了一个用于保存观察者对象的聚集类和增加、删除观察者对象的方法，以及通知所有观察者的抽象方法
 * @author choleece
 */
public abstract class Subject {

    /**
     * 观察者队列，用于存放观察者
     */
    protected List<Observer> observers = new LinkedList<>();

    /**
     * 向观察者队列中添加观察者
     * @param observer
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * 将观察者从观察者队列中移除
     * @param observer
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * 通知观察者队列里的观察者，具体实现交给具体的主题去实现
     */
    public abstract void notifyObservers();
}
