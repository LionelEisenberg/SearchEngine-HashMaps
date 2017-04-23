import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
* Ordered maps implemented as (basic) binary search trees.
*
* These BSTs are *not* balanced so all operations (except for size) are
* O(n) in the worst case. Iterators operate on a copy of the keys, so
* changing the tree will not change iterations in progress. (Iterating
* over the tree directly would require a "threaded" representation, a
* much more complicated beast.)
*
* @param <K> Type for keys.
* @param <V> Type for values.
*/
public class TreapMap<K extends Comparable<? super K>, V>
implements OrderedMap<K, V> {

    private static Random rand = new Random();
    // Inner TreapNode class, each holds a key (which is what we sort the
    // BST by) as well as a value. We don't need a parent pointer as
    // long as we use recursive insert/remove helpers.
    private class TreapNode {
        TreapNode left;
        TreapNode right;
        K key;
        V value;
        int priority;

        // Constructor to make TreapNode creation easier to read.
        TreapNode(K k, V v) {
            // left and right default to null
            this.key = k;
            this.value = v;
            this.priority = rand.nextInt();
        }

        // Just for debugging purposes.
        public String toString() {
            return "TreapNode<key: " + this.key
            + "; value: " + this.value
            + ">";
        }
    }

    private TreapNode root;
    private int size;
    private StringBuilder stringBuilder;

    @Override
    public int size() {
        return this.size;
    }

    // Return TreapNode for given key. This one is iterative but a recursive
    // one would also work. It's just that there's no real advantage to
    // using recursion for this operation.
    private TreapNode find(K k) {
        if (k == null) {
            throw new IllegalArgumentException("cannot handle null key");
        }
        TreapNode n = this.root;
        while (n != null) {
            int cmp = k.compareTo(n.key);
            if (cmp < 0) {
                n = n.left;
            } else if (cmp > 0) {
                n = n.right;
            } else {
                return n;
            }
        }
        return null;
    }

    @Override
    public boolean has(K k) {
        if (k == null) {
            return false;
        }
        return this.find(k) != null;
    }

    // Return TreapNode for given key, throw an exception if the key is not
    // in the tree.
    private TreapNode findForSure(K k) {
        TreapNode n = this.find(k);
        if (n == null) {
            throw new IllegalArgumentException("cannot find key " + k);
        }
        return n;
    }

    @Override
    public void put(K k, V v) {
        TreapNode n = this.findForSure(k);
        n.value = v;
    }

    @Override
    public V get(K k) {
        TreapNode n = this.findForSure(k);
        return n.value;
    }

    // Insert given key and value into subtree rooted at given TreapNode;
    // return changed subtree with new TreapNode added. Unlike in find()
    // above, doing this recursively *has* benefits: First we get
    // away with simpler code that doesn't need parent pointers,
    // second the recursive structure makes it easier to add fancy
    // rebalancing code later.
    private TreapNode insert(TreapNode n, K k, V v) {
        if (n == null) {
            return new TreapNode(k, v);
        }

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = this.insert(n.left, k, v);
            if (n.priority > n.left.priority) {
                TreapNode tn = rotateRight(n);
                return tn;
            }
        } else if (cmp > 0) {
            n.right = this.insert(n.right, k, v);
            if (n.priority > n.right.priority) {
                TreapNode tn = rotateLeft(n);
                return tn;
            }
        } else {
            throw new IllegalArgumentException("duplicate key " + k);
        }

        return n;
    }


    private TreapNode rotateRight(TreapNode n) {
        TreapNode m = n.left;
        n.left = m.right;
        m.right = n;

        return m;
    }

    private TreapNode rotateLeft(TreapNode n) {
        TreapNode m = n.right;
        n.right = m.left;
        m.left = n;

        return m;
    }

    @Override
    public void insert(K k, V v) {
        if (k == null) {
            throw new IllegalArgumentException("cannot handle null key");
        }
        this.root = this.insert(this.root, k, v);
        this.size++;
    }

    // Return TreapNode with maximum key in subtree rooted at given TreapNode.
    // (Iterative because once again recursion has no advantage.)
    private TreapNode max(TreapNode n) {
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    // Remove given TreapNode and return the remaining tree. Easy if the TreapNode
    // has 0 or 1 child; if it has two children, find the predecessor,
    // copy its data to the given TreapNode (thus removing the key we need to
    // get rid off), the remove the predecessor TreapNode.
    private TreapNode remove(TreapNode n) {
        // 0 and 1 child
        if (n.left == null) {
            return n.right;
        }
        if (n.right ==  null) {
            return n.left;
        }

        // 2 children
        TreapNode max = this.max(n.left);
        n.key = max.key;
        n.value = max.value;
        n.left = this.remove(n.left, max.key);
        return n;
    }

    // Remove TreapNode with given key from subtree rooted at given TreapNode;
    // return changed subtree with given key missing. (Again doing this
    // recursively makes it easier to add fancy rebalancing code later.)
    private TreapNode remove(TreapNode n, K k) {
        if (n == null) {
            throw new IllegalArgumentException("cannot find key " + k);
        }

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = this.remove(n.left, k);
        } else if (cmp > 0) {
            n.right = this.remove(n.right, k);
        } else if (n.left == null) {
            n = n.right;
        } else if (n.right == null) {
            n = n.left;
        } else if (n.right.priority < n.left.priority) {
            n = rotateRight(n);
            n.right = remove(n.right, k);
        } else {
            n = rotateLeft(n);
            n.left = remove(n.left, k);
        }

        return n;
    }

    @Override
    public V remove(K k) {
        // Need this additional find() for the V return value, because the
        // private remove() method cannot return that in addition to the new
        // root. If we had been smarter and used a void return type, we would
        // not need to do this extra work.
        V v = this.findForSure(k).value;
        this.root = this.remove(this.root, k);
        this.size--;
        return v;
    }

    // Recursively add keys from subtree rooted at given TreapNode into the
    // given list in order.
    private void iteratorHelper(TreapNode n, List<K> keys) {
        if (n == null) {
            return;
        }
        this.iteratorHelper(n.left, keys);
        keys.add(n.key);
        this.iteratorHelper(n.right, keys);
    }

    @Override
    public Iterator<K> iterator() {
        List<K> keys = new ArrayList<K>();
        this.iteratorHelper(this.root, keys);
        return keys.iterator();
    }

    // If we don't have a StringBuilder yet, make one;
    // otherwise just reset it back to a clean slate.
    private void setupStringBuilder() {
        if (this.stringBuilder == null) {
            this.stringBuilder = new StringBuilder();
        } else {
            this.stringBuilder.setLength(0);
        }
    }

    // Recursively append string representations of keys and values from
    // subtree rooted at given TreapNode in order.
    private void toStringHelper(TreapNode n, StringBuilder s) {
        if (n == null) {
            return;
        }
        this.toStringHelper(n.left, s);
        s.append(n.key);
        s.append(": ");
        s.append(n.value);
        s.append(", ");
        this.toStringHelper(n.right, s);
    }

    @Override
    public String toString() {
        this.setupStringBuilder();
        this.stringBuilder.append("{");

        this.toStringHelper(this.root, this.stringBuilder);

        int length = this.stringBuilder.length();
        if (length > 1) {
            // If anything was appended at all, get rid of the last ", "
            // toStringHelper put in; easier to correct this after the
            // fact than to avoid making the mistake in the first place.
            this.stringBuilder.setLength(length - 2);
        }
        this.stringBuilder.append("}");

        return this.stringBuilder.toString();
    }

    public boolean isTreaped(TreapNode n) {
        if (n == null) {
            n = this.root;
        }
        boolean rightIsTreaped = true;
        boolean leftIsTreaped = true;

        if (n.left != null) {
            if (n.left.priority >= n.priority && n.left.key.compareTo(n.key) < 0) {
                leftIsTreaped = isTreaped(n.left);
            } else {
                leftIsTreaped = false;
            }
        }
        if (n.right != null) {
            if (n.right.priority >= n.priority && n.right.key.compareTo(n.key) > 0) {
                rightIsTreaped = isTreaped(n.right);
            } else {
                rightIsTreaped = false;
            }
        }

        return rightIsTreaped && leftIsTreaped;
    }

    public static void main(String[] args) {
        TreapMap<Integer, Integer> m = new TreapMap<>();
        for (int i = 0; i < 100; i++) {
            m.insert(i,i);
        } for (int i = 0; i < 99; i++) {
            m.remove(i);
            System.out.print(m.isTreaped(null));
        }
        System.out.print(m.isTreaped(null));
    }
}
