import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Prefix-Trie. Supports linear time find() and insert().
 * Should support determining whether a word is a full word in the
 * Trie or a prefix.
 *
 * @author
 */
public class Trie {

    private TrieNode head;
    private HashMap<Character, Integer> order;

    public Trie() {
        head = null;
        order = new HashMap<>();
        String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ, ";
        for (int i = 0; i < s.length(); i++) {
            order.put(s.charAt(i), i + 1);
        }
    }

    public Trie(String s) {
        order = new HashMap<Character, Integer>();
        for (int i = 0; i < s.length(); i++) {
            order.put(s.charAt(i), i + 1);
        }
    }

    public Trie(TrieNode h, String s) {
        head = h;
        for (int i = 0; i < s.length(); i++) {
            order.put(s.charAt(i), i + 1);
        }
    }

    public TrieNode getHead() {
        return head;
    }

    public HashMap<Character, Integer> getOrder() {
        return order;
    }

    public void setOrder(char c, int b) {
        order.put(c, b);
    }


    public class TrieNode {
        private char character;
        private TrieNode leftLink;
        private TrieNode middleLink;
        private TrieNode rightLink;
        private boolean terminal;
        private double val;
        private TrieNode parent;
        private double max;

        public TrieNode(char character) {
            this.character = character;
            leftLink = null;
            middleLink = null;
            rightLink = null;
            terminal = false;
            val = 0;
            max = 0;
        }

        public TrieNode(char c, TrieNode l, TrieNode m, TrieNode r, boolean t, double v) {
            character = c;
            leftLink = l;
            middleLink = m;
            rightLink = r;
            terminal = t;
            val = v;
            max = v;
        }



        //All the getter methods
        public char getCharacter() {
            return character;
        }

        public TrieNode getLeftLink() {
            return leftLink;
        }

        public TrieNode getMiddleLink() {
            return middleLink;
        }

        public TrieNode getRightLink() {
            return rightLink;
        }

        public boolean isTerminal() {
            return terminal;
        }

        public double getVal() {
            return val;
        }

        public TrieNode getParent() {
            return parent;
        }

        public double getMax() {
            return max;
        }





        //All the setter methods
        public void setCharacter(char character) {
            this.character = character;
        }

        public void setLeftLink(TrieNode leftLink) {
            this.leftLink = leftLink;
        }

        public void setMiddleLink(TrieNode middleLink) {
            this.middleLink = middleLink;
        }

        public void setRightLink(TrieNode rightLink) {
            this.rightLink = rightLink;
        }

        public void setTerminal(boolean terminal) {
            this.terminal = terminal;
        }

        public void setVal(int val) {
            this.val = val;
        }
    }


    public boolean find(String s, boolean isFullWord) {

        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("Input cannot be empty!");
        }

        int length = s.length();
        int value = valCalculator(s);
        TrieNode curr = head;
        int index = 0;

        while (index < length) {
            if (curr == null) {
                return false;
            }

            char toSearch = s.charAt(index);
            String result = findComparator(curr, toSearch);
            if (result.equals("goLeft")) {
                curr = curr.leftLink;
            } else if (result.equals("goRight")) {
                curr = curr.rightLink;
            } else {
                index++;
                if (index == length) {
                    break;
                }
                curr = curr.middleLink;
            }
        }

        if (isFullWord) {

            return (curr.terminal && curr.val == value);

        } else {
            return true;
        }

    }

    public String findComparator(TrieNode a, char b) {
        int oldChar = order.get(a.character);
        int newChar = order.get(b);

        if (newChar < oldChar) {
            return "goLeft";
        } else if (newChar > oldChar) {
            return "goRight";
        } else {
            return "justRight";
        }
    }

    public void insert(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("Argument is illegal!");
        }
        int length = s.length();
        if (head == null) {
            insertHelper(s);
        } else {
            TrieNode curr = head;
            int index = 0;
            while (index < length) {
                char toInsert = s.charAt(index);
                String result = nodeComparator(curr, toInsert);
                if (result.equals("goLeft")) {
                    curr = curr.leftLink;
                } else if (result.equals("goLeftNull")) {
                    index++;
                    curr.leftLink = new TrieNode(toInsert);
                    curr = curr.leftLink;
                    if (index == length) {
                        curr.terminal = true;
                        curr.val = valCalculator(s);
                        return;
                    }
                    for (int i = index; i < s.length(); i++) {
                        curr.middleLink = new TrieNode(s.charAt(i));
                        index++;
                        curr = curr.middleLink;
                    }
                    curr.terminal = true;
                    curr.val = valCalculator(s);
                } else if (result.equals("goRight")) {
                    curr = curr.rightLink;
                } else if (result.equals("goRightNull")) {
                    index++;
                    curr.rightLink = new TrieNode(toInsert);
                    curr = curr.rightLink;
                    if (index == length) {
                        curr.terminal = true;
                        curr.val = valCalculator(s);
                        return;
                    }
                    for (int i = index; i < s.length(); i++) {
                        curr.middleLink = new TrieNode(s.charAt(i));
                        index++;
                        curr = curr.middleLink;
                    }
                    curr.terminal = true;
                    curr.val = valCalculator(s);
                } else if (result.equals("goDown")) {
                    index++;
                    if (index == length) {
                        curr.terminal = true;
                        curr.val = valCalculator(s);
                        return;
                    }
                    curr = curr.middleLink;
                } else {
                    index++;
                    if (index == length) {
                        curr.terminal = true;
                        curr.val = valCalculator(s);
                        return;
                    }
                    for (int i = index; i < s.length(); i++) {
                        curr.middleLink = new TrieNode(s.charAt(i));
                        index++;
                        curr = curr.middleLink;
                    }
                    curr.terminal = true;
                    curr.val = valCalculator(s);
                }
            }
        }
    }

    public void insertHelper(String s) {
        head = new TrieNode(s.charAt(0));
        head.parent = null;
        TrieNode curr = head;
        TrieNode prev = head;
        for (int i = 1; i < s.length(); i++) {
            curr.middleLink = new TrieNode(s.charAt(i));
            curr = curr.middleLink;
            curr.parent = prev;
            prev = curr;
        }
        curr.terminal = true;
        curr.val = valCalculator(s);
    }


    //The insert function for Autocomplete
    public void insert(String s, double w) {
        exceptionHelper(s);
        if (head == null) {
            insertHelper(s, w);
        } else {
            TrieNode curr = head;
            TrieNode prev = head;
            maxChecker(curr, w);
            int index = 0;
            while (index < s.length()) {
                String result = nodeComparator(curr, s.charAt(index));
                if (result.equals("goLeft")) {
                    curr = curr.leftLink;
                    maxChecker(curr, w);
                    prev = curr;
                } else if (result.equals("goLeftNull")) {
                    index++;
                    leftLinkHelper(curr, s.charAt(index - 1), w);
                    curr = curr.leftLink;
                    curr.parent = prev;
                    prev = curr;
                    if (terminator(index, s.length(), curr, w)) {
                        return;
                    }
                    for (int i = index; i < s.length(); i++) {
                        middleLinkHelper(curr, s.charAt(i), w);
                        index++;
                        curr = curr.middleLink;
                        curr.parent = prev;
                        prev = curr;
                    }
                    terminalSetter(curr, w);
                } else if (result.equals("goRight")) {
                    curr = curr.rightLink;
                    maxChecker(curr, w);
                    curr.parent = prev;
                    prev = curr;
                } else if (result.equals("goRightNull")) {
                    index++;
                    rightLinkHelper(curr, s.charAt(index - 1), w);
                    curr = curr.rightLink;
                    curr.parent = prev;
                    prev = curr;
                    if (terminator(index, s.length(), curr, w)) {
                        return;
                    }
                    for (int i = index; i < s.length(); i++) {
                        middleLinkHelper(curr, s.charAt(i), w);
                        index++;
                        curr = curr.middleLink;
                        curr.parent = prev;
                        prev = curr;
                    }
                    terminalSetter(curr, w);
                } else if (result.equals("goDown")) {
                    index++;
                    if (terminator(index, s.length(), curr, w)) {
                        return;
                    }
                    curr = curr.middleLink;
                    maxChecker(curr, w);
                    curr.parent = prev;
                    prev = curr;
                } else {
                    index++;
                    if (terminator(index, s.length(), curr, w)) {
                        return;
                    }
                    for (int i = index; i < s.length(); i++) {
                        middleLinkHelper(curr, s.charAt(i), w);
                        index++;
                        curr = curr.middleLink;
                        curr.parent = prev;
                        prev = curr;
                    }
                    terminalSetter(curr, w);
                }
            }
        }
    }

    public void maxChecker(TrieNode a, double b) {
        if (b > a.max) {
            a.max = b;
        }
    }

    public void terminalSetter(TrieNode a, double b) {
        a.terminal = true;
        a.val = b;
    }

    public void exceptionHelper(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("Argument is illegal!");
        }
    }

    public boolean terminator(int ind, int leng, TrieNode cur, double w) {
        if (ind == leng) {
            terminalSetter(cur, w);
            return true;
        }
        return false;
    }

    public void leftLinkHelper(TrieNode c, char toInsert, double w) {
        c.leftLink = new TrieNode(toInsert);
        c.leftLink.max = w;
    }


    public void rightLinkHelper(TrieNode c, char toInsert, double w) {
        c.rightLink = new TrieNode(toInsert);
        c.rightLink.max = w;
    }

    public void middleLinkHelper(TrieNode c, char toInsert, double w) {
        c.middleLink = new TrieNode(toInsert);
        c.middleLink.max = w;
    }

    public void middleLinkHelper2(TrieNode c, TrieNode p, double w) {
        c = c.middleLink;
        c.max = w;
        c.parent = p;
        p = c;
    }

    public void insertHelper(String s, double w) {
        head = new TrieNode(s.charAt(0));
        head.max = w;
        head.parent = null;

        TrieNode curr = head;
        TrieNode prev = head;
        for (int i = 1; i < s.length(); i++) {
            curr.middleLink = new TrieNode(s.charAt(i));
            curr = curr.middleLink;
            curr.parent = prev;
            curr.max = w;
            prev = curr;
        }

        curr.terminal = true;
        curr.val = w;
        curr.max = w;
    }


    public String nodeComparator(TrieNode a, char toInsert) {
        if (a == null) {
            return "newPath";
        }

        int oldChar = order.get(a.character);
        int newChar = order.get(toInsert);
        if (newChar < oldChar) {
            if (a.leftLink != null) {
                return "goLeft";
            } else {
                return "goLeftNull";
            }

        } else if (newChar > oldChar) {
            if (a.rightLink != null) {
                return "goRight";
            } else {
                return "goRightNull";
            }

        } else {
            if (a.middleLink != null) {
                return "goDown";
            } else {
                return "goDownNull";
            }
        }
    }

    public int valCalculator(String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (order.containsKey(s.charAt(i))) {
                result += order.get(s.charAt(i));
            }
        }
        return result;
    }

    public ArrayList<String> traverse() {
        ArrayList<String> result = new ArrayList<>();
        Stack stack = new Stack();
        TrieNode curr = head;
        stack.push(curr);
        while (!stack.isEmpty()) {
            TrieNode now = (TrieNode) stack.pop();

            if (now.rightLink != null) {
                stack.push(now.rightLink);
            }
            if (now.middleLink != null) {
                stack.push(now.middleLink);
            }
            if (now.leftLink != null) {
                stack.push(now.leftLink);
            }

            if (now.isTerminal()) {
                double ind = now.val;
                StringBuilder sb = new StringBuilder();
                sb.insert(0, now.character);
                ind = ind - order.get(now.character);
                while (ind != 0) {
                    TrieNode parent = now.parent;
                    if (parent.middleLink == now) {         //十分有可能会出现equal的问题
                        sb.insert(0, now.parent.character);
                        ind = ind - order.get(now.parent.character);
                    }
                    now = now.parent;
                }
                result.add(sb.toString());
            }

        }
        return result;
    }

    public static void main(String[] args) {
        Trie t = new Trie("abcdefghijklmnopqrstuvwxyz");
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        System.out.println(t.find("hell", false));
        System.out.println(t.find("hello", true));
        System.out.println(t.find("good", false));
        System.out.println(t.find("bye", false));
        System.out.println(t.find("heyy", false));
        System.out.println(t.find("hell", true));
    }
}
