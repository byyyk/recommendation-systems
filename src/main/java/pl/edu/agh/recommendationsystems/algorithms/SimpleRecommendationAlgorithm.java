package pl.edu.agh.recommendationsystems.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;
import pl.edu.agh.recommendationsystems.persistence.repository.PersonRepository;
import pl.edu.agh.recommendationsystems.persistence.repository.VoteRepository;

public class SimpleRecommendationAlgorithm implements RecommendationAlgorithm {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRecommendationAlgorithm.class);
	
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private VoteRepository voteRepository;
	
	public Vote predict(Person person, Movie movie) {
		LOGGER.debug("Predicting rating for " + person.getUsername() + " on movie " + movie.getTitle());
		throw new UnsupportedOperationException("not yet implemented");
		//TODO implementation
	}

}
