package pl.edu.agh.recommendationsystems.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.agh.recommendationsystems.persistence.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	public Movie findByTitle(String title);
	
}
