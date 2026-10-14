package am.gortsup.testing.util;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DateTimeUtilsTest {

    @Test
    public void isLeapYear() {
        assertTrue(DateTimeUtils.isLeapYear(2000));
        assertFalse(DateTimeUtils.isLeapYear(1900));
        assertTrue(DateTimeUtils.isLeapYear(2024));
    }

    @Test
    public void calculateDaysBetween() {
        LocalDate startDate = LocalDate.parse("2024-01-01");
        LocalDate endDate = LocalDate.parse("2024-01-10");

        assertEquals(DateTimeUtils.calculateDaysBetween(startDate, endDate), 9L);
    }

    @Test
    public void isWeekend() {
        assertTrue(DateTimeUtils.isWeekend(LocalDate.parse("2024-06-08")));
        assertFalse(DateTimeUtils.isWeekend(LocalDate.parse("2024-06-10")));
    }

    @Test
    public void calculateBusinessDaysBetween() {
        LocalDate startDate = LocalDate.parse("2024-06-07");
        LocalDate endDate = LocalDate.parse("2024-06-12");

        assertEquals(DateTimeUtils.calculateBusinessDaysBetween(startDate, endDate), 3);
    }

    @Test
    public void addBusinessDays() {
        LocalDate friday = LocalDate.parse("2024-06-07");
        LocalDate nextBusinessDay = DateTimeUtils.addBusinessDays(friday, 1);

        assertEquals(nextBusinessDay, LocalDate.parse("2024-06-10"));
    }

    @Test
    public void doIntervalsOverlap() {
        boolean intervalsOverlap = DateTimeUtils.doIntervalsOverlap(
                LocalDate.parse("2024-01-01"),
                LocalDate.parse("2024-01-05"),
                LocalDate.parse("2024-01-05"),
                LocalDate.parse("2024-01-10")
        );

        assertTrue(intervalsOverlap);
    }

    @Test
    public void isDateInRange() {
        LocalDate date = LocalDate.parse("2024-01-05");
        LocalDate minimumDate = LocalDate.parse("2024-01-01");
        LocalDate maximumDate = LocalDate.parse("2024-01-10");

        assertTrue(DateTimeUtils.isDateInRange(date, minimumDate, maximumDate));
    }
}
