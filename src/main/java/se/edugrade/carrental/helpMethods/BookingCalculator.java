package se.edugrade.carrental.helpMethods;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingCalculator {

    public static int calculateTotalCost(LocalDate startDate, LocalDate endDate, int pricePerDay) {
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) (numberOfDays * pricePerDay);
    }
}
