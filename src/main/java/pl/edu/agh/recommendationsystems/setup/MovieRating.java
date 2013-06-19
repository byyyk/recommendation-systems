package pl.edu.agh.recommendationsystems.setup;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pl.edu.agh.recommendationsystems.persistence.Movie;

public class MovieRating {
	private Movie movie;
	private Map<Integer, Integer> votes = new HashMap<Integer, Integer>();

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie= movie;
	}

	public Map<Integer, Integer> getVotes() {
		return votes;
	}

	public void setVotes(Map<Integer, Integer> votes) {
		this.votes = votes;
	}
	
	public int getVoteCount() {
		int voteCount = 0;
		for (Entry<Integer, Integer> entry : votes.entrySet()) {
			voteCount += entry.getValue();
		}
		return voteCount;
	}
	
}
