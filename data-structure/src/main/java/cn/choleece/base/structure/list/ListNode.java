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

    public static ListNode reverseList(ListNode root) {

        ListNode dummyHead = new ListNode(0);
        dummyHead.next = root;
        ListNode p = root;

        while (p.next != null) {

            ListNode tmp = p.next;
            p.next = tmp.next;
            tmp.next = dummyHead.next;

            dummyHead.next = tmp;
        }

        return dummyHead.next;

    }
    public static ListNode segmentReverseList(ListNode root, int k) {

        ListNode dummyHead = new ListNode(0);
        dummyHead.next = root;
        ListNode p = root, tmpDummy = dummyHead;

        int round = 1;
        while (p.next != null) {

            ListNode tmp = p.next;
            p.next = tmp.next;
            tmp.next = tmpDummy.next;

            tmpDummy.next = tmp;

            round++;

            System.out.println("round: " + round);
            if (round % k == 0 && p.next != null) {
                tmpDummy = p;
                p = p.next;
                round = 1;
                System.out.println("P: " + p.val);
            }
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode root = initList(1, Arrays.asList(2, 3));

//        loopList(root);
//
//        ListNode reverseNode = reverseList(root);
//
//        loopList(reverseNode);
//
//        loopList(root);

        ListNode segmentNode = segmentReverseList(root, 2);

        loopList(segmentNode);
    }
}
