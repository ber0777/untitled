package am.gortsup.testing.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ValidationUtilsTest {

    @DataProvider
    public Object[][] emailCases() {
        return new Object[][] {
                {"qa@example.com", true},
                {"a..b@example.com", false},
                {"", false},
                {null, false}
        };
    }

    @Test(dataProvider = "emailCases")
    public void isValidEmail(String email, boolean expectedResult) {
        assertEquals(ValidationUtils.isValidEmail(email), expectedResult);
    }

    @Test
    public void isValidPassword() {
        assertTrue(ValidationUtils.isValidPassword("Strong1!"));
        assertFalse(ValidationUtils.isValidPassword("weak"));
    }

    @Test
    public void isValidPhoneNumber() {
        assertTrue(ValidationUtils.isValidPhoneNumber("+1 (555) 123-4567"));
        assertFalse(ValidationUtils.isValidPhoneNumber("12"));
    }

    @Test
    public void isValidPostalCode() {
        assertTrue(ValidationUtils.isValidPostalCode("12345", "US"));
        assertFalse(ValidationUtils.isValidPostalCode("12", "US"));
    }

    @Test
    public void isValidIban() {
        assertTrue(ValidationUtils.isValidIban("GB82WEST12345698765432"));
        assertFalse(ValidationUtils.isValidIban("GB00WEST12345698765432"));
    }

    @Test
    public void isValidIpAddress() {
        assertTrue(ValidationUtils.isValidIpAddress("192.168.1.1"));
        assertFalse(ValidationUtils.isValidIpAddress("256.1.1.1"));
    }

    @Test
    public void isValidUrl() {
        assertTrue(ValidationUtils.isValidUrl("https://example.com/path"));
        assertFalse(ValidationUtils.isValidUrl("ftp://example.com"));
    }

    @Test
    public void sanitizeInput() {
        assertEquals(ValidationUtils.sanitizeInput("<a&\"'>"), "&lt;a&amp;&quot;&#x27;&gt;");
        assertEquals(ValidationUtils.sanitizeInput(null), "");
    }

    @Test
    public void isPositiveNumber() {
        assertTrue(ValidationUtils.isPositiveNumber(0.1));
        assertFalse(ValidationUtils.isPositiveNumber(0));
        assertFalse(ValidationUtils.isPositiveNumber(null));
    }
}
