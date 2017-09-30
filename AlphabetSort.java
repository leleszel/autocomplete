import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;





/**
 * AlphabetSort takes input from stdin and prints to stdout.
 * The first line of input is the alphabet permutation.
 * The the remaining lines are the words to be sorted.

 * The output should be the sorted words, each on its own line,
 * printed to std out.
 */
public class AlphabetSort {

    /**
     * Reads input from standard input and prints out the input words in
     * alphabetical order.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List arg = br.lines().collect(Collectors.toList());

        if (arg.isEmpty() || arg.size() == 1) {
            throw new IllegalArgumentException("No argument is given");
        }

        String stdin = (String) arg.get(0);

        HashSet dup = new HashSet();
        for (char i : stdin.toCharArray()) {
            int size = dup.size();
            dup.add(i);
            if (dup.size() == size) {
                throw new IllegalArgumentException("Duplicated inside alphabet");
            }
        }

        RegularTrie trie = new RegularTrie(stdin);
        for (int i = 1; i < arg.size(); i++) {
            String j = (String) arg.get(i);
            boolean flag = false;
            for (char k : j.toCharArray()) {
                if (!dup.contains(k)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                trie.insert(j);
            }

        }


        ArrayList<String> result = trie.traverse();

        for (String i : result) {
            System.out.println(i);
        }


    }
}
