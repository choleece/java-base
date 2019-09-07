package cn.choleece.base.leetcode.code328;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Given a singly linked list, group all odd nodes together followed by the even nodes. Please note here we are talking about the node number and not the value in the nodes.
 */
public class Solution328 {

    public static ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode cur = head.next.next;
        ListNode preCur = head;




        return head;
    }

    public static void main(String[] args) {

        ListNode head = ListNode.initList(Arrays.asList(2, 3, 4, 5));

        ListNode.loopList(head);

        ListNode.loopList(oddEvenList(head));

    }
}
