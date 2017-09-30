

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Stack;
import java.util.Comparator;
/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 *
 * @author
 */
public class Autocomplete {
    private Trie data;

    /**
     * Initializes required data structures from parallel arrays.
     *
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {

        if (terms.length != weights.length) {
            throw new IllegalArgumentException("Different lengths!");
        }

        data = new Trie();

        HashSet<String> seen = new HashSet<>();
        HashMap check = data.getOrder();

        for (int i = 0; i < terms.length; i++) {
            if (seen.contains(terms[i])) {
                throw new IllegalArgumentException("Duplicates!");
            }

            if (weights[i] < 0) {
                throw new IllegalArgumentException("Negative weights!");
            }



            char[] j = terms[i].toCharArray();
            for (char c : j) {
                if (!check.containsKey(c)) {
                    data.setOrder(c, data.getOrder().size() + 1);
                }
            }
            data.insert(terms[i], weights[i]);
            seen.add(terms[i]);
        }

    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     *
     * @param term
     * @return
     */
    public double weightOf(String term) {
        int length = term.length();
        Trie.TrieNode curr = data.getHead();
        int index = 0;

        while (index < length) {
            if (curr == null) {
                return 0.0;
            }

            char toSearch = term.charAt(index);
            String result = data.findComparator(curr, toSearch);
            if (result.equals("goLeft")) {
                curr = curr.getLeftLink();
            } else if (result.equals("goRight")) {
                curr = curr.getRightLink();
            } else {
                index++;
                if (index == length) {
                    break;
                }
                curr = curr.getMiddleLink();
            }
        }

        if (curr.isTerminal()) {
            return curr.getVal();
        } else {
            return 0.0;
        }
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        HashMap<String, Double> allMatches = new HashMap<>();
        String bestSoFar = null;
        int length = prefix.length();
        Trie.TrieNode curr = data.getHead();
        int index = 0;
        while (index < length) {
            if (curr == null) {
                return null;
            }
            char toSearch = prefix.charAt(index);
            String result = data.findComparator(curr, toSearch);
            if (result.equals("goLeft")) {
                curr = curr.getLeftLink();
            } else if (result.equals("goRight")) {
                curr = curr.getRightLink();
            } else {
                index++;
                if (index == length) {
                    break;
                }
                curr = curr.getMiddleLink();
            }
        }
        if (curr.isTerminal()) {
            allMatches.put(prefix, curr.getVal());
            bestSoFar = prefix;
        }
        if (curr.getMiddleLink() == null) {
            return bestSoFar;
        }
        curr = curr.getMiddleLink();
        Trie.TrieNode beginning = curr;
        if (prefix.equals("")) {
            curr = data.getHead();
            beginning = curr;
        }
        Stack stack = new Stack();
        Trie.TrieNode firstToGo = curr;
        stack.push(firstToGo);
        while (!stack.isEmpty()) {
            Trie.TrieNode now = (Trie.TrieNode) stack.pop();
            if (now.getRightLink() != null) {
                stack.push(now.getRightLink());
            }
            if (now.getMiddleLink() != null) {
                stack.push(now.getMiddleLink());
            }
            if (now.getLeftLink() != null) {
                stack.push(now.getLeftLink());
            }
            if (now.isTerminal()) {
                double finalVal = now.getVal();
                StringBuilder sb = new StringBuilder();
                sb.insert(0, now.getCharacter());
                while (now != beginning) {
                    Trie.TrieNode parent = now.getParent();
                    if (parent.getMiddleLink() != null) {
                        if (parent.getMiddleLink().equals(now)) {         //十分有可能会出现equal的问题
                            sb.insert(0, now.getParent().getCharacter());
                        }
                    }
                    now = now.getParent();
                }
                String word = prefix + sb.toString();
                if (allMatches.isEmpty()) {
                    bestSoFar = word;
                }
                allMatches.put(word, finalVal);
                if (finalVal > allMatches.get(bestSoFar)) {
                    bestSoFar = word;
                }
            }

        }
        return bestSoFar;
    }

    public PriorityQueue<Trie.TrieNode> createpq() {
        return new PriorityQueue<>(1, new Comparator<Trie.TrieNode>() {
            @Override
            public int compare(Trie.TrieNode o1, Trie.TrieNode o2) {
                if (o1.getMax() < o2.getMax()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    public Trie.TrieNode findcurr(String prefix) {
        int length = prefix.length();
        Trie.TrieNode curr = data.getHead();
        int index = 0;
        while (index < length) {
            if (curr == null) {
                return null;
            }
            char toSearch = prefix.charAt(index);
            if (!data.getOrder().containsKey(toSearch)) {
                return null;
            }
            String result = data.findComparator(curr, toSearch);
            if (result.equals("goLeft")) {
                curr = curr.getLeftLink();
            } else if (result.equals("goRight")) {
                curr = curr.getRightLink();
            } else {
                index++;
                if (index == length) {
                    break;
                }
                curr = curr.getMiddleLink();
            }
        }
        return curr;
    }

    public String makest(Trie.TrieNode now, Trie.TrieNode root) {
        StringBuilder sb = new StringBuilder();
        sb.insert(0, now.getCharacter());

        while (now != root) {
            Trie.TrieNode parent = now.getParent();
            if (parent.getMiddleLink() != null) {
                if (parent.getMiddleLink().equals(now)) {         //十分有可能会出现equal的问题
                    sb.insert(0, now.getParent().getCharacter());
                }
            }
            now = now.getParent();
        }

        return sb.toString();
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     *
     * @param prefix
     * @param k
     * @return
     */

    public Iterable<String> topMatches(String prefix, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k should be non-negative");
        }
        HashMap<String, Double> best = new HashMap<>();
        PriorityQueue<Trie.TrieNode> pq = createpq();
        Trie.TrieNode curr = findcurr(prefix);
        if (curr == null) {
            return new ArrayList<>();
        }
        boolean flag = false;
        if (curr.isTerminal()) {
            best.put(prefix, curr.getVal());
            flag = true;
        }
        Trie.TrieNode root;
        if (prefix.length() == 0) {
            root = curr;
        } else {
            root = curr.getMiddleLink();
            if (root == null) {
                ArrayList result = new ArrayList();
                result.add(prefix);
                return result;
            }
        }
        double min;
        if (flag) {
            min = Math.min(root.getMax(), curr.getVal());
        } else {
            min = root.getMax();
        }
        pq.add(root);
        while (!pq.isEmpty()) {
            Trie.TrieNode now = pq.poll();
            if (now.getRightLink() != null) {
                pq.add(now.getRightLink());
            }
            if (now.getMiddleLink() != null) {
                pq.add(now.getMiddleLink());
            }
            if (now.getLeftLink() != null) {
                pq.add(now.getLeftLink());
            }
            double value = now.getVal();
            double max = now.getMax();
            if (now.isTerminal()) {
                String st = makest(now, root);
                int lg = best.size();
                if (lg < k) {
                    best.put(prefix + st, value);
                    if (value < min) {
                        min = value;
                    }
                } else {
                    if (max < min) {
                        break;
                    } else {
                        double temp = value;
                        String toremove = null;
                        for (String i : best.keySet()) {
                            if (best.get(i) == min) {
                                toremove = i;
                            } else {
                                if (best.get(i) < temp) {
                                    temp = best.get(i);
                                }
                            }
                        }
                        min = temp;
                        best.remove(toremove);
                        best.put(prefix + st, value);
                    }
                }
            }
        }
        Set<String> a = best.keySet();
        return a.stream().sorted((o1, o2) -> -best.get(o1).
                        compareTo(best.get(o2))).collect(Collectors.toList());
    }

    /**
     * Test client. Reads the data from the file, then repeatedly reads autocomplete
     * queries from standard input and prints out the top k matching terms.
     *
     * @param args takes the name of an input file and an integer k as
     *             command-line arguments
     */
    public static void main(String[] args) {


        String[] terms = {"hallo", "halo", "haeyo", "lala" };
        double[] weights = {1, 20, 30, 40};

        Autocomplete auto = new Autocomplete(terms, weights);
        System.out.println(auto.topMatch(""));

        String[] terms2 = {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights2 = {5, 10, 12, 20, 15, 7};
        Autocomplete auto2 = new Autocomplete(terms2, weights2);
        System.out.println(auto2.topMatches("so", 3));



        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms3 = new String[N];
        double[] weights3 = new double[N];
        for (int i = 0; i < N; i++) {
            weights3[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms3[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms3, weights3);

        System.out.println(autocomplete.topMatches("pa", 10));


        /*
        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }
        */
    }
}
