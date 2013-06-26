package pl.edu.agh.recommendationsystems.webapi.businessobjects;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 24.06.13
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class _Vote {
    @SerializedName("id")
    private long id;
    @SerializedName("person_id")
    private long personId;
    @SerializedName("movie_id")
    private long movieId;
    @SerializedName("acting_score")
    private int actingScore;
    @SerializedName("visuals_score")
    private int visualsScore;
    @SerializedName("story_score")
    private int storyScore;
    @SerializedName("direction_score")
    private int directionScore;

    public _Vote() {
    }

    public _Vote(long id, long personId, long movieId, int actingScore,
                  int visualsScore, int storyScore, int directionScore) {
        this.id = id;
        this.personId = personId;
        this.movieId = movieId;
        this.actingScore = actingScore;
        this.visualsScore = visualsScore;
        this.storyScore = storyScore;
        this.directionScore = directionScore;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public int getActingScore() {
        return actingScore;
    }

    public void setActingScore(int actingScore) {
        this.actingScore = actingScore;
    }

    public int getVisualsScore() {
        return visualsScore;
    }

    public void setVisualsScore(int visualsScore) {
        this.visualsScore = visualsScore;
    }

    public int getStoryScore() {
        return storyScore;
    }

    public void setStoryScore(int storyScore) {
        this.storyScore = storyScore;
    }

    public int getDirectionScore() {
        return directionScore;
    }

    public void setDirectionScore(int directionScore) {
        this.directionScore = directionScore;
    }
}
