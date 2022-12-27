package pf.bb.model;

import com.google.gson.annotations.Expose;
import pf.bb.Main;
import java.util.ArrayList;

public class Configuration extends EntityWithID {
    //required
    @Expose
    public String writeAccess;

    @Expose
    public String timestampCreated;
    @Expose
    public String timestampLastTouched;

    @Expose
    public User user;
    @Expose
    public String status;

    //optional
    @Expose
    public ArrayList<Article> articles;
    @Expose
    public OrderClass order;

    public static String[] stats = {"ENTWURF", "ABGESCHLOSSEN", "EINKAUF", "STORNO"};

    public static String getUrl() {
        return Main.API_HOST + "/configurations";
    }

    public Configuration(User user) {

        this.writeAccess = null;
        this.user = user;
        this.status = stats[0];
        this.articles = new ArrayList<>();
    }

    public String toString() {
        return String.format(this.getClass().getName() + "[id=%d, dateCreated='%s', lastTouched='%s']", id, timestampCreated, timestampLastTouched);
    }

}
