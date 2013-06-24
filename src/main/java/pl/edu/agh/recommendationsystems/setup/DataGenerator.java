package pl.edu.agh.recommendationsystems.setup;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class DataGenerator {

	@Autowired
	private MovieDataGenerator movieDataGenerator;
	@Autowired
	private PersonDataGenerator personDataGenerator;
	@Autowired
	private VoteDataGenerator voteDataGenerator;

	@Transactional
	public void generateAndPersist() throws IOException {
		removeAll();
		
		MovieGenerationResult movieGenerationResult = movieDataGenerator.readAndPersistMovies();
		List<MovieRating> movieRatings = movieGenerationResult.getMovieRatings();
		int maxVoteCount = movieGenerationResult.getMaxVoteCount();
		
		personDataGenerator.generateAndPersistUsers(maxVoteCount);

		voteDataGenerator.generateAndPersistVotes(movieRatings);
	}

	private void removeAll() {
		voteDataGenerator.removeAll();
		personDataGenerator.removeAll();
		movieDataGenerator.removeAll();
	}

	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		DataGenerator dataGenerator = (DataGenerator) context
				.getBean("dataGenerator");
		dataGenerator.generateAndPersist();
	}

}
