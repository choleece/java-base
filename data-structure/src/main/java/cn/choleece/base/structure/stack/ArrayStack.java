package cn.choleece.base.structure.stack;

/**
 * @author choleece
 * @Description: 顺序栈（FILO） 用数组实现
 * @Date 2019-09-15 16:08
 **/
public class ArrayStack {

    private int[] datas;

    private int size = 0;

    private int capacity;

    public ArrayStack(int capacity) {
        this.capacity = capacity;
        this.datas = new int[capacity];
    }

    /**
     * 从尾部压入元素
     * @param val
     */
    public boolean push(int val) {
        if (size == capacity) {
            return false;
        }

        datas[size++] = val;
        return true;
    }

    /**
     * 弹出栈顶元素
     */
    public int pop() {
        if (size == 0) {
            return Integer.parseInt(null);
        }

        return datas[--size];
    }

    public void print() {
        for (int i = 0; i < size; i++) {
            System.out.print(datas[i] + " ");
        }
    }

    public static void main(String[] args) {
        ArrayStack stack = new ArrayStack(10);
        for (int i = 0; i < 12; i++) {
            stack.push(i);
        }

        for (int i = 0; i < 11; i++) {
            stack.pop();
        }

        stack.print();
    }


}
