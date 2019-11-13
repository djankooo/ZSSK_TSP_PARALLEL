package TSP_GA;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
class Map {
    private String NAME;
    private String TYPE;
    private String COMMENT;
    private Integer DIMENSION;
    private String EDGE_WEIGHT_TYPE;
    private ArrayList<City> CITIES;

    Map() {
        this.CITIES = new ArrayList<>();
    }

    void addCity(City city) {
        CITIES.add(city);
    }
}
