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
public class _VoteList {
    @SerializedName("votes")
    private List<_Vote> votes = new ArrayList<_Vote>();

    public List<_Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<_Vote> votes) {
        this.votes = votes;
    }
}
