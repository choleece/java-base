package cn.choleece.base.leetcode.code19;

import java.util.Arrays;
import java.util.List;

/**
 * 解题思路：快慢指针
 * 注意：注意边界
 * Given a linked list, remove the n-th node from the end of list and return its head
 * @author choleece
 */
public class Solution19 {

    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null || n <= 0) {
            return head;
        }

        ListNode p = head, cur = head;

        int start = 1, length = 0;
        while (cur.next != null) {
            length++;
            if (start < n + 1) {
                start++;
            } else {
                p = p.next;
            }
            cur = cur.next;
        }

        if (n == length + 1) {
            head = head.next;
        } else if (p.next == null) {
            head = null;
        } else {
            p.next = p.next.next;
        }

        return head;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(0);
        ListNode p = head;
        List<Integer> nums = Arrays.asList(1);
        for (int num : nums) {
            ListNode tmp = new ListNode(num);
            p.next = tmp;
            p = tmp;
        }

        loopList(head);

        ListNode newHead = removeNthFromEnd(head, 1);

        loopList(newHead);
    }

    public static void loopList(ListNode head) {
        ListNode p = head;
        while (p != null) {
            System.out.print(p.val + "--->");
            p = p.next;
        }
        System.out.print("end");
        System.out.println();
    }
}

class ListNode {

    public int val;

    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
