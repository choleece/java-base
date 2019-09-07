package cn.choleece.base.leetcode.code203;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Remove all elements from a linked list of integers that have value val.
 */
public class Solution203 {

    public static ListNode removeElements(ListNode head, int val) {
        if (head == null) {
            return null;
        }

        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode cur = head, preCur = dummy;

        while (cur != null) {
            if (val == cur.val) {
                preCur.next = cur.next;
            } else {
                preCur = preCur.next;
            }
            cur = cur.next;
        }

        return dummy.next;
    }

    public static void main(String[] args) {

        ListNode head = ListNode.initList(Arrays.asList(1, 1, 3, 4, 5, 6, 7, 0));

        ListNode.loopList(head);

        ListNode.loopList(removeElements(head, 0));

    }
}
