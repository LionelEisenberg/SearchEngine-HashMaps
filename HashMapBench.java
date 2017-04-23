import com.github.phf.jb.Bench;
import com.github.phf.jb.Bee;
import java.util.*;

public final class HashMapBench {
    private static final int SIZE = 200;
    private static final int STRINGSIZE = 5;
    private static String allPossible = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random random = new Random();
    private HashMapBench() {}

        //insert helper method.
        private static void insert(HashMap m, String[] data) {
            for (Integer i = 0; i < SIZE; i++) {
                m.insert(data[i], data[i]);
            }
        }

        //remove helper method
        private static void remove(HashMap m, String[] data) {
            for (Integer i = 0; i < SIZE; i++) {
                m.remove(data[i]);
            }
        }

        //find helper method.
        private static void lookup(HashMap m, String[] data) {
            for (Integer i = 0; i < SIZE; i++) {
                boolean x = m.has(data[i]);
            }
        }

        private static void put(HashMap m, String[] data) {
            for (Integer i = 0; i < SIZE; i++) {
                m.put(data[i], data[i]);
            }
        }

        //helper function to make a random string of length argument.
        private static String randomString(int length) {
            StringBuilder s = new StringBuilder(length);
            for( int i = 0; i < SIZE; i++ ) {
                s.append(allPossible.charAt(random.nextInt
                (allPossible.length())));
            }
            return s.toString();
        }

        // Now the benchmarks we actually want to run.
        @Bench
        public static void insert(Bee b) {
            for (int n = 0; n < b.reps(); n++) {
                b.stop();
                String[] data = new String[SIZE];
                //Make an array of size 200 to benchmark
                for(int i = 0; i < SIZE; i++) {
                    data[i] = randomString(STRINGSIZE);
                }
                HashMap m = new HashMap();
                b.start();
                insert(m, data);
            }
        }

        @Bench
        public static void lookup(Bee b) {
            for (int n = 0; n < b.reps(); n++) {
                b.stop();
                String[] data = new String[SIZE];
                //Make an array of size 200 to benchmark
                for(int i = 0; i < SIZE; i++) {
                    data[i] = randomString(STRINGSIZE);
                }
                HashMap m = new HashMap();
                insert(m, data);
                b.start();
                lookup(m, data);
            }
        }

        @Bench
        public static void remove(Bee b) {
            for (int n = 0; n < b.reps(); n++) {
                b.stop();
                String[] data = new String[SIZE];
                //Make an array of size 200 to benchmark
                for(int i = 0; i < SIZE; i++) {
                    data[i] = randomString(STRINGSIZE);
                }
                HashMap m = new HashMap();
                b.start();
                remove(m, data);
            }
        }

        @Bench
        public static void put(Bee b) {
            for (int n = 0; n < b.reps(); n++) {
                b.stop();
                String[] data = new String[SIZE];
                //Make an array of size 200 to benchmark
                for(int i = 0; i < SIZE; i++) {
                    data[i] = randomString(STRINGSIZE);
                }
                HashMap m = new HashMap();
                insert(m, data);
                b.start();
                put(m, data);
            }
        }
    }
