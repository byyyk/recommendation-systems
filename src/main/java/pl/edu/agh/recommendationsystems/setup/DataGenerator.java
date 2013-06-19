package pl.edu.agh.recommendationsystems.setup;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataGenerator {

	@Autowired
	private MovieDataGenerator movieDataGenerator;
	@Autowired
	private PersonDataGenerator personDataGenerator;
	@Autowired
	private VoteDataGenerator voteDataGenerator;


	public void generateAndPersist() throws IOException {
		MovieGenerationResult movieGenerationResult = movieDataGenerator.readAndPersistMovies();
		List<MovieRating> movieRatings = movieGenerationResult.getMovieRatings();
		int maxVoteCount = movieGenerationResult.getMaxVoteCount();
		
		personDataGenerator.generateAndPersistUsers(maxVoteCount);

		voteDataGenerator.generateAndPersistVotes(movieRatings);
	}

	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		DataGenerator dataGenerator = (DataGenerator) context
				.getBean("dataGenerator");
		dataGenerator.generateAndPersist();
	}

}
