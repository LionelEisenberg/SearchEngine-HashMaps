import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public abstract class MapBaseTest {
    private static Random rand = new Random();
    public Map<Integer, LinkedList<String>> map;
    protected abstract Map<Integer, LinkedList<String>> createMap();

    @Before
    public void setupMapTests() {
        map = this.createMap();
    }

    @Test
    public void newIsEmpty() {
        assertEquals(0, map.size());
    }

    @Test
    public void linearInsert() {
        for (Integer i = 0; i < 5; i++) {
            map.insert(i, i);
        }
        assertEquals("0: 0\n1: 1\n2: 2\n3: 3\n4: 4", map.toString());
        assertEquals(5, map.size());
    }

    @Test
    public void insertRandom() {
        map.insert(0, 0);
        map.insert(2, 2);
        map.insert(4, 4);
        map.insert(1, 1);
        map.insert(3, 3);
        assertEquals("0: 0\n1: 1\n2: 2\n3: 3\n4: 4", map.toString());
        assertEquals(5, map.size());
        map = this.createMap();
        map.insert(0, 0);
        map.insert(-2, -2);
        assertEquals("-2: -2\n0: 0", map.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentWhenKeyNullInsert() {
        map.insert(null, rand.nextInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentWhenKeyRepeatedInsert() {
        map.insert(1, rand.nextInt()); //rand --> any value will cause exception
        map.insert(1, rand.nextInt());
    }

    @Test
    public void insertFollowedByRemove() {
        for (int i = 0; i < 10; i++) {
            map.insert(i, i);
            //using object cast to remove ambiguousness
            assertEquals((Object) i, map.remove(i));
            assertEquals(0, map.size());
        }
    }

    @Test
    public void randomRemove() {
        Integer data[] = new Integer[5];
        for (int i = 0; i < 5; i++) {
            data[i] = i;
            map.insert(i, i);
        }
        Collections.shuffle(Arrays.asList(data));
        for (int i = 0; i < 5; i++) {
            Integer a = map.remove(data[i]);
            assertEquals((Object) data[i], a);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentWhenKeyNullRemove() {
        map.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentWhenKeyDNERemove() {
        map.insert(1, 1);
        map.insert(2, 2);
        map.insert(4, 4);
        map.insert(5, 5);
        map.remove(3); //key 3 does not belong in map.
    }

    @Test
    public void getTester() {
        for (Integer i = 0; i < 20; i++) {
            map.insert(i, i);
            assertEquals((Object) i, map.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentWhenKeyNullGet() {
        map.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentWhenKeyDNEGet() {
        map.insert(1, 1);
        map.insert(2, 2);
        map.insert(4, 4);
        map.insert(5, 5);
        map.get(3); //key 3 does not belong in map.
    }

    @Test
    public void jointPutGetTester() {
        for (Integer i = 0; i < 20; i++) {
            map.insert(i, i);
            map.put(i, i+1);
            assertEquals((Object) (i+1), map.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentWhenKeyNullPut() {
        map.put(null, rand.nextInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentWhenKeyDNEPut() {
        map.insert(1, 1);
        map.insert(2, 2);
        map.insert(4, 4);
        map.insert(5, 5);
        map.put(3, 3); //key 3 does not belong in map.
    }

    @Test
    public void hasTester() {
        assertFalse(map.has(rand.nextInt()));
        map.insert(1, 1);
        assertTrue(map.has(1));
        assertFalse(map.has(2));
        map.remove(1);
        assertFalse(map.has(1));
    }

    @Test
    public void sizeTest() {
        for (Integer i = 0; i < 10; i++) {
            map.insert(i, rand.nextInt());
            assertEquals((Object) (i+1) , map.size());
        }
        for (Integer i = 9; i >= 0; i--) {
            map.remove(i);
            assertEquals((Object) (i) , map.size());
        }
    }
}
