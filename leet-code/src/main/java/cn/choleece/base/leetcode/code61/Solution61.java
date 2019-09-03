package cn.choleece.base.leetcode.code61;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Given a linked list, rotate the list to the right by k places, where k is non-negative.
 * 思路1：构建一个环形链表，然后根据k对环进行切分
 * 思路2：对单链表切分成两条链，然后将第二条链的tail.next = 第一条链的head
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

        int remainder = k % length;
        if (remainder == 0) {
            return head;
        }

        // build a cycle list
        tail.next = head;

        ListNode cur = head;

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

        head = rotateRight(head, 2);

        ListNode.loopList(head);
    }
}

