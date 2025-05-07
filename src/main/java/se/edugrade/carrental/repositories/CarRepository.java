package se.edugrade.carrental.repositories;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.edugrade.carrental.entities.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>
{

    Example<? extends Car> id(Long id);

    Example<? extends Car> id(Long id);
}
