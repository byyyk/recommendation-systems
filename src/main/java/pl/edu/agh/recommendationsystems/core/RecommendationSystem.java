package pl.edu.agh.recommendationsystems.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.recommendationsystems.algorithms.RecommendationAlgorithm;
import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;
import pl.edu.agh.recommendationsystems.persistence.repository.MovieRepository;
import pl.edu.agh.recommendationsystems.persistence.repository.PersonRepository;

public class RecommendationSystem {

	private final Logger LOGGER = LoggerFactory
			.getLogger(RecommendationSystem.class);

	@Autowired
    @Qualifier("sbcAlgorithm")
	private RecommendationAlgorithm algorithm;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private MovieRepository movieRepository;

	@Transactional
	public void doWork() {
		LOGGER.debug("Recommendation system started");
        long time = System.currentTimeMillis();
		List<Movie> movies = movieRepository.findAll();
		for (Person person : personRepository.findAll()) {
			List<Movie> notRatedMovies = new ArrayList<Movie>(movies);
			for (Vote vote : person.getVotes()) {
				Movie ratedMovie = vote.getMovie();
				notRatedMovies.remove(ratedMovie);
			}

			LOGGER.debug("Predicting scores for " + person.getUsername()
					+ " on movies: "
					+ Arrays.toString(notRatedMovies.toArray(new Movie[0])));

			for (Movie movie : notRatedMovies) {
				Vote predictedVote = algorithm.predict(person, movie);
				// TODO log results
			}
		}
        time = System.currentTimeMillis() - time;
        LOGGER.info("\n\t--------\n\n\trecommendations calculated in: " + time/1000 + " sec\n\n\t--------");
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		RecommendationSystem recommendationSystem = (RecommendationSystem) context
				.getBean("recommendationSystem");
		recommendationSystem.doWork();
	}

}
