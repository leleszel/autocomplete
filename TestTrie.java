import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Trie class.
 *  @author
 */
public class TestTrie {

    Trie myTrie = new Trie();

    /** A dummy test to avoid complaint. */
    @Test
    public void testInsert() {
        myTrie.insert("little");
        myTrie.insert("lip");
        myTrie.insert("lit");
        myTrie.insert("lot");
        myTrie.insert("later");


        //Test error cases
        try {
            myTrie.insert("");
            fail("Exception not thrown!");
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed!");
        }

        try {
            myTrie.insert(null);
            fail("Exception not thrown!");
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed!");
        }

        try {
            myTrie.find(null, true);
            fail("Exception not thrown!");
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed!");
        }

        try {
            myTrie.find("", false);
            fail("Exception not thrown!");
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed!");
        }


        //Test full words
        assertEquals(true, myTrie.find("lit", true));
        assertEquals(false, myTrie.find("lott", true));
        assertEquals(false, myTrie.find("lat", true));
        assertEquals(false, myTrie.find("lips", true));
        assertEquals(true, myTrie.find("later", true));
        assertEquals(false, myTrie.find("haha", true));
        assertEquals(false, myTrie.find("another", true));


        //Test incomplete words
        assertEquals(true, myTrie.find("lit", false));
        assertEquals(false, myTrie.find("lipp", false));
        assertEquals(true, myTrie.find("lot", false));
        assertEquals(true, myTrie.find("la", false));
        assertEquals(true, myTrie.find("l", false));
        assertEquals(false, myTrie.find("haha", false));
        assertEquals(false, myTrie.find("you", false));

    }


    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestTrie.class);
    }
}
