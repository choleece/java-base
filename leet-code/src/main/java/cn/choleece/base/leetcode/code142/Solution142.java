package cn.choleece.base.leetcode.code142;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Given a linked list, return the node where the cycle begins. If there is no cycle, return null.
 *
 * To represent a cycle in the given linked list, we use an integer pos which represents the position (0-indexed) in the linked list where tail connects to. If pos is -1, then there is no cycle in the linked list.
 */
public class Solution142 {

    public static ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }

        if (head == head.next) {
            return head;
        }

        Set<ListNode> set = new HashSet<ListNode>();

        ListNode cur = head;

        while (cur != null) {
            if (set.contains(cur)) {
                return cur;
            }
            set.add(cur);
            cur = cur.next;
        }

        return null;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.initList(Arrays.asList(1, 2, 3, 4, 5, 6));

        head.next.next.next.next.next = head.next.next.next.next;

        ListNode cycleBeginNode = detectCycle(head);

        System.out.println(cycleBeginNode == null ? "no cycle" : "cycle begin node" + cycleBeginNode.val);
    }

}
