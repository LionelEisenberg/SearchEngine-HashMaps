/*
Lionel Eisenberg (leisenb5) & Sanat Deshpande (sdeshpa4)
main file for JHUgle.
*/

import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.EmptyStackException;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Set;
import java.util.Stack;
import java.lang.Exception;


public class JHUgle {
    private static HashMapTwo<String, ArrayList<String>> hashmap = new HashMapTwo<String, ArrayList<String>>();

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
            String url = inFile.nextLine();
            ArrayList<String> urls = new ArrayList<>();
            String keyWords = inFile.nextLine();
            String[] words = pattern.split(keyWords);
            for (String word : words) {
                System.out.println(urls);
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

        GetInputQuery(hashmap);
    }

    private static void GetInputQuery(HashMapTwo<String, ArrayList<String>> hashmap) {
        Scanner kb = new Scanner(System.in);
        Stack<ArrayList<String>> rpnStack = new Stack<>();
        System.out.print("> ");
        while (kb.hasNext()) {
            String next = kb.next();  //get next input from User
            if (next.compareTo("!") == 0) {
                break; //break out of loop and exit if the input is "!"
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
                    System.out.print("> ");
                } catch (EmptyStackException e) {
                    System.err.println("Stack to empty to and!");
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
                    System.out.print("> ");
                } catch (EmptyStackException e) {
                    System.err.println("Stack to empty to or!");
                }
            } else if (next.compareTo("?") == 0) {
                try {
                    ArrayList<String> values = rpnStack.peek();
                    for (String value : values) {
                        System.out.println(value);
                    }
                    System.out.print("> ");
                } catch (EmptyStackException e) {
                    System.err.println("Stack is empty");
                }
            } else {
                if (hashmap.has(next)) {
                    rpnStack.push(hashmap.get(next));
                    System.out.print("> ");
                } else {
                    System.out.println("Cannot Be found in database or this" +
                    "command is not recognised, please try again.");
                }
            }
        }
    }
}
