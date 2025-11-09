import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A simple Binary-Search-Tree based Map.
 * Keys must be Comparable.
 */
public class MyTreeMap<K extends Comparable<K>, V> implements Iterable<V> {

    private class Node {
        K key;
        V value;
        Node left, right, parent;

        Node(K k, V v, Node p) {
            key = k;
            value = v;
            parent = p;
        }
    }

    private Node root;
    private int size;

    /* -------------------- public API -------------------- */

    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("null key");
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }
        Node parent = null;
        Node cur = root;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);
            if (cmp == 0) {                 // key already exists → replace
                V old = cur.value;
                cur.value = value;
                return old;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        // insert new leaf
        Node newNode = new Node(key, value, parent);
        if (cmp < 0) parent.left = newNode;
        else        parent.right = newNode;
        size++;
        return null;
    }

    public V get(K key) {
        Node n = findNode(key);
        return (n == null) ? null : n.value;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Remove the entry with the given key and return its value.
     *  Returns null if the key was not present. 
     *  Test 3 cases 
     *  Case 1: Leaf
     *  Case 2: one right child
     *  Case 2: one left child
     *  Case 3: two children
     */
    public V remove(K key) {
        Node z = findNode(key);
        if (z == null) {
            return null; // Key not found, nothing to remove.
        }

        V removedValue = z.value;

        if (z.left == null) {
            // Case 1 (leaf) & Case 2 (only right child)
            transplant(z, z.right);
        } else if (z.right == null) {
            // Case 2 (only left child)
            transplant(z, z.left);
        } else {
            // Case 3 (two children)
            Node y = minimum(z.right); // Find successor
            if (y.parent != z) {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
        }

        size--;
        return removedValue;
    }

    /* -------------------- iterator (in-order) -------------------- */

    @Override
    public Iterator<V> iterator() {
        return new InOrderIterator();
    }

    private class InOrderIterator implements Iterator<V> {
        private Node next = (root == null) ? null : minimum(root);

        @Override
        public boolean hasNext() { return next != null; }

        @Override
        public V next() {
            if (next == null) throw new NoSuchElementException();
            V value = next.value;
            next = successor(next);
            return value;
        }
    }

    /* -------------------- private helpers -------------------- */

    private Node findNode(K key) {
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur;
            else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        return null;
    }

    /** Replace subtree rooted at u with subtree rooted at v (v may be null). */
    private void transplant(Node u, Node v) {
        if (u.parent == null) {               // u is root
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) v.parent = u.parent;
    }

    private Node minimum(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node successor(Node n) {
        if (n.right != null) return minimum(n.right);
        Node p = n.parent;
        while (p != null && n == p.right) {
            n = p;
            p = p.parent;
        }
        return p;
    }
}