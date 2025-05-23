package se.edugrade.carrental.services;


import se.edugrade.carrental.entities.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingServiceInterface {
    Booking createBooking(Long userId, Long carId, LocalDate startDate, LocalDate endDate);
    void cancelUserBooking(Long bookingId);
    List<Booking> getUserActiveOrders(Long userId);
    List<Booking> getUserExpiredBookings(Long userId);
    List<Booking> getAdminActiveOrders();
    List<Booking> getAdminExpiredBookings();
    void deleteBookingAdmin(Long bookingId, boolean isAdmin);
    void deleteBookingsBeforeDate(LocalDate targetDate);
    Booking getBookingById(Long bookingId);
    List<Booking> findAllBookings();

    }
