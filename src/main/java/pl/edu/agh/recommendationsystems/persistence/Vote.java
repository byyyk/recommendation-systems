package pl.edu.agh.recommendationsystems.persistence;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Check;

@Entity
@Table(name = "vote", uniqueConstraints=@UniqueConstraint(columnNames={"person_id", "movie_id"}))
public class Vote {

	private long id;
	
	private Person person;
	private Movie movie;
	
	private int storyScore;
	private int actingScore;
	private int visualsScore;
	private int directionScore;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="vote_id", unique = true, nullable = false)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "movie_id", nullable = false)
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@Column(name = "vote_storyscore", nullable = false)
	public int getStoryScore() {
		return storyScore;
	}
	
	public void setStoryScore(int storyScore) {
		this.storyScore = storyScore;
	}
	
	@Column(name = "vote_actingscore", nullable = false)
	public int getActingScore() {
		return actingScore;
	}

	public void setActingScore(int actingScore) {
		this.actingScore = actingScore;
	}
	
	@Column(name = "vote_visualsscore", nullable = false)
	public int getVisualsScore() {
		return visualsScore;
	}
	
	public void setVisualsScore(int visualsScore) {
		this.visualsScore = visualsScore;
	}
	
	@Column(name = "vote_directionscore", nullable = false)
	@Check(constraints = "vote_directionscore <= 10 AND vote_directionscore > 0")
	public int getDirectionScore() {
		return directionScore;
	}
	
	public void setDirectionScore(int directionScore) {
		this.directionScore = directionScore;
	}
	
}
