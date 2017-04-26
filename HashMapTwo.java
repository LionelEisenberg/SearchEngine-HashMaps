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


public class HashMapTwo<K, V> implements Map<K, V> {
    private static final double LOADFACTOR = 0.5;
    private static final int DEFAULTSIZE = 131;

    public static void main (String[] args) {
        HashMapTwo<String, ArrayList<String>> map = new HashMapTwo<>(3);
        //System.out.println(map.size());
        ArrayList<String> list = new ArrayList<>();
        list.add("English");
        map.insert("USA", list);
        list = new ArrayList<>();
        list.add("French");
        map.insert("France", list);
        list = new ArrayList<>();
        list.add("Hindi");
        list.add("Marathi");
        map.insert("India", list);

        System.out.println(map.get("India"));
    }

    private class Node {
        K key;
        V value;
        boolean placeholder;
        public Node(K k, V v) {
            key = k;
            value = v;
            placeholder = false;
        }

        public String toString() {
            return key + ": " + value;
        }
    }

    private Node[] chain;
    private int size;  //refers to number of key-value pairs

    /**
     * Initializes array with default size.
     */

    public HashMapTwo () {
        this.size = 0;
        this.chain = (Node[]) Array.newInstance(Node.class, DEFAULTSIZE); //default
    }

    /**
     * Initializes array with specified size.
     */

    public HashMapTwo (int s) {
        this.size = 0;
        this.chain = (Node[]) Array.newInstance(Node.class, s); //user specified size
    }

    private int hashFunction(K k) {
        K key = k; //casts generic type to string
        return key.hashCode(); //returns hashcode
    }

    private Node find (K k) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = this.hashFunction(k);
        int mod = (hashCode % this.chain.length + this.chain.length) % this.chain.length;
        int probeCount = 1; //keeps track of how far to probe
        int index = 0; //keeps track of how many elements are searched
        while (this.chain[mod] != null && index < this.chain.length) {
            index++;
            if (this.chain[mod].placeholder == true) { //skips over tombstone
                continue;
            }
            if (this.chain[mod].key.equals(k)) { //checks for key
                return this.chain[mod];
            }
            mod = (mod + this.probe(probeCount)) % this.chain.length; //moves on to next slot
            probeCount++; // increment probing for next pass
        }
        return null;
    }

    private int probe(int n) {
        return n*n; // probes quadratically
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
        if (LOADFACTOR * this.chain.length <= this.size) {
            this.resize();
        }
        int hashCode = this.hashFunction(k);
        int mod = (hashCode % this.chain.length + this.chain.length)
                % this.chain.length;
        int probeCount = 1; //keeps track of how far to probe
        while (this.chain[mod] != null) {
            if (this.chain[mod].placeholder == true) {
                this.chain[mod] = new Node(k, v);//why no increase size here?
                this.size++;
                return;
            }
            mod = (mod + this.probe(probeCount)) % this.chain.length; //moves on to next slot
            probeCount++;
        }
        this.chain[mod] = new Node(k, v);
        this.size++;
    }

    private void resize() {
        int nextSize = this.size*2;
        Node[] temp = (Node[]) Array.newInstance(Node.class, this.chain.length); //default
        for (int i = 0; i < this.chain.length; i++) {
            if (this.chain[i] != null && this.chain[i].placeholder == false) {
                temp[i] = new Node(this.chain[i].key, this.chain[i].value);
            }
        }
        this.chain = (Node[]) Array.newInstance(Node.class, nextSize);
        this.size = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != null) {
                this.insert(temp[i].key, temp[i].value);
            }
        }
    }

    /**
     * Remove an existing key/value pair.
     *
     * @param k The key.
     * @return The value that was associated with k.
     * @throws IllegalArgumentException If k is null or not mapped.
     */

    public V remove(K k) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        Node n = this.find(k); //gets node to remove
        if (n == null) {
            return null; // returns null if node is null
        } else {
            n.placeholder = true; //"deletes" by setting placeholder status
        }
        this.size--;
        return n.value; //returns value of node that was "deleted"
    }

    /**
     * Update the value associated with a key.
     *
     * @param k The key.
     * @param v The value to be associated with k.
     * @throws IllegalArgumentException If k is null or not mapped.
     */

    public void put(K k, V v) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        Node n = this.find(k); //gets node to update
        if (n == null) {
            throw new IllegalArgumentException(); //node not mapped
        } else if (n.placeholder == true) {
            throw new IllegalArgumentException(); //node considered deleted if true
        }
        n = new Node(k,v);
    }

    /**
     * Get the value associated with a key.
     *
     * @param k The key.
     * @return The value associated with k.
     * @throws IllegalArgumentException If k is null or not mapped.
     */

    public V get(K k) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        Node n = this.find(k);
        if (n == null) {
            throw new IllegalArgumentException(); //not mapped
        } else if (n.placeholder == true) {
            throw new IllegalArgumentException(); //not mapped (considered deleted)
        }
        return n.value;
    }

    /**
     * Check existence of a key.
     *
     * @param k The key.
     * @return True if k is mapped, false otherwise (even for null!).
     */

    public boolean has(K k) {
        if (k == null) {
            return false;
        }
        Node n = this.find(k);
        if (n == null) {
            return false; //not mapped
        } else if (n.placeholder == true) { //considered deleted
            return false;
        }
        return true;
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
                keys.add(this.chain[i].key);
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
                 out += this.chain[i].key + ": " + this.chain[i].value + "\n";
             }
         }
         return out;
     }

 }
