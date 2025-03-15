package models;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("zips")
public class Zip {

    @Id
    private String id;
    private String city;
    private List<Double> loc;
    private long pop;
    private String state;

    public Zip() {
    }

    public Zip(String id, String city, List<Double> loc, long pop, String state) {
        this.id = id;
        this.city = city;
        this.loc = loc;
        this.pop = pop;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Double> getLoc() {
        return loc;
    }

    public void setLoc(List<Double> loc) {
        this.loc = loc;
    }

    public long getPop() {
        return pop;
    }

    public void setPop(long pop) {
        this.pop = pop;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
