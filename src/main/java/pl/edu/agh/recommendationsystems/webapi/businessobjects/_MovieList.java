package pl.edu.agh.recommendationsystems.webapi.businessobjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 24.06.13
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class _MovieList {
    @SerializedName("movies")
    private List<_Movie> movies = new ArrayList<_Movie>();

    public List<_Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<_Movie> movies) {
        this.movies = movies;
    }
}
