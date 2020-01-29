package cn.choleece.base.leetcode.code21;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * @description: 合并两个排序好的链表
 * @author: sf
 * @time: 2019-10-22 16:40
 */
public class Solution21 {

    public static ListNode mergeTwoSortedList(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }

        if (l2 == null) {
            return l1;
        }

        ListNode resultList = new ListNode(-1);
        ListNode tail = resultList;

        ListNode p = l1, q = l2;

        while (p != null && q != null) {
            if (p.val < q.val) {
                tail.next = new ListNode(p.val);
                p = p.next;
            } else {
                tail.next = new ListNode(q.val);
                q = q.next;
            }

            tail = tail.next;
        }

        if (p != null) {
            tail.next = p;
        }

        if (q != null) {
            tail.next = q;
        }

        return resultList.next;
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode resultListNode = new ListNode(0);
        ListNode p = l1, q = l2, r = resultListNode;
        while (p != null && q != null) {
            if (p.val < q.val) {
                r.val = p.val;
                p = p.next;
            } else {
                r.val = q.val;
                q = q.next;
            }
            r.next = new ListNode(0);
            r = r.next;
        }
        while (p != null) {
            r.val = p.val;
            r.next = p.next;
            p = p.next;
            r = r.next;
        }
        while (q != null) {
            r.val = q.val;
            r.next = q.next;
            q = q.next;
            r = r.next;
        }
        return resultListNode;
    }

    public static void main(String[] args) {
        ListNode list1 = ListNode.initList(Arrays.asList(1, 3, 4, 5, 7, 9));
        ListNode list2 = ListNode.initList(Arrays.asList(2, 4, 6, 8, 10));

        ListNode root = mergeTwoSortedList(list1, list2);

        ListNode.loopList(root);
    }
}
