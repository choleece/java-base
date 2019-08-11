package cn.choleece.base.leetcode;

public class ReverseList {

    public static void main(String[] args) {
        ReverseListNode listNode = new ReverseListNode(-1);
        ReverseListNode p = listNode, cur = listNode;
        for (int i = 0; i < 5; i++) {
            ReverseListNode tmp = new ReverseListNode(i);
            cur.next = new ReverseListNode(i);
            cur = cur.next;
        }

        while (p != null) {
            System.out.println(p.val);
            p = p.next;
        }

        ReverseListNode q = reverse(listNode);
        while (q != null) {
            System.out.println(q.val);
            q = q.next;
        }

    }

    static ReverseListNode reverse(ReverseListNode list) {
        ReverseListNode cur = list;
        ReverseListNode head = list;
        while (cur.next != null) {
            ReverseListNode tmp = cur.next;
            cur.next = tmp.next;
            tmp.next = head;
            head = tmp;
        }
        return head;
    }
}

class ReverseListNode {
    public int val;

    public ReverseListNode next;

    public ReverseListNode() {
    }

    public ReverseListNode(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public ReverseListNode getNext() {
        return next;
    }

    public void setNext(ReverseListNode next) {
        this.next = next;
    }
}
