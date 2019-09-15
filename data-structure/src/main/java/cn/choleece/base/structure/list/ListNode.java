package cn.choleece.base.structure.list;

import java.util.Arrays;
import java.util.List;

/**
 * LinkList Node
 * @author choleece
 */
public class ListNode {
    public int val;
    public ListNode next;
    public ListNode(int x) {
        val = x;
        next = null;
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

    public static ListNode initList(int rootVal, List<Integer> nums) {
        ListNode head = new ListNode(rootVal);
        ListNode p = head;
        for (int num : nums) {
            ListNode tmp = new ListNode(num);
            p.next = tmp;
            p = tmp;
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode root = initList(0, Arrays.asList(1, 2, 3, 4, 5, 6, 7));

        loopList(root);
    }
}
