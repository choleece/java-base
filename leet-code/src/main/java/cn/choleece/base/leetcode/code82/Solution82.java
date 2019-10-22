package cn.choleece.base.leetcode.code82;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Given a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list.
 * Input: 1->2->3->3->4->4->5
 * Output: 1->2->5
 */
public class Solution82 {

    public static ListNode deleteDuplicates(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode tmpHead = new ListNode(-1);
        tmpHead.next = head;

        ListNode first = head, second = head.next, preFirst = tmpHead;

        while (first.next != null) {
            System.out.println("preFirst: " + preFirst.val);

            if (first.val == second.val) {
                second = second.next;
                if (second == null) {
                    preFirst.next = second;
                    return tmpHead.next;
                }
                while (first.val == second.val) {
                    second = second.next;
                    if (second == null) {
                        preFirst.next = second;
                        return tmpHead.next;
                    }
                }
                preFirst.next = second;
                first = second;
                second = first.next;
            } else {
                first = first.next;
                second = second.next;
                preFirst = preFirst.next;
            }
        }

        return tmpHead.next;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.initList(Arrays.asList(1, 2, 3, 4, 5, 5, 5));

        ListNode.loopList(head);

        head = deleteDuplicates(head);

        ListNode.loopList(head);
    }

}
