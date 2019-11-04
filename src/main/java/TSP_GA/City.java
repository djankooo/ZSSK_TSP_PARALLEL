package TSP_GA;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class City {

    private Float X;
    private Float Y;

    public double distanceToCity(City city) {
        Float x = Math.abs(getX() - city.getX());
        Float y = Math.abs(getY() - city.getY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
}
