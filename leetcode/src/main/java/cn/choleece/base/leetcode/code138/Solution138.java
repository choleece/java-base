package cn.choleece.base.leetcode.code138;

/**
 * A linked list is given such that each node contains an additional random pointer which could point to any node in the list or null.
 *
 * Return a deep copy of the list.
 */
public class Solution138 {

    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }

        if (head.next == null) {
            return new Node(head.val, head.next, head.random);
        }

        Node cur = head.next, tmpHead = new Node(head.val, head.next, head.random), tmpCur = tmpHead;

        while (cur != null) {
            tmpCur.next = new Node(cur.val, cur.next, cur.random);
            tmpCur = tmpCur.next;
            cur = cur.next;
        }

        return tmpHead;
    }

}

class Node {
    public int val;
    public Node next;
    public Node random;

    public Node() {}

    public Node(int _val, Node _next, Node _random) {
        val = _val;
        next = _next;
        random = _random;
    }
}