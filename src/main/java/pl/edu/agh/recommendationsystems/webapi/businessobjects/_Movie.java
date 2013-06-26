package pl.edu.agh.recommendationsystems.webapi.businessobjects;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 24.06.13
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class _Movie {
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;

    public _Movie() {
    }

    public _Movie(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
