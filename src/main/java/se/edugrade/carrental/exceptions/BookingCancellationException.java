package se.edugrade.carrental.exceptions;

public class BookingCancellationException extends RuntimeException {
    public BookingCancellationException(String message) {
        super(message);
    }
}
