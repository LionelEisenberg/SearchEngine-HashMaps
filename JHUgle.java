/*
Lionel Eisenberg (leisenb5) & Sanat Deshpande (sdeshpa4)
main file for JHUgle.
*/

import java.io.*;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Set;
import java.util.Stack;


public class JHUgle {
    private static HashMap hashmap = new HashMap();

    public static void main(String[] args) throws IOException {
        Scanner inFile;
        if (0 < args.length) {
            inFile = new Scanner(new File(args[0]));
        } else {
            System.out.println("Error, wrong command line arguments");
            return;
        }

        Pattern pattern = Pattern.compile("[\\s[^0-9a-zA-Z]]+");
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        int i = 0;
        while (inFile.hasNext()) {
            String url = inFile.nextLine();
            String keyWords = inFile.nextLine();
            String[] words = pattern.split(keyWords);
            for (String word : words) {
                hashmap.insert(word, url);
            }
        }

        GetInputQuery(hashmap);
    }

    private static void GetInputQuery(HashMap hashmap) {
        Scanner kb = new Scanner(System.in);
        Stack<String> rpnStack = new Stack<>();
        while (kb.hasNext()) {
            String next = kb.next(); //get next input from User
            rpnStack.push(next);
            if (rpnStack.peek().compareTo("!") == 0) {
                break; //break out of loop and exit if the input is "!"
            }
            if (rpnStack.peek().compareTo("&&") == 0) {
                String key1 = rpnStack.pop();
                String key2 = rpnStack.pop();
                //concatenate & pop back on top of stack
            }
            if (rpnStack.peek().compareTo("||") == 0) {
                Set<String> treeS = new TreeSet<>();
                String oredKeyToPopBack = "";

                rpnStack.pop();
                String key1 = rpnStack.pop();
                String key2 = rpnStack.pop();
                String key1result = hashmap.get(key1);
                String key2result = hashmap.get(key2);

                String[] urls = key1result.split(" ");
                for (String url : urls) {
                    treeS.add(url);
                }

                urls = key2result.split(" ");
                for (String url : urls) {
                    treeS.add(url);
                }
                for (String result : treeS) {
                    oredKeyToPopBack += result;
                }
                rpnStack.push(oredKeyToPopBack);
            } else if (rpnStack.peek().compareTo("?") == 0) {
                rpnStack.pop();
                System.out.println(hashmap.get(rpnStack.pop()));
            }
        }
    }
}
