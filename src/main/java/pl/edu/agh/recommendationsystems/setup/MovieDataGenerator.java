package pl.edu.agh.recommendationsystems.setup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.repository.MovieRepository;

public class MovieDataGenerator {

	@Autowired
	private MovieRepository movieRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MovieDataGenerator.class);

	public MovieGenerationResult readAndPersistMovies() throws IOException {
		MovieGenerationResult movieGenerationResult = new MovieGenerationResult();
		LOGGER.debug("Generating movies");
		BufferedReader ratingsReader = createRatingFileReader();

		List<MovieRating> movieRatings = new ArrayList<MovieRating>();

		int maxVoteCount = 0;
		String line;
		while ((line = ratingsReader.readLine()) != null) {
			MovieRating movieRating = processMovieRatingLine(line);
			if (movieRating != null) {
				int voteCount = movieRating.getVoteCount();
				if (voteCount > maxVoteCount) {
					maxVoteCount = voteCount;
				}
				movieRatings.add(movieRating);
			}
		}
		movieGenerationResult.setMaxVoteCount(maxVoteCount);
		movieGenerationResult.setMovieRatings(movieRatings);
		return movieGenerationResult;
	}

	private BufferedReader createRatingFileReader()
			throws FileNotFoundException {
		InputStream inputStream = this.getClass().getResourceAsStream(
				"/ratings.list");
		Reader reader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(reader);
		return bufferedReader;
	}

	private MovieRating processMovieRatingLine(String line) {
		String movieData[] = line.split("\\s+", 5);
		String voteDistribution = movieData[1].trim();
		int totalVoteCount = Integer.parseInt(movieData[2].trim());
		@SuppressWarnings("unused")
		float totalRating = Float.parseFloat(movieData[3].trim());
		String title = movieData[4].trim();

		if (title.matches(".*\\{.*\\}.*")) {
			return null;
		} else {
			Movie movie = new Movie();
			movie.setTitle(title);
			movie = movieRepository.save(movie);

			MovieRating movieRating = new MovieRating();
			movieRating.setMovie(movie);
			Map<Integer, Integer> votes = movieRating.getVotes();

			fillVotes(votes, voteDistribution, totalVoteCount);

			return movieRating;
		}
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

}
