package cn.choleece.base.leetcode.code725;

import cn.choleece.base.leetcode.ListNode;

import java.util.Arrays;

/**
 * Split Linked List in Parts
 * @author choleece
 */
public class Solution725 {

    public static ListNode[] splitListToParts1(ListNode root, int k) {
        ListNode p = root;

        ListNode[] list = new ListNode[k];
        int i = 0;
        while (p != null) {

            ListNode tmpNode = list[i];
            if (tmpNode != null) {
                while (tmpNode.next != null) {
                    tmpNode = tmpNode.next;
                }
                tmpNode.next = new ListNode(p.val);
            } else {
                list[i] = new ListNode(p.val);
            }

            p = p.next;
            i = i == k - 1 ? 0 : i + 1;
        }
        return list;
    }

    public static ListNode[] splitListToParts(ListNode root, int k) {
        int l = 0;
        ListNode p = root;

        ListNode[] list = new ListNode[k];
        while (p != null) {
            l++;
            p = p.next;
        }

        int i = 0, count = 0;
        int yu = l % k;
        int shang = l / k;
        ListNode q = root;
        while (q != null) {
            ListNode tmp = list[i];
            if (tmp != null) {
                while (tmp.next != null) {
                    tmp = tmp.next;
                }
                tmp.next = new ListNode(q.val);
            } else {
                list[i] = new ListNode(q.val);
            }

            q = q.next;
            count++;

            if (l / k == 0) {
                i++;
            } else {
                if (count < yu * (shang + 1)) {
                    i = count / (shang + 1);
                } else {
                    i = (count - (yu * (shang + 1))) / shang + yu;
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {

        ListNode head = ListNode.initList(Arrays.asList(1, 2, 3, 4), 0);

        ListNode[] results = splitListToParts(head, 3);

        for (int i = 0; i < results.length; i++) {
            ListNode.loopList(results[i]);
        }

    }

}
