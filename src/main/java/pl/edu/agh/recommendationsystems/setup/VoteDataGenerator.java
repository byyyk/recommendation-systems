package pl.edu.agh.recommendationsystems.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
		LOGGER.info("Generating criteria scores for rating: " + rating);
		
		Random random = new Random();
		List<Integer> criterias = new ArrayList<Integer>();
		for (int i = 0; i < 4; i ++) {
			criterias.add(1 + random.nextInt(10));
		}

		final double eps = 0.2d;
		double error = average(criterias) - rating;
		while (Math.abs(error) > eps) {
			if (error > 0) {
				Integer max = Collections.max(criterias);
				criterias.remove(max);
				criterias.add(1 + random.nextInt(10));
			} else if (error < 0) {
				Integer min = Collections.min(criterias);
				criterias.remove(min);
				criterias.add(1 + random.nextInt(10));
			}
			error = average(criterias) - rating;
		}
		
		LOGGER.info("Calculated scores: " + Arrays.toString(criterias.toArray()) + ", average: " + average(criterias) + ", diff: " + error);

		vote.setActingScore(criterias.remove(0));
		vote.setDirectionScore(criterias.remove(0));
		vote.setStoryScore(criterias.remove(0));
		vote.setVisualsScore(criterias.remove(0));
	}
	
	private double average(List<Integer> list) {
		if (list.size() == 0) {
			return 0;
		}
		
		int sum = 0;
		for (int i : list) {
			sum += i;
		}
		return ((double) sum) / list.size();
	}

	public void removeAll() {
		voteRepository.deleteAllInBatch();
	}
}
