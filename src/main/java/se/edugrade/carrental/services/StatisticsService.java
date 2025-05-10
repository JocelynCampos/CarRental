package se.edugrade.carrental.services;

/*Erik Edman*/

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.exceptions.ResourceNotFoundException;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
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

        if(bookings.isEmpty())
        {
            throw new ResourceNotFoundException("Bookings", "List<Booking>", bookings);
        }

        // Step 1: Create a list of events: +1 for start, -1 for end + 1
        TreeMap<LocalDate, Integer> events = new TreeMap<>();
        for(Booking booking : bookings)
        {
            events.merge(booking.getDateWhenPickedUp(), 1, Integer::sum);
            events.merge(booking.getDateWhenTurnedIn().plusDays(1), -1, Integer::sum);
        }

        //Step 2: Sweep through the dates to track active booking and max overlap
        int active = 0;
        int maxActive = 0;
        LocalDate bestStart = null;
        LocalDate bestEnd = null;

        for(Map.Entry<LocalDate, Integer> entry : events.entrySet())
        {
            LocalDate date = entry.getKey();
            active += entry.getValue();

            if(active > maxActive)
            {
                maxActive = active;
                bestStart = date;
                bestEnd = null;
            }
            else if(active < maxActive && bestStart != null && bestEnd == null)
            {
                bestEnd = date.minusDays(1);
            }
        }

        if(bestStart != null && bestEnd == null)
        {
            bestEnd = events.lastKey();
        }

        return "Most popular period: " + bestStart +" to " + bestEnd;
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
    public Map<String, Long> getCarRentalCounts()
    {
        return bookingService.findAllBookings().stream()
                .collect(Collectors.groupingBy(
                        b-> b.getCar().getRegistrationNumber(),
                        Collectors.counting()
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
