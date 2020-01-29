package cn.choleece.base.leetcode.code160;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Write a program to find the node at which the intersection of two singly linked lists begins.
 */
public class Solution160 {

    public static ListNode getIntersectionNode1(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        ListNode curA = headA, curB = headB;

        Set<ListNode> set = new HashSet<ListNode>();

        while (curA != null || curB != null) {
            if (curA != null) {
                if (set.contains(curA)) {
                    return curA;
                } else {
                    set.add(curA);
                    curA = curA.next;
                }
            }

            if (curB != null) {
                if (set.contains(curB)) {
                    return curB;
                } else {
                    set.add(curB);
                    curB = curB.next;
                }
            }
        }

        return null;
    }

    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        ListNode curA = headA, curB = headB;

        /**
         * 当某一条链到达最后一个节点的时候，从另一条链的开头开始，必然在第二遍的时候，两者会在交叉点相遇
         */
        while (curA != curB) {
            curA = curA == null ? headB : curA.next;
            curB = curB == null ? headA : curB.next;
        }

        return curA;
    }

    public static void main(String[] args) {
        ListNode headA = ListNode.initList(Arrays.asList(1, 2, 3, 4, 5, 6));

        ListNode headB = ListNode.initList(Arrays.asList(1, 2, 3, 4, 5, 6));
    }
}
