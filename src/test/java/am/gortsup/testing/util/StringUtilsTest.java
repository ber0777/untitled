package am.gortsup.testing.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class StringUtilsTest {

    @Test
    public void toSlug() {
        String slug = StringUtils.toSlug("Crème Brûlée!");

        assertEquals(slug, "creme-brulee");
    }

    @Test
    public void capitalizeWords() {
        String result = StringUtils.capitalizeWords("jOhN dOE");

        assertEquals(result, "John Doe");
    }

    @Test
    public void truncate() {
        String result = StringUtils.truncate("Hello world", 8, "...");

        assertEquals(result, "Hello...");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void truncateRejectsTooShortLength() {
        StringUtils.truncate("x", 2, "...");
    }

    @Test
    public void countOccurrences() {
        assertEquals(StringUtils.countOccurrences("aaaa", "aa"), 2);
        assertEquals(StringUtils.countOccurrences(null, "a"), 0);
    }

    @Test
    public void isPalindrome() {
        assertTrue(StringUtils.isPalindrome("A man, a plan, a canal: Panama"));
        assertFalse(StringUtils.isPalindrome("hello"));
    }

    @Test
    public void maskCreditCard() {
        String maskedNumber = StringUtils.maskCreditCard("4111-1111-1111-1111");

        assertEquals(maskedNumber, "************1111");
    }

    @Test
    public void reverseWords() {
        String reversedSentence = StringUtils.reverseWords("a b c");

        assertEquals(reversedSentence, "c b a");
    }
}
