package cn.choleece.base.leetcode.code206;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Reverse a singly linked list.
 */
public class Solution206 {

    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode newHead = new ListNode(-1);
        newHead.next = head;

        ListNode cur = head.next, preCur = head;

        while (cur != null) {
            preCur.next = cur.next;
            cur.next = newHead.next;
            newHead.next = cur;

            cur = preCur.next;
        }
        return newHead.next;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.initList(Arrays.asList(1, 2, 3, 4, 5));

        ListNode.loopList(head);

        head = reverseList(head);

        ListNode.loopList(head);
    }

}
