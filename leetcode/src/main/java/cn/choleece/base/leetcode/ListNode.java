package cn.choleece.base.leetcode;

import java.util.List;

public class ListNode {
    public int val;
    public ListNode next;
    public ListNode(int x) {
        this.val = x;
        this.next = null;
    }

    public ListNode(int x, ListNode next) {
        this.val = x;
        this.next = next;
    }

    public static void loopList(ListNode head) {
        ListNode p = head;
        while (p != null) {
            System.out.print(p.val + "--->");
            p = p.next;
        }
        System.out.print("end");
        System.out.println();
    }

    public static ListNode initList(List<Integer> nums) {
        ListNode head = new ListNode(-1);
        ListNode p = head;
        for (int num : nums) {
            ListNode tmp = new ListNode(num);
            p.next = tmp;
            p = tmp;
        }
        return head;
    }

    public static ListNode initList(List<Integer> nums, int headVal) {
        ListNode head = new ListNode(headVal);
        ListNode p = head;
        for (int num : nums) {
            ListNode tmp = new ListNode(num);
            p.next = tmp;
            p = tmp;
        }
        return head;
    }
}
