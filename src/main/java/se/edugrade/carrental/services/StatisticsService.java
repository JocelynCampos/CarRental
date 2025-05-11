package se.edugrade.carrental.services;

/*Erik Edman*/

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.exceptions.ResourceNotFoundException;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class StatisticsService implements StatisticsServiceInterface
{
    private BookingService bookingService;

    @Autowired
    public StatisticsService(BookingService bookingService)
    {
        this.bookingService = bookingService;
    }

    @Override
    public String getMostPopularPeriod()
    {
        List<Booking> bookings = bookingService.findAllBookings();
        if (bookings.isEmpty())
        {
            throw new ResourceNotFoundException("Bookings", "List<Bookings>", bookings);
        }

        Map<LocalDate, Integer> dateCounts = new HashMap<>();

        // Count active bookings for each day
        for (Booking booking : bookings)
        {
            LocalDate start = booking.getDateWhenPickedUp();
            LocalDate end = booking.getDateWhenTurnedIn();

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1))
            {
                dateCounts.put(date, dateCounts.getOrDefault(date, 0) + 1);
            }
        }

        // Find max count
        int max = dateCounts.values().stream().max(Integer::compareTo).orElse(0);

        // Find all dates with max activity
        List<LocalDate> maxDates = dateCounts.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        // Group consecutive maxDates to find longest period
        LocalDate bestStart = null;
        LocalDate bestEnd = null;
        LocalDate tempStart = null;

        for (int i = 0; i < maxDates.size(); i++)
        {
            LocalDate current = maxDates.get(i);

            if (tempStart == null)
            {
                tempStart = current;
            }

            boolean isLast = i == maxDates.size() - 1;
            boolean isGap = !isLast && !maxDates.get(i + 1).equals(current.plusDays(1));

            if (isGap || isLast)
            {
                LocalDate tempEnd = current;
                if (bestStart == null || (tempEnd.toEpochDay() - tempStart.toEpochDay()) > (bestEnd.toEpochDay() - bestStart.toEpochDay()))
                {
                    bestStart = tempStart;
                    bestEnd = tempEnd;
                }
                tempStart = null;
            }
        }

        return "Most popular period: " + bestStart + " to " + bestEnd;
    }


    @Override
    public String getMostRentedBrand()
    {
        return bookingService.findAllBookings().stream()
                .collect(Collectors.groupingBy(b -> b.getCar().getBrand(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
    }

    @Override
    public Map<String, Integer> getCarRentalCounts()
    {
        return bookingService.findAllBookings().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getCar().getRegistrationNumber(),
                        Collectors.reducing(0, e -> 1, Integer::sum)
                ));
    }

    @Override
    public double getAverageBookingCost()
    {
        double nrOfBookings = bookingService.findAllBookings().size();
        double cost = 0;

        List<Booking> bookings = bookingService.findAllBookings();

        for(Booking booking : bookings)
        {
            cost += booking.getTotalCost();
        }

        return cost / nrOfBookings;
    }

    @Override
    public Map<String, Integer> getRevenuePerCar()
    {
        return bookingService.findAllBookings().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getCar().getRegistrationNumber(),
                        Collectors.summingInt(Booking::getTotalCost)
                ));
    }

    @Override
    public Integer getTotalRevenue()
    {
        return bookingService.findAllBookings().stream()
                .map(Booking::getTotalCost)
                .reduce(0, Integer::sum);
    }
}
