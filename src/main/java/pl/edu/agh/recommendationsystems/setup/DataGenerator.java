package pl.edu.agh.recommendationsystems.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;
import pl.edu.agh.recommendationsystems.persistence.repository.MovieRepository;
import pl.edu.agh.recommendationsystems.persistence.repository.PersonRepository;
import pl.edu.agh.recommendationsystems.persistence.repository.VoteRepository;

public class DataGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);
	
	private static final int MOVIE_COUNT = 25;
	private static final int USER_COUNT = 50;
	private static final int MIN_SCORE = 1;
	private static final int MAX_SCORE = 10;
	private static final int USER_MIN_NUMBER_OF_VOTES = 4;
	private static final int USER_MAX_NUMBER_OF_VOTES = 23; //can't be greater than MOVIE_COUNT 

	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private VoteRepository voteRepository;

	@Transactional
	public void generateAndPersist() {
		generateAndPersistMovies();
		generateAndPersistUsers();
		generateAndPersistVotes();
	}

	private void generateAndPersistMovies() {
		LOGGER.debug("Generating movies");
		for (int i = 1; i <= MOVIE_COUNT; i++) {
			Movie movie = new Movie();
			movie.setTitle("Movie Title " + i);
			movieRepository.save(movie);
		}
	}

	private void generateAndPersistUsers() {
		LOGGER.debug("Generating users");
		for (int i = 1; i <= USER_COUNT; i++) {
			Person person = new Person();
			person.setUsername("user" + i);
			personRepository.save(person);
		}
	}

	private void generateAndPersistVotes() {
		LOGGER.debug("Generating votes");
		List<Person> people = personRepository.findAll();
		List<Movie> movies = movieRepository.findAll();
		Random random = new Random();
		
		for (Person person : people) {
			List<Movie> moviesCopy = new ArrayList<Movie>(movies);
			
			int numberOfVotes = USER_MIN_NUMBER_OF_VOTES
					+ random.nextInt(USER_MAX_NUMBER_OF_VOTES
							- USER_MIN_NUMBER_OF_VOTES);
			
			for (int i = 0; i < numberOfVotes; i++) {
				Vote vote = new Vote();
				vote.setActingScore(generateRandomScore());
				vote.setDirectionScore(generateRandomScore());
				vote.setStoryScore(generateRandomScore());
				vote.setVisualsScore(generateRandomScore());
				
				Movie movie = removeRandomMovie(moviesCopy);
				vote.setMovie(movie);
				vote.setPerson(person);
				
				voteRepository.save(vote);
			}
		}
	}
	
	private int generateRandomScore() {
		Random random = new Random();
		return MIN_SCORE + random.nextInt(MAX_SCORE - MIN_SCORE);
	}
	
	private Movie removeRandomMovie(List<Movie> movies) {
		Random random = new Random();
		int size = movies.size();
		return movies.remove(random.nextInt(size));
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		DataGenerator dataGenerator = (DataGenerator) context.getBean("dataGenerator");
		dataGenerator.generateAndPersist();
	}

}
