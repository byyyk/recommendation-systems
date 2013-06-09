package pl.edu.agh.recommendationsystems.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.agh.recommendationsystems.persistence.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
