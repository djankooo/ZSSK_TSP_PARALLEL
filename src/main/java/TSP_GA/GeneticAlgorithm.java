package TSP_GA;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Getter
@Setter
public class GeneticAlgorithm {

    private double routeLength = 0;
    private Map map = new Map();
    private ArrayList<ArrayList<Integer>> population = new ArrayList<>();
    private int SIZE_OF_POPULATION = 10;

    private Double distanceBetweenCities(City city, City city2) {
        float x = Math.abs(city.getX() - city2.getX());
        float y = Math.abs(city.getY() - city2.getY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Double routeLength(ArrayList<Integer> path) {
        for (int i = 0; i < map.getDIMENSION() - 1; i++) {
            if (i != map.getDIMENSION()) {
                routeLength += distanceBetweenCities(map.getCITIES().get(path.get(i)), map.getCITIES().get(path.get(i + 1)));
            } else {
                routeLength += distanceBetweenCities(map.getCITIES().get(path.get(i)), map.getCITIES().get(path.get(0)));
            }
        }
        return routeLength;
    }

    private ArrayList randomSolution() {
        ArrayList list = IntStream.range(0, map.getDIMENSION()).boxed().collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(list, new Random());
        return list;
    }

    public void createPopulation() {
        int iterator = 0;
        while (iterator != SIZE_OF_POPULATION) {
            population.add(randomSolution());
            iterator++;
        }
    }

    void sortSolutions() {
        // TODO : sortujesz populacje ( ArrayList<ArrayList<Integer>> population ) uzywajac routeLenght() od najkrotszej do najdluzszej
    }

    void tournamentSelection() {
        // TODO : Wybierasz kilka najlepszych rozwiazan metoda turniejowa
    }

    void crossing() {
        // TODO : krzyzowanie wybranych osobnikow / metoda PMX, albo jakos tak ( u Kaplona na slajdach na bank jest )
    }

    void mutation() {
        // TODO : Mutowanie j/w
    }

}

