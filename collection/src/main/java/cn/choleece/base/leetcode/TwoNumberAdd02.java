package cn.choleece.base.leetcode;

/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 *
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 *
 */
public class TwoNumberAdd02 {

    public static ListNode addTwoNumners(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) {
            throw new IllegalArgumentException("illegal list");
        }
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        int carry = 0;
        ListNode resultList = null, tail = null;
        while (l1 != null || l2 != null) {
            int x = l1 != null ? l1.val : 0;
            int y = l2 != null ? l2.val : 0;
            int result = x + y + carry;
            carry = result / 10;
            if (resultList == null) {
                resultList = new ListNode(result % 10);
                tail = resultList;
            } else {
                tail.next = new ListNode(result % 10);
                tail = tail.next;
            }
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }

        if (carry > 0) {
            tail.next = new ListNode(carry);
        }

        return resultList;
    }

    public static void main(String[] args) {
        ListNode l1 = null, l2 = null, p = null, q = null;
        int[] nums1 = {2, 4, 3};
        int[] nums2 = {5, 6, 4};

        for (int i = 0; i < nums1.length; i++) {
            if (l1 == null) {
                l1 = new ListNode(nums1[i]);
                p = l1;
            } else {
                p.next = new ListNode(nums1[i]);
                p = p.next;
            }
        }

        for (int i = 0; i < nums2.length; i++) {
            if (l2 == null) {
                l2 = new ListNode(nums2[i]);
                q = l2;
            } else {
                q.next = new ListNode(nums2[i]);
                q = q.next;
            }
        }

        ListNode t = l1;
        while (t != null) {
            System.out.println("l1: " + t.val);
            t = t.next;
        }

        ListNode t1 = l2;
        while (t1 != null) {
            System.out.println("l2: " + t1.val);
            t1 = t1.next;
        }

        ListNode resultList = addTwoNumners(l1, l2);
        ListNode r = resultList;
        while (r != null) {
            System.out.println("r: " + r.val);
            r = r.next;
        }
    }
}

class ListNode {
    int val;

    ListNode next;

    public ListNode(int val) {
        this.val = val;
    }
}
