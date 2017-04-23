import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

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
public class AvlTreeMap<K extends Comparable<? super K>, V>
implements OrderedMap<K, V> {
    public static final int UNBALANCE = 2; //Maximum difference between subtrees

    // Inner AvlNode class, each holds a key (which is what we sort the
    // BST by) as well as a value. We don't need a parent pointer as
    // long as we use recursive insert/remove helpers.
    private class AvlNode {
        AvlNode left;
        AvlNode right;
        K key;
        V value;
        int height;

        // Constructor to make AvlNode creation easier to read.
        AvlNode(K k, V v) {
            // left and right default to null
            this.key = k;
            this.value = v;
            this.height = 0;
        }

        // Just for debugging purposes.
        public String toString() {
            return "AvlNode<key: " + this.key
            + "; value: " + this.value
            + ">";
        }
    }

    private AvlNode root;
    private int size;
    private StringBuilder stringBuilder;


    @Override
    public int size() {
        return this.size;
    }

    // Return AvlNode for given key. This one is iterative but a recursive
    // one would also work. It's just that there's no real advantage to
    // using recursion for this operation.
    private AvlNode find(K k) {
        if (k == null) {
            throw new IllegalArgumentException("cannot handle null key");
        }
        AvlNode n = this.root;
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

    // Return AvlNode for given key, throw an exception if the key is not
    // in the tree.
    private AvlNode findForSure(K k) {
        AvlNode n = this.find(k);
        if (n == null) {
            throw new IllegalArgumentException("cannot find key " + k);
        }
        return n;
    }

    @Override
    public void put(K k, V v) {
        AvlNode n = this.findForSure(k);
        n.value = v;
    }

    @Override
    public V get(K k) {
        AvlNode n = this.findForSure(k);
        return n.value;
    }

    private int height(AvlNode n) {
        if (n == null) {
            return -1;
        } else {
            return n.height;
        }
    }

    // Insert given key and value into subtree rooted at given AvlNode;
    // return changed subtree with new AvlNode added. Unlike in find()
    // above, doing this recursively *has* benefits: First we get
    // away with simpler code that doesn't need parent pointers,
    // second the recursive structure makes it easier to add fancy
    // rebalancing code later.
    private AvlNode insert(AvlNode n, K k, V v) {
        if (n == null) {
            return new AvlNode(k, v);
        }

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = this.insert(n.left, k, v);

            int balance = this.height(n.left) - this.height(n.right);
            if (balance == UNBALANCE && k.compareTo(n.left.key) < 0) {
                //do code for right rotation here.
                n = rotateRight(n);
            } else if (balance == UNBALANCE && k.compareTo(n.left.key) >= 0) {
                //do code for right then left rotation
                n.left = rotateLeft(n.left);
                n = rotateRight(n);
            }
        } else if (cmp > 0) {
            n.right = this.insert(n.right, k, v);

            int balance = this.height(n.right) - this.height(n.left);
            if (balance == UNBALANCE && k.compareTo(n.right.key) > 0) {
                //do code for left rotation here
                n = rotateLeft(n);
            } else if (balance == UNBALANCE && k.compareTo(n.right.key) <= 0) {
                n.right = rotateRight(n.right);
                n = rotateLeft(n);
            }
        } else {
            throw new IllegalArgumentException("duplicate key " + k);
        }

        n.height = max(this.height(n.left), this.height(n.right)) + 1;
        return n;
    }

    @Override
    public void insert(K k, V v) {
        if (k == null) {
            throw new IllegalArgumentException("cannot handle null key");
        }
        this.root = this.insert(this.root, k, v);
        this.size++;
    }

    // Return AvlNode with maximum key in subtree rooted at given AvlNode.
    // (Iterative because once again recursion has no advantage.)
    private AvlNode max(AvlNode n) {
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    private int max(int a, int b) {
        if (a >= b) {
            return a;
        } else {
            return b;
        }
    }

    private AvlNode rotateRight(AvlNode n) {
        AvlNode m = n.left;
        n.left = m.right;
        m.right = n;


        n.height = max(this.height(n.left), this.height(n.right));
        m.height = max(this.height(m.left), this.height(m.right));
        return m;
    }

    private AvlNode rotateLeft(AvlNode n) {
        AvlNode m = n.right;
        n.right = m.left;
        m.left = n;

        n.height = max(this.height(n.left), this.height(n.right)) + 1;
        m.height = max(this.height(m.left), this.height(m.right)) + 1;
        return m;
    }

    // Remove given AvlNode and return the remaining tree. Easy if the AvlNode
    // has 0 or 1 child; if it has two children, find the predecessor,
    // copy its data to the given AvlNode (thus removing the key we need to
    // get rid off), the remove the predecessor AvlNode.
    private AvlNode remove(AvlNode n) {
        // 0 and 1 child
        if (n.left == null) {
            return n.right;
        }
        if (n.right ==  null) {
            return n.left;
        }

        // 2 children
        AvlNode max = this.max(n.left);
        n.key = max.key;
        n.value = max.value;
        n.left = this.remove(n.left, max.key);
        return n;
    }

    // Remove AvlNode with given key from subtree rooted at given AvlNode;
    // return changed subtree with given key missing. (Again doing this
    // recursively makes it easier to add fancy rebalancing code later.)
    private AvlNode remove(AvlNode n, K k) {
        if (n == null) {
            throw new IllegalArgumentException("cannot find key " + k);
        }

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = this.remove(n.left, k);
        } else if (cmp > 0) {
            n.right = this.remove(n.right, k);
        } else {
            n = this.remove(n);
        }

        if (n == null) {
            return n;
        }

        if (this.height(n.left) - this.height(n.right) >= UNBALANCE) {
            if (this.height(n.left.left) - this.height(n.left.right) >= 0) {
                n = rotateRight(n);
            } else {
                n.left = rotateLeft(n.left);
                n = rotateRight(n);
            }
        } else if ((this.height(n.right) - this.height(n.left) >= UNBALANCE)) {
            if (this.height(n.right.right) - this.height(n.right.left) >= 0) {
                n = rotateLeft(n);
            } else {
                n.right = rotateRight(n.right);
                n = rotateLeft(n);
            }
        }

        n.height = max(this.height(n.left), this.height(n.right)) + 1;
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

    // Recursively add keys from subtree rooted at given AvlNode into the
    // given list in order.
    private void iteratorHelper(AvlNode n, List<K> keys) {
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
    // subtree rooted at given AvlNode in order.
    private void toStringHelper(AvlNode n, StringBuilder s) {
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

    public boolean isBalanced(AvlNode n) {
        if (n == null) {
            n == this.root;
        }
        boolean rightIsBalanced = true;
        boolean leftIsBalanced = true;
        int lh = 0;
        int rh = 0;

        if (n.left != null) {
            leftIsBalanced = isBalanced(n.left);
            lh = height(n.left);
        }

        if (n.right != null) {
            rightIsBalanced = isBalanced(n.right);
            rh = height(n.right);
        }

        return Math.abs(lh - rh) < 2 && leftIsBalanced && rightIsBalanced;
    }

    /*public static void main(String[] args) {
        AvlTreeMap<Integer, Integer> m = new AvlTreeMap<>();
        m.insert(6, 5);
        m.insert(2, 5);
        m.insert(9, 5);
        m.insert(1, 2);
        m.insert(4, 3);
        m.insert(8, 5);
        m.insert(11, 5);
        m.insert(3, 2);
        m.insert(5, 3);
        m.insert(7, 5);
        m.insert(10, 5);
        m.insert(12, 2);
        for (Integer i = 1; i < 13; i++) {
            m.remove(i);
            System.out.print(m.isBalanced(m.root) + "\n");
            m.insert(i, 0);
        }
        System.out.print(m.isBalanced(m.root) + "\n");
        System.out.print(m.toString() + "\n");
    }*/
}
