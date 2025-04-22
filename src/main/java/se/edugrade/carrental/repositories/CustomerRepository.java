package se.edugrade.carrental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.edugrade.carrental.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>
{

}
