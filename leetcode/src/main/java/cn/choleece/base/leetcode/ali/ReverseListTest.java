package cn.choleece.base.leetcode.ali;

/**
 * @author choleece
 * @Description: 单向链表，逆向输出
 * @Date 2019-10-21 21:15
 **/
public class ReverseListTest {

    static class ListNode {
        private int val;

        private ListNode next;

        public ListNode(int val) {
            this(val, null);
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static ListNode reverseList(ListNode root) {
        if (root == null || root.next == null) {
            return root;
        }

        ListNode dummyHead = new ListNode(0, root);

        ListNode p = root;

        while (p.next != null) {

            ListNode tmp = p.next;
            p.next = tmp.next;
            tmp.next = dummyHead.next;

            dummyHead.next = tmp;
        }

        return dummyHead.next;
    }

    public static void printList(ListNode root) {
        ListNode p = root;

        while (p != null) {
            System.out.print(p.val + "---->");
            p = p.next;
        }

        System.out.println("end");
    }

    public static void printReverseList(ListNode root) {

        printList(root);

        ListNode reverseRoot = reverseList(root);

        printList(reverseRoot);
    }

    public static void main(String[] args) {
        ListNode root = new ListNode(0, new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4)))));

        printReverseList(root);
    }
}
