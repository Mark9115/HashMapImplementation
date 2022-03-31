package myHashMap;

/**
 * Interface which declares methods
 */
public interface HashMapInterface<K, V> {
    void put(K key, V value);

    V get(Object key);

    V remove(Object key);

    void clear();

    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);
}
