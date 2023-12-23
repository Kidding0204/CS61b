package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author ASAKA
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;
        private Node parent;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (p == null) {
            return null;
        }

        int cmp = key.compareTo(p.key);
        if (cmp > 0) {
            return getHelper(key, p.right);
        } else if (cmp < 0) {
            return getHelper(key, p.left);
        }

        return p.value;
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        if (p == null) {
            return new Node(key, value);
        }

        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left =  putHelper(key, value, p.left);
            p.left.parent = p;
        } else if (cmp > 0) {
            p.right =  putHelper(key, value, p.right);
            p.right.parent = p;
        } else {
            p.value = value;
        }

        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (get(key) == null) {
            size++;
        }
        if (root == null) {
            root = new Node(key, value);
        }

        putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    private void getKeys(Set<K> keySet, Node p) {
        if (p == null) {
            return;
        }
        keySet.add(p.key);
        getKeys(keySet, p.left);
        getKeys(keySet, p.right);
    }
    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> r = new HashSet<>();
        getKeys(r, root);
        return r;
    }

    private Node findLeftSubstitute(Node p) {
        if (p == null) {
            return null;
        }
        if (p.right == null) {
            return p;
        }
        return findLeftSubstitute(p.right);
    }

    private Node removeHelper(K key, Node p) {
        if (p == null) {
            return null;
        }

        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = removeHelper(key, p.left);
        } else if (cmp > 0) {
            p.right = removeHelper(key, p.right);
        } else {
            // Node to be removed found
            if (p.left == null) {
                return p.right; // If only left child or no child
            } else if (p.right == null) {
                return p.left; // If only right child
            } else {
                // Node to be removed has both left and right children
                Node successor = findLeftSubstitute(p.right); // or findRightSubstitute(p.left)
                p.key = successor.key;
                p.value = successor.value;
                p.right = removeHelper(successor.key, p.right);
            }
        }

        return p;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        Node removedNode = removeHelper(key, root);
        if (removedNode != null) {
            size--;
            root = removedNode;
            return removedNode.value;
        }
        return null;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        Node removedNode = removeHelper(key, root);
        if (removedNode != null) {
            if (removedNode.value.equals(value)) {
                return null;
            }
            size--;
            root = removedNode;
            return removedNode.value;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
