package pl.edu.agh.recommendationsystems.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.Person;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	public Movie findByTitle(String title);

    @Query("SELECT DISTINCT v.movie FROM Vote v WHERE v.person <> ?")
    public List<Movie> findRatedByOthers(Person person);
	
}
