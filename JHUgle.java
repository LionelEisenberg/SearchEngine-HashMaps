/*
Lionel Eisenberg (leisenb5) & Sanat Deshpande (sdeshpa4)
main file for JHUgle.
*/

import java.io.IOException;
import java.io.File;
import java.util.EmptyStackException;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Set;
import java.util.Stack;

/** JHUgle class. */
public final class JHUgle {
    /** Hashmap. */
    private static HashMap<String, ArrayList<String>> hashmap =
        new HashMap<String, ArrayList<String>>();

    private JHUgle() {}

    /**
    * Main file for JHUgle, indexes file.
    *
    * @param args command line which gives name of file to index
    * @throws IOException if file is non existent.
    */
    public static void main(String[] args) throws IOException {
        Scanner inFile;
        if (0 < args.length) {
            inFile = new Scanner(new File(args[0]));
        } else {
            System.out.println("Error, wrong command line arguments");
            return;
        }

        Pattern pattern = Pattern.compile("[\\s[^0-9a-zA-Z]]+");

        while (inFile.hasNext()) { //get each input in file
            String url = inFile.nextLine(); //get url into a string
            ArrayList<String> urls = new ArrayList<>();
            String keyWords = inFile.nextLine(); //get all keyWords
            String[] words = pattern.split(keyWords); //split keywords into arra
            TreeSet<String> wordsSet = new TreeSet<>();
            //make sure there are no repitions in keys
            for (String word : words) {
                wordsSet.add(word);
            }
            //for each word in set insert in map
            for (String word : wordsSet) {
                //if key already exists, concatenate value
                if (hashmap.has(word)) {
                    urls = hashmap.get(word);
                    urls.add(url);
                    hashmap.put(word, urls);
                } else {
                    urls = new ArrayList<>();
                    urls.add(url);
                    hashmap.insert(word, urls);
                }
            }
        }
        System.out.println("Index created");

        getInputQuery();
    }

    private static void getInputQuery() {

        Scanner kb = new Scanner(System.in);
        Stack<ArrayList<String>> rpnStack = new Stack<>();
        System.out.print("> ");
        while (kb.hasNext()) {
            String[] nextLine = kb.nextLine().split(" ");
            for (int i = 0; i < nextLine.length; i++) {
                String next = nextLine[i];

                if (next.compareTo("!") == 0) {
                    return; //break out of loop and exit if the input is "!"
                } else if (next.compareTo("&&") == 0) {
                    ArrayList<String> listV = new ArrayList<>();
                    String oredKeyToPopBack = "";
                    try {
                        ArrayList<String> val1 = rpnStack.pop();
                        ArrayList<String> val2 = rpnStack.pop();

                        for (String url1 : val1) {
                            for (String url2 : val2) {
                                if (url1.compareTo(url2) == 0) {
                                    listV.add(url1);
                                }
                            }
                        }
                        rpnStack.push(listV);
                    } catch (EmptyStackException e) {
                        System.err.println("Stack too empty to and!");
                    }
                } else if (next.compareTo("||") == 0) {
                    Set<String> treeS = new TreeSet<>();
                    String oredKeyToPopBack = "";
                    ArrayList<String> listV = new ArrayList<>();
                    try {
                        ArrayList<String> val1 = rpnStack.pop();
                        ArrayList<String> val2 = rpnStack.pop();
                        for (String url : val1) {
                            treeS.add(url);
                        }

                        for (String url : val2) {
                            treeS.add(url);
                        }

                        for (String result : treeS) {
                            listV.add(result);
                        }
                        rpnStack.push(listV);
                    } catch (EmptyStackException e) {
                        System.err.println("Stack too empty to or!");
                    }
                } else if (next.compareTo("?") == 0) {
                    try {
                        ArrayList<String> values = rpnStack.peek();
                        for (String value : values) {
                            if (!"".equals(value)) {
                                System.out.println(value);
                            }
                        }
                    } catch (EmptyStackException e) {
                        System.err.println("Stack is empty");
                    }
                } else {
                    if (hashmap.has(next)) {
                        rpnStack.push(hashmap.get(next));
                    } else {
                        ArrayList<String> empty = new ArrayList<>();
                        empty.add("");
                        rpnStack.push(empty);
                    }
                }
            }
            System.out.print("> ");
        }
    }
}
