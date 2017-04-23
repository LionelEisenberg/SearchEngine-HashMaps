import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Count unique words.
 *
 * Read text from standard input, count how often each unique word appears,
 * write the results to standard output. Note that the definiton of "word"
 * is rather arbitrary and won't make the linguists among you very happy.
 */
public final class Words {
    // Make checkstyle happy.
    private Words() {}

    /**
     * Main method.
     * @param args Command line arguments (ignored).
     * @throws IOException in the unlikely event of a loss of input pressure.
     */
    public static void main(String[] args) throws IOException {
        // The regular expression splits strings on whitespace and non-letter
        // characters (anything except [a-zA-Z]). Far from perfect, but close
        // enough for this simple program.
        Pattern pattern = Pattern.compile("[\\s[^0-9    a-zA-Z]]+");
        Map<String, Integer> data = new TreapMap<>();

        // If you're wondering why we're not using Scanner instead, you're
        // welcome to try out what happens... :-)
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = pattern.split(line);
            for (String word: words) {
                if (word.length() <= 1) {
                    // Skip "short" words, most of which just "dirty up"
                    // the statistics.
                    continue;
                }
                if (data.has(word)) {
                    data.put(word, data.get(word) + 1);
                } else {
                    data.insert(word, 1);
                }
            }
        }

        reader.close();
        input.close();

        for (String word: data) {
            System.out.println(word + ": " + data.get(word));
        }
    }
}
