package pl.edu.agh.recommendationsystems.setup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.repository.MovieRepository;

public class MovieDataGenerator {

	@Autowired
	private MovieRepository movieRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MovieDataGenerator.class);

	private static final int MOVIE_COUNT = 40;
	private static final int MAX_VOTE_COUNT_TRESHOLD = 1000; //will be truncated if exceeds, also there will be approximately as much users as maximum vote count for any movie

	public MovieGenerationResult readAndPersistMovies() throws IOException {
		MovieGenerationResult movieGenerationResult = new MovieGenerationResult();
		LOGGER.debug("Generating movies");

		Set<Integer> randomIndexes = getRandomIndexes();
		Iterator<Integer> randomIndexesIterator = randomIndexes.iterator();

		List<MovieRating> movieRatings = new ArrayList<MovieRating>();

		int maxVoteCount = 0;
		String line;
		int currentLineIndex = 0;
		int nextRandomLineIndex = randomIndexesIterator.next();
		BufferedReader ratingsReader = createRatingFileReader();
		while ((line = ratingsReader.readLine()) != null) {
			currentLineIndex++;
			if (nextRandomLineIndex == currentLineIndex) {
				MovieRating movieRating = processMovieRatingLine(line);
				if (movieRating != null) {
					int voteCount = movieRating.getVoteCount();
					if (voteCount > maxVoteCount) {
						maxVoteCount = voteCount;
					}
					movieRatings.add(movieRating);
				}

				if (randomIndexesIterator.hasNext()) {
					nextRandomLineIndex = randomIndexesIterator.next();
				} else {
					break;
				}
			}
		}
		ratingsReader.close();
		movieGenerationResult.setMaxVoteCount(maxVoteCount);
		movieGenerationResult.setMovieRatings(movieRatings);
		return movieGenerationResult;
	}

	public static BufferedReader createRatingFileReader()
			throws FileNotFoundException {
		InputStream inputStream = MovieDataGenerator.class
				.getResourceAsStream("/ratings.list");
		Reader reader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(reader);
		return bufferedReader;
	}

	private Set<Integer> getRandomIndexes() throws IOException {
		BufferedReader reader = createRatingFileReader();
		Set<Integer> randomIndexes = new TreeSet<Integer>();

		int totalCount = 0;

		@SuppressWarnings("unused")
		String line = null;
		while ((line = reader.readLine()) != null) {
			totalCount++;
		}

		Random random = new Random();
		for (int i = 0; i < MOVIE_COUNT; i++) {
			int randomIndex = 1 + random.nextInt(totalCount);
			while (!randomIndexes.add(randomIndex)) {
				if (randomIndex == totalCount) {
					randomIndex = 0;
				} else {
					randomIndex++;
				}
			}
		}

		reader.close();
		return randomIndexes;
	}

	private MovieRating processMovieRatingLine(String line) {
		String movieData[] = line.split("\\s+", 5);
		String voteDistribution = movieData[1].trim();
		int totalVoteCount = Integer.parseInt(movieData[2].trim());
		totalVoteCount = Math.min(MAX_VOTE_COUNT_TRESHOLD, totalVoteCount); // XXX

		@SuppressWarnings("unused")
		float totalRating = Float.parseFloat(movieData[3].trim());
		String title = movieData[4].trim();

		Movie movie = new Movie();
		movie.setTitle(title);
		movie = movieRepository.save(movie);

		MovieRating movieRating = new MovieRating();
		movieRating.setMovie(movie);
		Map<Integer, Integer> votes = movieRating.getVotes();

		fillVotes(votes, voteDistribution, totalVoteCount);

		return movieRating;
	}

	private void fillVotes(Map<Integer, Integer> votes,
			String voteDistribution, int totalVoteCount) {
		int rating = 1;
		for (char c : voteDistribution.toCharArray()) {
			int votesPercentageForRating;
			switch (c) {
			case '.':
				// no votes
				break;
			case '*':
				// 100%
				votes.put(rating, totalVoteCount);
				return;
			default:
				votesPercentageForRating = Integer.parseInt("" + c) * 10 + 5; // i.e.
																				// 3
																				// =
																				// 35%,
																				// 4
																				// =
																				// 45%
																				// ...
				votes.put(
						rating,
						calculateNumberOfVotes(totalVoteCount,
								votesPercentageForRating));
				break;
			}
			rating++;
		}
	}

	public int calculateNumberOfVotes(int totalVoteCount, int votePercentage) {
		return totalVoteCount * votePercentage / 100;
	}

	public void removeAll() {
		movieRepository.deleteAllInBatch();
	}

}
