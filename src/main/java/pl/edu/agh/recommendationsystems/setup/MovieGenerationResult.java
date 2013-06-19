package pl.edu.agh.recommendationsystems.setup;

import java.util.List;

public class MovieGenerationResult {
	private List<MovieRating> movieRatings;
	private int maxVoteCount;

	public List<MovieRating> getMovieRatings() {
		return movieRatings;
	}

	public void setMovieRatings(List<MovieRating> movieRatings) {
		this.movieRatings = movieRatings;
	}

	public int getMaxVoteCount() {
		return maxVoteCount;
	}

	public void setMaxVoteCount(int maxVoteCount) {
		this.maxVoteCount = maxVoteCount;
	}
}
