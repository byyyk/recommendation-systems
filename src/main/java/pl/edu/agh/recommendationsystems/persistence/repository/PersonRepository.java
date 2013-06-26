package pl.edu.agh.recommendationsystems.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.agh.recommendationsystems.persistence.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findPersonByUsernameAndPassword(String username, String password);
}
