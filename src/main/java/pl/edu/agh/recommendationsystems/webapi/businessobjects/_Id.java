package pl.edu.agh.recommendationsystems.webapi.businessobjects;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 24.06.13
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
public class _Id {
    @SerializedName("id")
    private long id;

    public _Id() {
    }

    public _Id(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
