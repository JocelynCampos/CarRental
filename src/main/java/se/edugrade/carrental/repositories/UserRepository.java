package se.edugrade.carrental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.edugrade.carrental.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

}
