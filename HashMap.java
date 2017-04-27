/**
 * Lionel Eisenberg & Sanat Deshpande
 * leisenb5 & sdeshpa4
 * Data Structures – Assignment 9
 */

/**
 * HashMap implementation.
 */


import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Array;

/**
 * This is an implementation of Map called HashMap. It uses a hash function
 * to map key-value pairs to indices in an array. It employs open addressing
 * and quadratic probing as a collision resolution policy.
 * @param <K> Type for keys
 * @param <V> Type for values
 */

public class HashMap<K, V> implements Map<K, V> {
    private static final double LOADFACTOR = 0.5;
    private static final int DEFAULTSIZE = 131; //size is always prime

    private class Node {
        K key;
        V value;
        boolean placeholder;

        Node(K k, V v) {
            this.key = k;
            this.value = v;
            this.placeholder = false;
        }

        public String toString() {
            return this.key + ": " + this.value;
        }
    }

    private Node[] chain;
    private int size;  //refers to number of key-value pairs

    /**
     * Initializes array with default size.
     */

    public HashMap() {
        this.size = 0;
        this.chain = (Node[]) Array.newInstance(Node.class, DEFAULTSIZE);
    }

    /**
     * Initializes array with specified size.
     * @param s size of initial array
     */

    public HashMap(int s) {
        this.size = 0;
        this.chain = (Node[]) Array.newInstance(Node.class, s); //user specified
    }

    private int hashFunction(K k) {
        K key = k; //casts generic type to string
        return key.hashCode(); //returns hashcode
    }

    private Node find(K k) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = this.hashFunction(k);
        int mod = (hashCode % this.chain.length + this.chain.length)
                    % this.chain.length;
        int probeCount = 1; //keeps track of how far to probe
        int index = 0; //keeps track of how many elements are searched
        while (this.chain[mod] != null && index < this.chain.length) {
            index++;
            if (this.chain[mod].placeholder) { //skips over tombstone
                continue;
            }
            if (this.chain[mod].key.equals(k)) { //checks for key
                return this.chain[mod];
            }
            //moves on to next slot
            mod = (mod + this.probe(probeCount)) % this.chain.length;
            probeCount++; // increment probing for next pass
        }
        return null;
    }

    private int probe(int n) {
        return n * n; // probes quadratically
    }

    /**
     * Insert a new key/value pair.
     *
     * @param k The key.
     * @param v The value to be associated with k.
     * @throws IllegalArgumentException If k is null or k is mapped.
     */

    public void insert(K k, V v) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        if (this.has(k)) {
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
            if (this.chain[mod].placeholder) {
                this.chain[mod] = new Node(k, v); //why no increase size here?
                this.size++;
                return;
            }
            //moves on to next slot
            mod = (mod + this.probe(probeCount)) % this.chain.length;
            probeCount++;
        }
        this.chain[mod] = new Node(k, v);
        this.size++;
    }

    private void resize() {
        int nextSize = this.getNextPrime(this.chain.length);
        Node[] temp = (Node[]) Array.newInstance(Node.class, this.chain.length);
        for (int i = 0; i < this.chain.length; i++) {
            if (this.chain[i] != null && !this.chain[i].placeholder) {
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

    private int getNextPrime(int s) {
        s = 2 * s + 1;
        int prime = 0;
        for (int i = s; i < 2 * s; i += 2) {
            for (int j = 3; j < i / 2; j += 2) {
                if (i % j == 0) {
                    prime = 1;
                }
            }
            if (prime == 0) {
                return i;
            }
            prime = 0;
        }
        return s;
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
            throw new IllegalArgumentException();
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
        } else if (n.placeholder) {
            throw new IllegalArgumentException(); //considered deleted if true
        }
        n.value = v;
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
        } else if (n.placeholder) {
            throw new IllegalArgumentException(); //(considered deleted)
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
        } else if (n.placeholder) { //considered deleted
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
     * String representation of HashMap.
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
