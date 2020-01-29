package cn.choleece.base.leetcode.code83;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Given a sorted linked list, delete all duplicates such that each element appear only once.
 * 思路1：逐个遍历，重复了就删除后边那个，注意边界
 */
public class Solution83 {

    public static ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode first = head, second = head.next;
        while (second != null) {
            if (first.val == second.val) {
                first.next = second.next;
                second = first.next;
            } else {
                first = first.next;
                second = second.next;
            }
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.initList(Arrays.asList(0));

        ListNode.loopList(head);

        head = deleteDuplicates(head);

        ListNode.loopList(head);
    }

}
