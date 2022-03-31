package myHashMap;

import java.util.*;

/**
 * My HashMap implementation.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class HashMapImplementation<K extends Comparable<K>, V extends Comparable<V>> implements HashMapInterface<K, V> {
    /**
     * The default initial capacity
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    /**
     * The maximum capacity
     */
    static final int MAXIMUM_CAPACITY = 100000;
    /**
     * The default load factor
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * main constructor
     */
    public HashMapImplementation() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    /**
     * Main structural unit
     */
    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        HashMapImplementation.Node<K, V> next;

        Node(int hash, K key, V value, HashMapImplementation.Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Represents buckets
     */
    HashMapImplementation.Node<K, V>[] table;

    final float loadFactor;
    int size;
    int modCount;
    int threshold;

    /**
     * hash function from documentation
     */
    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * resizes hashmap
     * @return - new bucket table
     */
    HashMapImplementation.Node<K, V>[] resize() {

        HashMapImplementation.Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1;
        }else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;

        @SuppressWarnings("unchecked")
        HashMapImplementation.Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];

        table = newTab;
        /*
        Copying the old table to new one from original hashmap source
         */
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                HashMapImplementation.Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else {
                        HashMapImplementation.Node<K, V> loHead = null, loTail = null;
                        HashMapImplementation.Node<K, V> hiHead = null, hiTail = null;
                        HashMapImplementation.Node<K, V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    /**
     * creating the new Node
     * @return - new node
     */
    HashMapImplementation.Node<K, V> newNode(int hash, K key, V value) {
        return new HashMapImplementation.Node<>(hash, key, value, null);
    }

    /**
     * Puts the node to the hashmap
     */
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    /**
     * Checking hash to insert the node
     */
    final void putVal(int hash, K key, V value) {
        HashMapImplementation.Node<K, V>[] tab;
        HashMapImplementation.Node<K, V> p;
        int n, i;

        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null) {
            ++size;
            tab[i] = newNode(hash, key, value);
        } else {
            HashMapImplementation.Node<K, V> e;
            K k;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k)))) {
                return;
            } else {
                for (; ; ) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value);
                        ++size;
                        break;
                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
        }
        ++modCount;
        if (size > threshold)
            resize();
    }

    /**
     * Returns the right value
     * @return - V
     */
    public V get(Object key) {
        HashMapImplementation.Node<K, V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    /**
     * Checking whether the node exists or in the linkedList sequence
     * @return - the node
     */
    final HashMapImplementation.Node<K, V> getNode(Object key) {
        HashMapImplementation.Node<K, V>[] tab;
        HashMapImplementation.Node<K, V> first, e;
        int n, hash;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & (hash = hash(key))]) != null) {
            if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    /**
     * Remove the right Object
     * @return - V
     */
    public V remove(Object key) {
        HashMapImplementation.Node<K, V> e;
        return (e = removeNode(hash(key), key)) == null ?
                null : e.value;
    }
    /**
     * Checking whether the node exists or in the linkedList sequence to remove
     * @return - the node
     */
    HashMapImplementation.Node<K, V> removeNode(int hash, Object key) {
        HashMapImplementation.Node<K, V>[] tab;
        HashMapImplementation.Node<K, V> p;
        int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {
            HashMapImplementation.Node<K, V> node = null, e;
            K k;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key ||
                                    (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
            if (node != null) {
                if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
                --size;
                return node;
            }
        }
        return null;
    }

    /**
     * Inserts null to all nodes
     */
    public void clear() {
        HashMapImplementation.Node<K, V>[] tab;
        modCount++;
        if ((tab = table) != null && size > 0) {
            size = 0;
            Arrays.fill(tab, null);
        }
    }

    /**
     * returns size.
     * @return - the total number of elements
     */
    public final int size() {
        return size;
    }

    /**
     * Indicates map emptiness
     * @return - false/true
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Shows whether we have the key or not
     * @return - true/false
     */
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    /**
     * Shows whether we have the value or not
     * @return - true/false
     */
    public boolean containsValue(Object value) {
        HashMapImplementation.Node<K, V>[] tab;
        V v;
        if ((tab = table) != null && size > 0) {
            for (HashMapImplementation.Node<K, V> e : tab) {
                for (; e != null; e = e.next) {
                    if ((v = e.value) == value ||
                            (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * public method which helps to divide keys and sorting
     */
    public Object[] sort() {
        Object[] arrayKey = new Object[size];
        int i = 0;
        for (HashMapImplementation.Node<K, V> e : table) {
            for (; e != null; e = e.next) {
                arrayKey[i++] = e.key;
            }
        }

        quickSort(arrayKey, 0, arrayKey.length - 1);

        return arrayKey;

    }

    /**
     * quickSort sorting method
     */
    private void quickSort(Object[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    /**
     * division into pieces
     */
    @SuppressWarnings("unchecked")
    private int partition(Object[] arr, int begin, int end) {
        K pivot = (K) arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (((K) arr[j]).compareTo(pivot) < 0) {
                i++;

                K swapTemp = (K) arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        K swapTemp = (K) arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    /**
     * Represents Object as a String
     * @return - complete String value
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (HashMapImplementation.Node<K, V> e : table) {
            for (; e != null; e = e.next) {
                result.append(e.key).append(" = ").append(e.value).append("\n");
            }
        }
        return String.valueOf(result);
    }
}

/**
 * testing
 */
class Driver {
    public static void main(String[] args) {
        HashMapImplementation<Integer, Integer> hashMapTestIntegerToInteger = new HashMapImplementation<>();
        hashMapTestIntegerToInteger.put(1, 11);
        hashMapTestIntegerToInteger.clear();
    }
}

