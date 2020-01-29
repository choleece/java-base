package cn.choleece.base.leetcode.code234;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;
import java.util.Stack;

/**
 * Given a singly linked list, determine if it is a palindrome.(回文)
 */
public class Solution234 {

    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }

        Stack<Integer> stack = new Stack<Integer>();
        ListNode cur = head;

        while (cur != null) {
            stack.push(cur.val);
            cur = cur.next;
        }

        cur = head;
        while (cur != null) {
            if (cur.val != stack.pop()) {
                return false;
            }
            cur = cur.next;
        }
        return true;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.initList(Arrays.asList(1, 1, 0));

        ListNode.loopList(head);

        System.out.println(isPalindrome(head));
    }

}
