package pl.edu.agh.recommendationsystems.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    public List<Vote> findByMovie(Movie movie);

    public List<Vote> findByPerson(Person person);
}
