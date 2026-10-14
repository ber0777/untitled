package am.gortsup.testing.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MathAndFinanceUtilsTest {

    @Test
    public void calculateTax() {
        double tax = MathAndFinanceUtils.calculateTax(100, 8.25);

        assertEquals(tax, 8.25, 0.001);
    }

    @Test
    public void calculateDiscount() {
        double discountedAmount = MathAndFinanceUtils.calculateDiscount(100, 15);

        assertEquals(discountedAmount, 85.0, 0.001);
    }

    @Test
    public void roundToDecimalPlaces() {
        double roundedValue = MathAndFinanceUtils.roundToDecimalPlaces(1.235, 2);

        assertEquals(roundedValue, 1.24, 0.001);
    }

    @Test
    public void isValidLuhn() {
        assertEquals(MathAndFinanceUtils.isValidLuhn("4532015112830366"), true);
        assertEquals(MathAndFinanceUtils.isValidLuhn("4532015112830367"), false);
    }

    @Test
    public void convertCurrency() {
        double convertedAmount = MathAndFinanceUtils.convertCurrency(12.5, 2);

        assertEquals(convertedAmount, 25.0, 0.001);
    }

    @Test
    public void calculateCompoundInterest() {
        double futureValue = MathAndFinanceUtils.calculateCompoundInterest(1000, 12, 12, 1);

        assertEquals(futureValue, 1126.83, 0.01);
    }

    @Test
    public void calculateMonthlyInstallment() {
        double monthlyPayment = MathAndFinanceUtils.calculateMonthlyInstallment(1200, 0, 12);

        assertEquals(monthlyPayment, 100.0, 0.001);
    }

    @Test
    public void calculatePercentageChange() {
        double percentageChange = MathAndFinanceUtils.calculatePercentageChange(50, 60);

        assertEquals(percentageChange, 20.0, 0.001);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void calculateTaxRejectsNegativeAmount() {
        MathAndFinanceUtils.calculateTax(-1, 10);
    }
}
