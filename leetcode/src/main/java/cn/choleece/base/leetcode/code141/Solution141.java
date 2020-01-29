package cn.choleece.base.leetcode.code141;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Given a linked list, determine if it has a cycle in it.
 *
 * To represent a cycle in the given linked list, we use an integer pos which represents the position (0-indexed) in the linked list where tail connects to. If pos is -1, then there is no cycle in the linked list.
 */
public class Solution141 {

    /***
     * 判断重复
     * @param head
     * @return
     */
    public static boolean hasCycle1(ListNode head) {

        if (head == null || head.next == null) {
            return false;
        }

        if (head.next == head) {
            return true;
        }

        Set<ListNode> set = new HashSet<ListNode>();
        ListNode cur = head.next.next;
        while (cur != null) {
            if (set.contains(cur)) {
                return true;
            }

            set.add(cur);
            cur = cur.next;
        }

        return false;
    }

    /**
     * 快慢指针
     * @param head
     * @return
     */
    public static boolean hasCycle(ListNode head) {

        if (head == null || head.next == null) {
            return false;
        }

        if (head.next == head) {
            return true;
        }

        ListNode fast = head.next.next, slow = head;

        while (fast != null && fast.next != null) {
            if (fast.equals(slow)) {
                return true;
            }
            fast = fast.next.next;
            slow = slow.next;
        }

        return false;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.initList(Arrays.asList(1, 2, 3));

        head.next.next.next = head.next;

        System.out.println(hasCycle(head));
    }

}
