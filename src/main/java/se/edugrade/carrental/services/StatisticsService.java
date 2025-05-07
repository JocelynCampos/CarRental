package se.edugrade.carrental.services;

/*Erik Edman*/

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.edugrade.carrental.entities.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public Pair<LocalDate, LocalDate> getMostPopularPeriod()
    {
        List<Booking> bookings = bookingService.findAllBookings();

        if(bookings.isEmpty())
        {
            return null;
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

        return bestStart != null && bestEnd != null ? new Pair(bestStart, bestEnd) : null;
    }

    @Override
    public String getMostRentedBrand() {
        return "";
    }

    @Override
    public Map<String, Long> getCarRentalCounts() {
        return Map.of();
    }

    @Override
    public double getAverageBookingCost() {
        return 0;
    }

    @Override
    public Map<String, Integer> getRevenuePerCar() {
        return Map.of();
    }

    @Override
    public Integer getTotalRevenue() {
        return 0;
    }
}
