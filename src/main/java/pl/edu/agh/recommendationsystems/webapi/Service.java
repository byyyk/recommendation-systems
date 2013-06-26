package pl.edu.agh.recommendationsystems.webapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pl.edu.agh.recommendationsystems.algorithms.RecommendationAlgorithm;
import pl.edu.agh.recommendationsystems.persistence.Movie;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.Vote;
import pl.edu.agh.recommendationsystems.persistence.repository.MovieRepository;
import pl.edu.agh.recommendationsystems.persistence.repository.PersonRepository;
import pl.edu.agh.recommendationsystems.persistence.repository.VoteRepository;
import pl.edu.agh.recommendationsystems.webapi.businessobjects.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 24.06.13
 * Time: 20:00
 * To change this template use File | Settings | File Templates.
 */

@Path("/")
public class Service {

    private final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    @Qualifier("sbcAlgorithm")
    private RecommendationAlgorithm recommendationAlgorithm;

    public Service() {
        Server.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
    }

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("personId") String personId) {
        LOGGER.info("login() : personId:" + personId);
        return getOkJsonResponse(new _Id(Long.valueOf(personId)));
    }

    @GET
    @Path("/movie")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(
            @DefaultValue("0") @QueryParam("id") long id,
            @DefaultValue("") @QueryParam("title") String title) {

        Movie movie;
        if (id > 0) {
            movie = movieRepository.findOne(id);
        } else if (!title.isEmpty())  {
            movie = movieRepository.findByTitle(title);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (movie == null) {
            return Response.noContent().build();
        }
        return getOkJsonResponse(new _Movie(movie.getId(), movie.getTitle()));
    }

    @GET
    @Path("/movies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies() {
        _MovieList resultList = new _MovieList();
        for (Movie m : movieRepository.findAll()) {
            resultList.getMovies().add(new _Movie(m.getId(), m.getTitle()));
        }
        return getOkJsonResponse(resultList);
    }

    @GET
    @Path("/person/{personId}/votes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVotes(@PathParam("personId") long personId) {
        Person person = personRepository.findOne(personId);
        _VoteList resultList = new _VoteList();

        List<Vote> votes = voteRepository.findByPerson(person);
        for (Vote v : votes) {
             _Vote _vote = new _Vote(v.getId(), personId, v.getMovie().getId(),
                     v.getActingScore(), v.getVisualsScore(), v.getStoryScore(), v.getDirectionScore());
            resultList.getVotes().add(_vote);
        }
        return getOkJsonResponse(resultList);
    }

    @GET
    @Path("/person/{personId}/recommendations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecommendations(@PathParam("personId") long personId) {
        long time = System.currentTimeMillis();
        Person person = personRepository.findOne(personId);
        _VoteList resultList = new _VoteList();

        List<Movie> moviesNotRated = movieRepository.findRatedByOthers(person);

        for (Movie m : moviesNotRated) {
            Vote v =recommendationAlgorithm.predict(person, m);
            if (v != null) {
                _Vote _vote = new _Vote(v.getId(), personId, v.getMovie().getId(),
                        v.getActingScore(), v.getVisualsScore(), v.getStoryScore(), v.getDirectionScore());
                resultList.getVotes().add(_vote);
            }
        }
        time = System.currentTimeMillis() - time;
        LOGGER.info("\n\t--------\n\n\trecommendations calculated in: " + time/1000 + " sec\n\n\t--------");
        return getOkJsonResponse(resultList);
    }

    @POST
    @Path("/votes")
    public Response submitVote(@HeaderParam("personId") String personId, String voteJson) {
        Gson gson = new Gson();
        _Vote _vote = gson.fromJson(voteJson, _Vote.class);

        if (_vote.getPersonId() != Long.valueOf(personId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Person person = personRepository.findOne(_vote.getPersonId());
        if (person == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Movie movie = movieRepository.findOne(_vote.getMovieId());
        if (movie == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Vote vote = new Vote();
        vote.setPerson(person);
        vote.setMovie(movie);
        vote.setActingScore(_vote.getActingScore());
        vote.setStoryScore(_vote.getStoryScore());
        vote.setVisualsScore(_vote.getVisualsScore());
        vote.setDirectionScore(_vote.getDirectionScore());
        Vote v = voteRepository.saveAndFlush(vote);
        return getOkJsonResponse(new _Id(v.getId()));
    }

    private static Response getOkJsonResponse(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String responseBody = gson.toJson(obj);
        return Response.ok(responseBody).build();
    }

}
