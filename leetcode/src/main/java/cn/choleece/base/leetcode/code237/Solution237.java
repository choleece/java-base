package cn.choleece.base.leetcode.code237;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * 直接指向下一个节点
 * Write a function to delete a node (except the tail) in a singly linked list, given only access to that node.
 */
public class Solution237 {

    public void deleteNode(ListNode node) {
        while (node != null && node.next != null) {
            node.val = node.next.val;
            node.next = node.next.next;
        }
    }

    public static void main(String[] args) {
        ListNode head = ListNode.initList(Arrays.asList(1, 2, 3, 4, 5, 6));
    }

}
