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

    /** Inserts the key
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

    private Node findNode(K key, Node p) {
        if (key == null) {
            throw new IllegalArgumentException("calls findNode() with a null key");
        }
        if (p == null) {
            return null;
        }


        int cmp = key.compareTo(p.key);
        if (cmp > 0) {
            return findNode(key, p.right);
        } else if (cmp < 0) {
            return findNode(key, p.left);
        }

        return p;
    }
    private Node findRightSubstitute(Node p) {
        if (p == null) {
            return null;
        }
        if (p.left == null) {
            return p;
        }
        return findRightSubstitute(p.left);
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

    private V removeHelper(K key, V value, boolean consistent) {
        Node keyNode = findNode(key, root);
        if (keyNode == null) {
            return null;
        }

        boolean removal = true;
        V r = keyNode.value;
        if (consistent) {
            removal = value.equals(r);
        }
        if (removal) {
            size--;
            Node leftSubstitute = findLeftSubstitute(keyNode.left);
            Node rightSubstitute = findRightSubstitute(keyNode.right);
            if (leftSubstitute != null) {
                keyNode.key = leftSubstitute.key;
                keyNode.value = leftSubstitute.value;
                if (leftSubstitute.left != null) {
                    leftSubstitute.key = leftSubstitute.left.key;
                    leftSubstitute.value = leftSubstitute.left.value;
                    leftSubstitute.left = null;
                } else {
                    if (rightSubstitute.equals(keyNode.left)) {
                        rightSubstitute.parent.left = null;
                    }
                    leftSubstitute.parent.right = null;
                }
                return r;
            } else if (rightSubstitute != null) {
                keyNode.key = rightSubstitute.key;
                keyNode.value = rightSubstitute.value;
                if (rightSubstitute.right != null) {
                    rightSubstitute.key = rightSubstitute.right.key;
                    rightSubstitute.value = rightSubstitute.right.value;
                    rightSubstitute.right = null;
                } else {
                    if (rightSubstitute.equals(keyNode.right)) {
                        rightSubstitute.parent.right = null;
                    }
                    rightSubstitute.parent.left = null;
                }
                return r;
            } else {
                Node keyParent = keyNode.parent;
                if (keyParent == null) {
                    root = null;
                    return r;
                }
                if (keyParent.right.equals(keyNode)) {
                    keyParent.right = null;
                } else {
                    keyParent.left = null;
                }
            }
        } else {
            return null;
        }

        return r;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        return removeHelper(key, null, false);
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        return removeHelper(key, value, true);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
