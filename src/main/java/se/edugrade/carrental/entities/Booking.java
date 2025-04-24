package se.edugrade.carrental.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

/*Erik Edman*/

@Entity
public class Booking
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int totalCost;

    @Column
    private LocalDate dateWhenPickedUp;

    @Column
    private LocalDate dateWhenTurnedIn;

    public Booking() {
    }

    public Booking(int totalCost, LocalDate dateWhenPickedUp, LocalDate dateWhenTurnedIn) {
        this.totalCost = totalCost;
        this.dateWhenPickedUp = dateWhenPickedUp;
        this.dateWhenTurnedIn = dateWhenTurnedIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDate getDateWhenPickedUp() {
        return dateWhenPickedUp;
    }

    public void setDateWhenPickedUp(LocalDate dateWhenPickedUp) {
        this.dateWhenPickedUp = dateWhenPickedUp;
    }

    public LocalDate getDateWhenTurnedIn() {
        return dateWhenTurnedIn;
    }

    public void setDateWhenTurnedIn(LocalDate dateWhenTurnedIn) {
        this.dateWhenTurnedIn = dateWhenTurnedIn;
    }
}
