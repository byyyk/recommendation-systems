package pl.edu.agh.recommendationsystems.algorithms;

import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;

public interface RecommendationAlgorithm {
	
	public Vote predict(Person person, Movie movie);
	
}
