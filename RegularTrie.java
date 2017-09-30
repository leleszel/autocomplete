import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class RegularTrie {
    RNode head;
    HashMap order;
    int lg;
    public RegularTrie(String s) {
        lg = s.length();
        head = new RNode(' ', null, new RNode[lg], false);
        order = new HashMap();
        for (int i = 0; i < lg; i++) {
            order.put(s.charAt(i), i);
        }
    }
    public class RNode {
        char character;
        RNode parent;
        RNode[] child;
        boolean terminal;

        public RNode(char character, RNode parent, RNode[] child, boolean terminal) {
            this.character = character;
            this.parent = parent;
            this.child = child;
            this.terminal = terminal;

        }
    }

    public void insert(String s) {
        int length = s.length();
        int i = 0;
        RNode pointer = head;
        while (i < length) {
            char a = s.charAt(i);
            int j = (int) order.get(a);
            if (pointer.child[j] == null) {
                pointer.child[j] = new RNode(a, pointer, new RNode[lg], false);
            }
            if (i == length - 1) {
                pointer.child[j].terminal = true;
                break;
            }
            pointer = pointer.child[j];
            i++;
        }
    }

    public ArrayList<String> traverse() {
        ArrayList<String> result = new ArrayList<>();
        Stack stack = new Stack();
        RNode curr = head;
        stack.push(curr);
        while (!stack.isEmpty()) {
            RNode now = (RNode) stack.pop();

            for (int i = lg - 1; i >= 0; i--) {
                if (now.child[i] != null) {
                    stack.push(now.child[i]);
                }
            }

            if (now.terminal) {
                StringBuilder sb = new StringBuilder();
                RNode up = now;
                while (up.parent != null) {
                    sb.insert(0, up.character);
                    up = up.parent;
                }
                result.add(sb.toString());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        RegularTrie t = new RegularTrie("abcdefghijklmnopqrstuvwxyz");
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        t.insert("smog");
        t.insert("buck");
        t.insert("sad");
        t.insert("spy");
        t.insert("spite");
        t.insert("spit");
        System.out.println(t.traverse());


    }


}
