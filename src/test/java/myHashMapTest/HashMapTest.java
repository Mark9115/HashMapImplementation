package myHashMapTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import myHashMap.HashMapImplementation;


public class HashMapTest {
    private HashMapImplementation<Integer, Integer> hashMapTestIntegerToInteger;

    @BeforeEach
    public void setUp() {
        hashMapTestIntegerToInteger = new HashMapImplementation<>();
        hashMapTestIntegerToInteger.put(9923, 9923);
        hashMapTestIntegerToInteger.put(1, 1);
        hashMapTestIntegerToInteger.put(3, 3);
        hashMapTestIntegerToInteger.put(51, 51);
        hashMapTestIntegerToInteger.put(4, 4);
        hashMapTestIntegerToInteger.put(5, 5);
        hashMapTestIntegerToInteger.put(333, 333);
        hashMapTestIntegerToInteger.put(2, 2);
        hashMapTestIntegerToInteger.put(55, 55);
        hashMapTestIntegerToInteger.put(1234, 1234);
    }

    @Test
    public void testSize() {
        Assertions.assertEquals(10, hashMapTestIntegerToInteger.size());
    }

    @Test
    public void testClear() {
        hashMapTestIntegerToInteger.clear();
        Assertions.assertEquals(0, hashMapTestIntegerToInteger.size());
    }

    @Test
    public void testRemove() {
        hashMapTestIntegerToInteger.remove(1);
        Assertions.assertFalse(hashMapTestIntegerToInteger.containsKey(1), "Can't remove the element!");
    }

    @Test
    public void testIsEmpty() {
        hashMapTestIntegerToInteger.clear();
        Assertions.assertTrue(hashMapTestIntegerToInteger.isEmpty(), "Isn't empty");
    }

    @Test
    public void testContainsValue() {
        Assertions.assertTrue(hashMapTestIntegerToInteger.containsValue(55),"Doesn't contain the correct value");
    }

    @Test
    public void testContainsKey() {
        Assertions.assertTrue(hashMapTestIntegerToInteger.containsKey(1), "Doesn't contain the correct value");
    }

    @Test
    public void testGet() {
        Assertions.assertEquals(Integer.valueOf(1), hashMapTestIntegerToInteger.get(1));
    }

    @Test
    public void testPut() {
        hashMapTestIntegerToInteger.put(99,99);
        Assertions.assertEquals(Integer.valueOf(99), hashMapTestIntegerToInteger.get(99));
        int size = hashMapTestIntegerToInteger.size();

        hashMapTestIntegerToInteger.put(99,99);
        hashMapTestIntegerToInteger.put(99,99);

        Assertions.assertEquals(size,hashMapTestIntegerToInteger.size());
    }

    @Test
    public void testSort() {
        Object[] arrKeys = hashMapTestIntegerToInteger.sort();

        for (int i = 1; i < hashMapTestIntegerToInteger.size(); i++){
            //System.out.println(arrKeys[i] + "=" + hashMapTestIntegerToInteger.get(arrKeys[i]));
            Assertions.assertTrue((int)arrKeys[i-1] <= (int)arrKeys[i]);
        }
    }
}