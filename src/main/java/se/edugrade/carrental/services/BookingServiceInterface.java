package se.edugrade.carrental.services;

import se.edugrade.carrental.entities.Customer;

import java.time.LocalDate;

public interface BookingServiceInterface {
    public int calculateTotalCost(LocalDate startDate, LocalDate endDate, int pricePerDay);
}
