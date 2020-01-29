package cn.choleece.base.leetcode.code24;

import java.util.Arrays;
import java.util.List;

/**
 * 交换两个相连的元素
 *
 * 思路：快慢指针
 *
 * Given a linked list, swap every two adjacent nodes and return its head.
 *
 * You may not modify the values in the list's nodes, only nodes itself may be changed.
 */
public class Solution24 {
    public static ListNode swapPairs(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode dumy = new ListNode(0);
        dumy.next = head;
        ListNode first = head, second = first.next, p = dumy;

        while (second != null) {
            first.next = second.next;
            second.next = first;

            p.next = second;
            p = first;

            first = first.next;
            if (first != null) {
                second = first.next;
            } else {
                second = null;
            }
        }

        return dumy.next;
    }

    public static void main(String[] args) {

        ListNode head = initList(Arrays.asList(1, 2));
        loopList(head);

        head = swapPairs(head);

        loopList(head);
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
        ListNode head = new ListNode(0);
        ListNode p = head;
        for (int num : nums) {
            ListNode tmp = new ListNode(num);
            p.next = tmp;
            p = tmp;
        }
        return head;
    }

}

class ListNode {

    public int val;

    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }
}
