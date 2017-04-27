import com.github.phf.jb.Bench;
import com.github.phf.jb.Bee;
import java.util.*;

public final class HashMapBench {
    private static final int SIZE = 200;
    private static final int STRINGSIZE = 8;
    private static String allPossible = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random random = new Random();
    private HashMapBench() {}

        //insert helper method.
        private static void insert(HashMap<Integer, ArrayList<String>> m, Integer[] data, ArrayList<String> list) {
            for (Integer i = 0; i < SIZE; i++) {
                try {
                    m.insert(data[i], list);
                } catch (IllegalArgumentException e) {
                }
            }
        }

        //remove helper method
        private static void remove(HashMap<Integer, ArrayList<String>> m, Integer[] data) {
            for (Integer i = 0; i < SIZE; i++) {
                m.remove(data[i]);
            }
        }

        //find helper method.
        private static void lookup(HashMap<Integer, ArrayList<String>> m, Integer[] data) {
            for (Integer i = 0; i < SIZE; i++) {
                boolean x = m.has(data[i]);
            }
        }

        private static void put(HashMap<Integer, ArrayList<String>> m, Integer[] data, ArrayList<String> list) {
            for (Integer i = 0; i < SIZE; i++) {
                m.put(data[i], list);
            }
        }

        // Now the benchmarks we actually want to run.
        @Bench
        public static void insert(Bee b) {
            for (int n = 0; n < b.reps(); n++) {
                b.stop();
                Integer[] data = new Integer[SIZE];
                ArrayList<String> list = new ArrayList<String>();
                //Make an array of size 200 to benchmark
                for(int i = 0; i < SIZE; i++) {
                    data[i] = i;
                    list.add("Easter Egg");
                }
                HashMap<Integer, ArrayList<String>> m = new HashMap<Integer, ArrayList<String>>();
                b.start();
                insert(m, data, list);
            }
        }

        @Bench
        public static void lookup(Bee b) {
            for (int n = 0; n < b.reps(); n++) {
                b.stop();
                Integer[] data = new Integer[SIZE];
                ArrayList<String> list = new ArrayList<String>();
                //Make an array of size 200 to benchmark
                for(int i = 0; i < SIZE; i++) {
                    data[i] = i;
                    list.add("Easter Egg");
                }
                HashMap<Integer, ArrayList<String>> m = new HashMap<Integer, ArrayList<String>>();
                insert(m, data, list);
                b.start();
                lookup(m, data);
            }
        }

        @Bench
        public static void remove(Bee b) {
            for (int n = 0; n < b.reps(); n++) {
                b.stop();
                Integer[] data = new Integer[SIZE];
                //Make an array of size 200 to benchmark
                for(int i = 0; i < SIZE; i++) {
                    data[i] = i;
                }
                HashMap<Integer, ArrayList<String>> m = new HashMap<Integer, ArrayList<String>>();
                b.start();
                remove(m, data);
            }
        }

        @Bench
        public static void put(Bee b) {
            for (int n = 0; n < b.reps(); n++) {
                b.stop();
                Integer[] data = new Integer[SIZE];
                ArrayList<String> list = new ArrayList<String>();
                //Make an array of size 200 to benchmark
                for(int i = 0; i < SIZE; i++) {
                    data[i] = i;
                    list.add("Easter Egg");
                }
                HashMap<Integer, ArrayList<String>> m = new HashMap<Integer, ArrayList<String>>();
                insert(m, data, list);
                b.start();
                put(m, data, list);
            }
        }
    }
