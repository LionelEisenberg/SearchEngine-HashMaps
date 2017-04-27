/**
 * Lionel Eisenberg & Sanat Deshpande
 * leisenb5 & sdeshpa4
 * Data Structures – Assignment 9
 */

/**
 * HashMap implementation v0 NOT CORRECT VERSION.
 */

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Array;


public class HashMapV0<K, V> implements Map<K, V> {

    /*public static void main (String[] args) {
        HashMap map = new HashMap();
        System.out.println(map.size());
        map.insert("USA", "English");
        map.insert("France", "French");
        map.insert("India", "Hindi");
        map.insert("India", "Marathi");
        System.out.println(map);
    }*/

    private LinkedList<V>[] chain;
    private int size;  //refers to number of key-value pairs

    /**
     * Initializes array with default size.
     */

    public HashMapV0 () {
        this.size = 0;
        this.chain = (LinkedList<V>[]) Array.newInstance(LinkedList.class, 10000); //default
    }

    /**
     * Initializes array with specified size.
     */

    public HashMapV0 (int s) {
        this.size = 0;
        this.chain = (LinkedList<V>[]) Array.newInstance(LinkedList.class, s); //user specified size
    }

    private int hashFunction(K k) {
        return k.hashCode(); //returns hashcode
    }

    private LinkedList<V> find (K k) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = this.hashFunction(k);
        int mod = (hashCode % this.chain.length + this.chain.length)
            % this.chain.length;
        LinkedList<V> values = this.chain[mod];
        return values;
    }

    /**
     * Insert a new key/value pair.
     *
     * @param k The key.
     * @param v The value to be associated with k.
     * @throws IllegalArgumentException If k is null.
     */

    public void insert(K k, V v) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = this.hashFunction(k);
        int mod = (hashCode % this.chain.length + this.chain.length)
            % this.chain.length;
        LinkedList<V> target = this.chain[mod];
        if (target == null) {
            this.chain[mod] = new LinkedList<V>();
            target = this.chain[mod];
        }
        target.add(v);
        this.size++;
    }

    /**
     * Remove an existing key/value pair.
     *
     * @param k The key.
     * @return The value that was associated with k.
     * @throws IllegalArgumentException If k is null or not mapped.
     */

    public String remove(K k) throws IllegalArgumentException {
        LinkedList<V> values = this.find(k);
        if (values == null) {
            return "";
        }
        String output = "";
        for (V s : values) {
            output += s;
            output += " ";
        }
        int mod = (this.hashFunction(k) % this.chain.length + this.chain.length)
            % this.chain.length;
        this.chain[mod] = null; //removes it from map
        this.size--;
        return output;
    }

    /**
     * Update the value associated with a key.
     *
     * @param k The key.
     * @param v The value to be associated with k.
     * @throws IllegalArgumentException If k is null or not mapped.
     */

    public void put(K k, V v) throws IllegalArgumentException {
        LinkedList<V> values = this.find(k);
        if (values == null) {
            throw new IllegalArgumentException();
        }
        values.clear();
        values.add(v);
    }

    /**
     * Get the value associated with a key.
     *
     * @param k The key.
     * @return The value associated with k.
     * @throws IllegalArgumentException If k is null or not mapped.
     */

    public String get(K k) throws IllegalArgumentException {
        LinkedList<V> values = this.find(k);
        String output = "";
        if (values == null) {
            return "";
        }
        for (V v : values) {
            output += v;
            output += " ";
        }
        return output;
    }

    /**
     * Check existence of a key.
     *
     * @param k The key.
     * @return True if k is mapped, false otherwise (even for null!).
     */

    public boolean has(K k) {
        try {
            LinkedList<K> values = this.find(k); //throws error if no match
            if (values == null) {
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Number of mappings.
     *
     * @return Number of key/value pairs in the map.
     */

    public int size() {
        return this.size;
    }


    /**
     * Returns an iterator.
     * @return iterator for class
     */

    @Override
    public Iterator<K> iterator() {
        ArrayList<K> keys = new ArrayList<>();
        for (int i = 0; i < this.chain.length; i++) {
            if (this.chain[i] != null) {
                keys.add(Integer.toString(i));
            }
        }
        return keys.iterator();
    }

    /**
     * String representation of HashMap
     * @return s string of hashedkey: value
     */

     public String toString() {
         String out = "";
         for (int i = 0; i < this.chain.length; i++) {
             if (this.chain[i] != null) {
                 out += i + ": " + this.chain[i] + "\n";
             }
         }
         return out;
     }

 }
