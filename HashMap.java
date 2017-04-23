/**
 * Lionel Eisenberg & Sanat Deshpande
 * leisenb5 & sdeshpa4
 * Data Structures – Assignment 9
 */

/**
 * HashMap implementation.
 */

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Array;

public class HashMap implements Map<String, String> {

    public static void main (String[] args) {
        HashMap map = new HashMap();
        System.out.println(map.size());
        map.insert("USA", "English");
        map.insert("France", "French");
        map.insert("India", "Hindi");
        map.insert("India", "Marathi");
        System.out.println(map);
    }

    private LinkedList<String>[] chain;
    private int size;  //refers to number of key-value pairs

    /**
     * Initializes array with default size.
     */

    public HashMap () {
        this.size = 0;
        this.chain = (LinkedList<String>[]) Array.newInstance(LinkedList.class, 10000); //default
    }

    /**
     * Initializes array with specified size.
     */

    public HashMap (int s) {
        this.size = 0;
        this.chain = (LinkedList<String>[]) Array.newInstance(LinkedList.class, s); //user specified size
    }

    private int hashFunction(String k) {
        String key = k; //casts generic type to string
        return key.hashCode(); //returns hashcode
    }

    private LinkedList<String> find (String k) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = this.hashFunction(k);
        LinkedList<String> values = this.chain[hashCode % this.chain.length];
        return values;
    }

    /**
     * Insert a new key/value pair.
     *
     * @param k The key.
     * @param v The value to be associated with k.
     * @throws IllegalArgumentException If k is null.
     */

    public void insert(String k, String v) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = hashFunction(k);
        LinkedList<String> target = this.chain[hashCode % this.chain.length];
        if (target == null) {
            this.chain[hashCode % this.chain.length] = new LinkedList<String>();
            target = this.chain[hashCode % this.chain.length];
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

    public String remove(String k) throws IllegalArgumentException {
        LinkedList<String> values = this.find(k);
        if (values == null) {
            return "";
        }
        String output = "";
        for (String s : values) {
            output += s;
            output += " ";
        }
        this.chain[this.hashFunction(k) % this.chain.length] = null; //removes it from map
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

    public void put(String k, String v) throws IllegalArgumentException {
        LinkedList<String> values = this.find(k);
        if (values == null) {
            throw new IllegalArgumentException();
        }
        values.clear();
        String[] toMap = v.split(" ");
        for (int i = 0; i < toMap.length; i++) {
            values.add(toMap[i]);
        }
    }

    /**
     * Get the value associated with a key.
     *
     * @param k The key.
     * @return The value associated with k.
     * @throws IllegalArgumentException If k is null or not mapped.
     */

    public String get(String k) throws IllegalArgumentException {
        LinkedList<String> values = find(k);
        String output = "";
        if (values == null) {
            return "";
        }
        for (String v : values) {
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

    public boolean has(String k) {
        try {
            LinkedList<String> values = this.find(k); //throws error if no match
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
    public Iterator<String> iterator() {
        ArrayList<String> keys = new ArrayList<>();
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
