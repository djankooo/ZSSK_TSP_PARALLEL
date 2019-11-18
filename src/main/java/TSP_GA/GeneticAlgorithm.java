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
class GeneticAlgorithm {

    private final int SIZE_OF_POPULATION = 10;
    private Map map;
    private ArrayList<ArrayList<Integer>> population = new ArrayList<>();
    private Integer numberOfCities;
    private Integer tournamentSize = 4;

    GeneticAlgorithm(Map map) {
        this.map = map;
        this.numberOfCities = this.map.getDIMENSION();
    }

    void createPopulation() {
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


    ArrayList<ArrayList<Integer>> sortSolutions(ArrayList<ArrayList<Integer>> population) {
        population.sort(Comparator.comparingDouble(this::routeLength));
        return population;
    }

//    public ArrayList<Integer> tournamentSelection(ArrayList<ArrayList<Integer>> pop) {
//
//        ArrayList<ArrayList<Integer>> tournamentRouts = new ArrayList<>(tournamentSize);
//        ArrayList routes = new ArrayList();
//        int length = pop.size();
//        if (length < tournamentSize) return null;
//        int j=0;
//        for (int i = length - 1; i >= length - tournamentSize; --i) {
//            int randomId = (int) (Math.random() * SIZE_OF_POPULATION);
//            tournamentRouts.add(j,pop.get(randomId));
//            j++;
//        }
//        int k=0; // Po co k?
//        for (ArrayList<Integer> integers : tournamentRouts) {
//            routes.add(routeLength(integers));
//        }
//        Double winner = (Double) Collections.min(routes);
//        for (int i = 0; i < tournamentSize; i++) {
//            if (routes.get(i) == winner) {
//                System.out.println(winner);
//                return tournamentRouts.get(i);
//
//            }
//        }
//        return null;
//    }

    ArrayList<Integer> tournamentSelection(ArrayList<ArrayList<Integer>> pop) {
        ArrayList<ArrayList<Integer>> winnerPopulation = new ArrayList<>();

        System.out.println("population - > " + pop);

        for (int i = 0; i < pop.size() - 1; i = i + 2) {

            System.out.println("[1] -> " + pop.get(i) + " -> " + routeLength(pop.get(i)).intValue());
            System.out.println("[2] -> " + pop.get(i + 1) + " -> " + routeLength(pop.get(i + 1)).intValue());

            if ((routeLength(pop.get(i)) < routeLength(pop.get(i + 1)))) {
                winnerPopulation.add(pop.get(i));
                System.out.println("Dodano [1] -> " + pop.get(i) + " -> " + routeLength(pop.get(i)).intValue());
            } else {
                winnerPopulation.add(pop.get(i + 1));
                System.out.println("Dodano [2] -> " + pop.get(i + 1) + " -> " + routeLength(pop.get(i + 1)).intValue());

            }
        }

        System.out.println("winnerPopulation - > " + winnerPopulation);
        return (winnerPopulation.size() > 1) ? tournamentSelection(winnerPopulation) : winnerPopulation.get(0);
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

