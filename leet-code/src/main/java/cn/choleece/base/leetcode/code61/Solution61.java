package cn.choleece.base.leetcode.code61;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Given a linked list, rotate the list to the right by k places, where k is non-negative.
 */
public class Solution61 {
    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k <= 0) {
            return head;
        }

        ListNode tmpHead = new ListNode(0);
        tmpHead.next = head;
        ListNode tail = head;
        int length = 1;

        while (tail.next != null) {
            tail = tail.next;
            ++length;
        }

        // build a cycle list
        tail.next = head;

        ListNode cur = head;

        int remainder = k % length;
        int step = length - remainder;
        ListNode preTail = tmpHead;
        while (step > 0) {
            step--;
            cur = cur.next;
            preTail = preTail.next;
        }
        tmpHead.next = cur;

        // break out the cycle list
        preTail.next = null;

        return tmpHead.next;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.initList(Arrays.asList(1));

        ListNode.loopList(head);

        head = rotateRight(head, 3);

        ListNode.loopList(head);
    }
}

