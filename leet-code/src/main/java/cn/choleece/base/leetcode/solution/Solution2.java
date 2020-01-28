package cn.choleece.base.leetcode.solution;

import cn.choleece.base.leetcode.ListNode;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-28 22:49
 **/
public class Solution2 {
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        ListNode node = new ListNode(-1);

        ListNode dummyHead = node;

        int jin = 0;
        while (l1 != null || l2 != null) {
            int val1 = 0, val2 = 0;
            if (l1 != null) {
                val1 = l1.val;
                l1 = l1.next;
            }

            if (l2 != null) {
                val2 = l2.val;
                l2 = l2.next;
            }
            int sum = val1 + val2 + jin;
            jin = sum / 10;

            node.next = new ListNode(sum % 10);

            node = node.next;
        }

        if (jin > 0) {
            node.next = new ListNode(jin);
        }
        return dummyHead.next;
    }


    public static void main(String[] args) {
        ListNode l1 = new ListNode(2, new ListNode(4, new ListNode(3)));
        ListNode l2 = new ListNode(5, new ListNode(6, new ListNode(4)));

        ListNode.loopList(l1);

        ListNode.loopList(addTwoNumbers(l1, l2));
    }
}