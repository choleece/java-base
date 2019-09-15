package cn.choleece.base.structure.list;

/**
 * @author choleece
 * @Description: 利用LinkList实现一个LRU算法
 * @Date 2019-09-15 14:50
 **/
public class LruList {

    final static Integer CACHE_SIZE = 10;

    private volatile int size = 0;

    private ListNode cache = new ListNode(-1);

    public void getCache(int val) {
        ListNode preCur = cache, cur = preCur.next, prePreCur = preCur;

        while (cur != null) {
            if (cur.val == val) {
                preCur.next = cur.next;
                cur.next = cache.next;
                cache.next = cur;
                break;
            } else {
                cur = cur.next;
                prePreCur = preCur;
                preCur = preCur.next;
            }
        }

        if (cur == null) {
            if (size < CACHE_SIZE) {
                preCur.next = new ListNode(val);
                size++;
            } else {
                ListNode t = new ListNode(val);
                t.next = cache.next;
                cache.next = t;
                prePreCur.next = null;
            }
        }
    }

    public static void main(String[] args) {
        LruList cache = new LruList();

        for (int i = 0; i < 11; i++) {
            cache.getCache(i);
        }

        cache.getCache(0);
        ListNode.loopList(cache.cache);
    }

}
