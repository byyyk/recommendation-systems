package pl.edu.agh.recommendationsystems.algorithms.sbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.recommendationsystems.algorithms.RecommendationAlgorithm;
import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;
import pl.edu.agh.recommendationsystems.persistence.repository.VoteRepository;

import java.util.*;

/**
 * Similarity-Based Collaborative Recommendation Algorithm
 *
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 23.06.13
 * Time: 20:17
 * To change this template use File | Settings | File Templates.
 */
public class SBCRecommendationAlgorithm implements RecommendationAlgorithm {

    private final Logger LOGGER = LoggerFactory.getLogger(SBCRecommendationAlgorithm.class);

    private static final int MIN_PERSON_VOTES = 3;
    private static final float MIN_PERSON_SIMILARITY = 0.1f;


    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PersonSimilarityCalculator similarityCalculator;

    @Override
    public Vote predict(Person person, Movie movie) {
        Set<Vote> personVotes = person.getVotes();

        if (personVotes.size() < MIN_PERSON_VOTES) {
            LOGGER.info(String.format("can't predict votes for person with less than %d existing votes", MIN_PERSON_VOTES));
            return null;
        }

        List<Vote> otherPeopleVotes = voteRepository.findByMovie(movie);
        if (otherPeopleVotes.isEmpty()) {
            LOGGER.info("no other people rated this movie - prediction impossible");
            return null;
        }

        Map<Vote, Double> similarVotes = new HashMap<Vote, Double>();

        for (Vote v : otherPeopleVotes) {
            double similarity = similarityCalculator.calculateSimilarity(person, v.getPerson());
            if (similarity >= MIN_PERSON_SIMILARITY) {
                similarVotes.put(v, similarity);
            }
        }

        if (similarVotes.isEmpty()) {
            LOGGER.info("didn't find similar people that rated this movie - prediction impossible");
            return null;
        }

        Vote predictedVote = calculateVote(similarVotes);
        predictedVote.setPerson(person);
        predictedVote.setMovie(movie);

        return predictedVote;
    }

    /**
     * Calculates vote using weighted sum approach
     *
     */
    private Vote calculateVote(Map<Vote, Double> votes) {
        double totalWeight = 0.0;

        double actingScore = 0.0;
        double visualsScore = 0.0;
        double storyScore = 0.0;
        double directionScore = 0.0;

        for (Map.Entry<Vote, Double> entry : votes.entrySet()) {
            double w = entry.getValue();
            totalWeight += w;
            Vote v = entry.getKey();

            actingScore += v.getActingScore() * w;
            visualsScore += v.getVisualsScore() * w;
            storyScore += v.getStoryScore() * w;
            directionScore += v.getDirectionScore() * w;
        }

        Vote vote = new Vote();
        vote.setActingScore((int) Math.round(actingScore / totalWeight));
        vote.setVisualsScore((int) Math.round(visualsScore / totalWeight));
        vote.setStoryScore((int) Math.round(storyScore / totalWeight));
        vote.setDirectionScore((int) Math.round(directionScore / totalWeight));

        return vote;
    }


}
