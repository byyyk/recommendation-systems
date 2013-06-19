package pl.edu.agh.recommendationsystems.setup;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;
import pl.edu.agh.recommendationsystems.persistence.repository.PersonRepository;
import pl.edu.agh.recommendationsystems.persistence.repository.VoteRepository;

public class VoteDataGenerator {
	
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private VoteRepository voteRepository;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(VoteDataGenerator.class);
	
	public void generateAndPersistVotes(List<MovieRating> movieRatings) {
		LOGGER.debug("Generating votes");
		List<Person> people = personRepository.findAll();

		for (MovieRating movieRating : movieRatings) {
			int totalVoteCount = movieRating.getVoteCount();
			Set<Integer> randomIndexes = generateDistinctRandomIndexes(
					totalVoteCount, people.size());
			Iterator<Integer> randomIndexesIterator = randomIndexes.iterator();
			for (Entry<Integer, Integer> ratingVotes : movieRating.getVotes()
					.entrySet()) {
				int rating = ratingVotes.getKey();
				int ratingVoteCount = ratingVotes.getValue();

				for (int i = 0; i < ratingVoteCount; i++) {
					Person person = people.get(randomIndexesIterator.next());

					Vote vote = new Vote();
					vote.setPerson(person);
					vote.setMovie(movieRating.getMovie());

					fillRandomCriteriaVotes(vote, rating);

					voteRepository.save(vote);
				}
			}
		}
	}

	private Set<Integer> generateDistinctRandomIndexes(int howMany,
			int randomIndexUpperExclusiveBound) {
		Set<Integer> randomIndexes = new LinkedHashSet<Integer>();
		Random random = new Random();
		for (int i = 0; i < howMany; i++) {
			int randomIndex = random.nextInt(randomIndexUpperExclusiveBound);
			while (!randomIndexes.add(randomIndex)) {
				if (randomIndex == randomIndexUpperExclusiveBound - 1) {
					randomIndex = 0;
				} else {
					randomIndex++;
				}
			}
		}
		return randomIndexes;
	}

	private void fillRandomCriteriaVotes(Vote vote, int rating) {
		// TODO
		vote.setActingScore(rating);
		vote.setDirectionScore(rating);
		vote.setStoryScore(rating);
		vote.setVisualsScore(rating);
	}
}
