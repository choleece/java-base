package cn.choleece.base.leetcode.code92;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Reverse a linked list from position m to n. Do it in one-pass.
 * Note: 1 ≤ m ≤ n ≤ length of list.
 */
public class Solution92 {

    public static ListNode reverseBetween(ListNode head, int m, int n) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode newHead = new ListNode(-1);
        newHead.next = head;

        ListNode cur = head, stayPoint = newHead;

        int i = 1;
        while (i < m && cur != null) {
            i++;
            cur = cur.next;
            stayPoint = stayPoint.next;
        }

        if (stayPoint.next == null || stayPoint.next.next == null) {
            return head;
        }

        ListNode preCur = cur;
        cur = cur.next;

        for (; i < n && cur != null; i++) {
            preCur.next = cur.next;
            cur.next = stayPoint.next;
            stayPoint.next = cur;

            cur = preCur.next;
        }

        return newHead.next;
    }

    public static void main(String[] args) {

        ListNode head = ListNode.initList(Arrays.asList(2, 3, 4, 5));

        ListNode.loopList(head);

        head = reverseBetween(head, 2, 4);

        ListNode.loopList(head);

    }

}
