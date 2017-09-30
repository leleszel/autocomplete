import ucb.junit.textui;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Autocomplete class.
 *  @author
 */
public class TestAutocomplete {

    /** A dummy test to avoid complaint. */
    @Test
    public void autocompleteTest() {

        String[] terms = {"Lisbon", "Oslo", "Hanoi", "Lyon",
            "Osaka", "Kuala Lumper", "Bergen", "Serville", "Singapore",
            "Seattle", "Chongqing", "Chicago", "Ching", "Cyon", "Choa Chu Kang"};
        double[] weights = {1, 23, 43, 111, 23, 45, 32, 44, 5, 10, 14, 15, 16, 20, 1700};
        Autocomplete auto = new Autocomplete(terms, weights);

        //test topMatch
        assertEquals("Lyon", auto.topMatch("L"));
        assertEquals("Serville", auto.topMatch("S"));
        assertEquals("Choa Chu Kang", auto.topMatch("C"));
        assertEquals("Choa Chu Kang", auto.topMatch(""));
        assertEquals(null, auto.topMatch("A"));


        //test topMatches
        ArrayList<String> actual = new ArrayList<>();
        for (String i : auto.topMatches("L", 1)) {
            actual.add(i);
        }
        ArrayList<String> expect = new ArrayList<>();
        expect.add("Lyon");
        assertEquals(expect, actual);

        ArrayList<String> actual2 = new ArrayList<>();
        for (String i : auto.topMatches("Ch", 3)) {
            actual.add(i);
        }
        ArrayList<String> expect2 = new ArrayList<>();
        expect.add("Choa Chu Kang");
        expect.add("Ching");
        expect.add("Chicago");
        assertEquals(expect2, actual2);

        ArrayList<String> actual3 = new ArrayList<>();
        for (String i : auto.topMatches("A", 3)) {
            actual.add(i);
        }
        ArrayList<String> expect3 = new ArrayList<>();
        assertEquals(expect3, actual3);

        ArrayList<String> actual4 = new ArrayList<>();
        for (String i : auto.topMatches("Singapore", 2)) {
            actual.add(i);
        }
        ArrayList<String> expect4 = new ArrayList<>();
        expect.add("Singapore");
        assertEquals(expect4, actual4);



        //test topMatches error case
        try {
            auto.topMatches("C", -1);
            fail("Exception not thrown!");
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed!");
        }







    }



    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestAutocomplete.class);
    }
}
