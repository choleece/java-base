package cn.choleece.base.leetcode.code328;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Given a singly linked list, group all odd nodes together followed by the even nodes. Please note here we are talking about the node number and not the value in the nodes.
 * 构建两条链 奇数的尾部指向偶数链头部
 */
public class Solution328 {

    public static ListNode oddEvenList1(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode odd = head, even = head.next, evenHead = even;
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;

            even.next = odd.next;
            even = even.next;
        }
        odd.next = evenHead;
        return head;
    }

    public static ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode oddHead = head, cur = head.next;
        while (cur != null) {
            if (cur.next != null) {
                ListNode tmp = oddHead.next;

                oddHead.next = cur.next;
                cur.next = cur.next.next;
                oddHead.next.next = tmp;

                oddHead = oddHead.next;
            }
            cur = cur.next;
        }
        return head;
    }



    public static void main(String[] args) {

        ListNode head = ListNode.initList(Arrays.asList(1, 3, 5, 6, 4, 7));

        ListNode.loopList(head);

        ListNode.loopList(oddEvenList(head));

    }
}
