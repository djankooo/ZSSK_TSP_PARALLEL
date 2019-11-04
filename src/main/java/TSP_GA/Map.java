package TSP_GA;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Map {
    private String NAME;
    private String TYPE;
    private String COMMENT;
    private Integer DIMENSION;
    private String EDGE_WEIGHT_TYPE;
    private ArrayList <City> CITIES;

    public Map() {
        this.CITIES = new ArrayList<>();
    }

    public void addCity(City city){
        CITIES.add(city);
    }
}
