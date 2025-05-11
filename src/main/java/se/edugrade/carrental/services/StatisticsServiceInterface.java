package se.edugrade.carrental.services;
import org.antlr.v4.runtime.misc.Pair;

import java.time.LocalDate;
import java.util.Map;

/*Erik Edman*/

public interface StatisticsServiceInterface
{
    String getMostPopularPeriod();
    String getMostRentedBrand();
    Map<String, Integer> getCarRentalCounts();
    double getAverageBookingCost();
    Map<String, Integer> getRevenuePerCar();
    Integer getTotalRevenue();
}
