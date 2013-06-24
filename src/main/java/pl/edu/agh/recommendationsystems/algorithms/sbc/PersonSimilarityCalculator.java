package pl.edu.agh.recommendationsystems.algorithms.sbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;
import pl.edu.agh.recommendationsystems.persistence.repository.VoteRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 23.06.13
 * Time: 20:19
 * To change this template use File | Settings | File Templates.
 */
public class PersonSimilarityCalculator {

    private final Logger LOGGER = LoggerFactory.getLogger(PersonSimilarityCalculator.class);

    private static final int MIN_COMMON_VOTES = 2;

    /**
     * Calculates how similar 2 people are by comparing their votes on the same movies
     *
     * @return normalized [0,1] level of similarity (0 - no similarity, 1 - max similarity)
     */
    public double calculateSimilarity(Person person1, Person person2) {
        Set<VotePair> commonVotes = findCommonVotes(person1, person2);
        if (commonVotes.size() < MIN_COMMON_VOTES) {
            LOGGER.info(String.format("persons id:%d and id:%d do not have enough commonly rated movies " +
                    "to calculate similarity", person1.getId(), person2.getId()));
            return 0f;
        }

        double distance = 0.0;  // average distance between ratings
        for (VotePair cv : commonVotes) {
            distance += DistanceCalculator.getDistance(getDetailVotes(cv.vote1), getDetailVotes(cv.vote2));
        }
        distance = distance / commonVotes.size();

        return 1 / (1 + distance);
    }

    /**
     * Finds movies that both people have rated
     *
     * @return Set of objects containing votes from both people for the same movie
     */
    private Set<VotePair> findCommonVotes(Person person1, Person person2) {
        Set<Vote> votes1 = person1.getVotes();
        Set<Vote> votes2 = person2.getVotes();
        Set<VotePair> commonVotes = new HashSet<VotePair>();
        for (Vote v1 : votes1) {
            long movieId = v1.getMovie().getId();
            for (Vote v2 : votes2) {
                if (v2.getMovie().getId() == movieId) {
                    commonVotes.add(new VotePair(v1, v2));
                    break;
                }
            }
        }
        return commonVotes;
    }

    private int[] getDetailVotes(Vote vote) {
        return new int[] {
                vote.getActingScore(),
                vote.getDirectionScore(),
                vote.getStoryScore(),
                vote.getVisualsScore()
        };
    }

    private static class VotePair {
        Vote vote1;
        Vote vote2;
        private VotePair(Vote vote1, Vote vote2) {
            this.vote1 = vote1;
            this.vote2 = vote2;
        }
    }
}
