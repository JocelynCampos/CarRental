package se.edugrade.carrental.services;

import java.time.LocalDate;

public interface BookingServiceInterface {
    public int calculateTotalCost(LocalDate startDate, LocalDate endDate, int pricePerDay);
}
