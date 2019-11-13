package TSP_GA;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Getter
@Setter
public class GeneticAlgorithm {

    private final int SIZE_OF_POPULATION = 10;
    private Map map;
    private ArrayList<ArrayList<Integer>> population = new ArrayList<>();
    private Integer numberOfCities;

    public GeneticAlgorithm(Map map) {
        this.map = map;
        this.numberOfCities = this.map.getDIMENSION();
    }

    public void createPopulation() {
        int iterator = 0;
        while (iterator != SIZE_OF_POPULATION) {
            population.add(randomSolution());
            iterator++;
        }
    }

    private Double distanceBetweenCities(City city, City city2) {
        float x = Math.abs(city.getX() - city2.getX());
        float y = Math.abs(city.getY() - city2.getY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Double routeLength(ArrayList<Integer> path) {
        double routeLength = IntStream
                .range(0, map.getDIMENSION() - 1)
                .filter(i -> i != map.getDIMENSION())
                .mapToDouble(i -> distanceBetweenCities(map.getCITIES().get(path.get(i)), map.getCITIES().get(path.get(i + 1))))
                .sum();
        routeLength += distanceBetweenCities(map.getCITIES().get(path.get(map.getDIMENSION() - 1)), map.getCITIES().get(path.get(0)));
        return routeLength;
    }

    private ArrayList randomSolution() {
        ArrayList list = IntStream
                .range(0, map.getDIMENSION())
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(list, new Random());
        return list;
    }


    void sortSolutions() {
        population.sort(Comparator.comparingDouble(this::routeLength));
    }

    void tournamentSelection() {
        // TODO : Wybierasz kilka najlepszych rozwiazan metoda turniejowa
    }

    ArrayList<ArrayList<Integer>> crossingGenotypes(int crossingProbability, ArrayList<Integer> parent1, ArrayList<Integer> parent2) {

        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();

        if (Helper.generateRandomIntIntRange(1, 100) < crossingProbability) {

            int pointOfCrossing = Helper.generateRandomIntIntRange(1, numberOfCities - 1);

            ArrayList<Integer> child1 = crossing(parent1, parent2, pointOfCrossing);
            ArrayList<Integer> child2 = crossing(parent2, parent1, pointOfCrossing);

            arrayLists.add(child1);
            arrayLists.add(child2);
        } else {
            arrayLists.add(parent1);
            arrayLists.add(parent2);
        }
        return arrayLists;
    }

    private ArrayList<Integer> crossing(ArrayList<Integer> parent1, ArrayList<Integer> parent2, int pointOfCrossing) {
        ArrayList<Integer> child = new ArrayList<>(parent1.subList(0, pointOfCrossing));
        parent2.forEach(i -> {
            if (!child.contains(i)) {
                child.add(i);
            }
        });
        return child;
    }

    ArrayList<Integer> mutationGenotypes(int mutationProbability, ArrayList<Integer> parent) {
        if (Helper.generateRandomIntIntRange(1, 100) < mutationProbability) {
            int pointOfCrossing = Helper.generateRandomIntIntRange(1, numberOfCities - 1);
            int pointOfCrossing2 = Helper.generateRandomIntIntRange(1, numberOfCities - 1);
            Collections.swap(parent, pointOfCrossing, pointOfCrossing2);
        }
        return parent;
    }


}

