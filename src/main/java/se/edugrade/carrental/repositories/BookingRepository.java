package se.edugrade.carrental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.edugrade.carrental.entities.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>
{
    //CRUD ingår
}
