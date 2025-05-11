package se.edugrade.carrental.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Booking(int totalCost, LocalDate dateWhenPickedUp, LocalDate dateWhenTurnedIn, Car car, User user) {
        this.totalCost = totalCost;
        this.dateWhenPickedUp = dateWhenPickedUp;
        this.dateWhenTurnedIn = dateWhenTurnedIn;
        this.car = car;
        this.user = user;
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


    public Car getCar() { return car; }

    public void setCar(Car car) { this.car = car; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

}
