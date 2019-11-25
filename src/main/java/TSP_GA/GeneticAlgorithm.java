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

    private final int POPULATION_SIZE;
    private final int MUTATION_PROBABILITY;
    private final int CROSSING_PROBABILITY;
    private final int TOURNAMENT_SIZE;
    private final int MAX_ITERATIONS;
    private final int GENOTYPES_TO_CROSSING = 16;
    private final int SOLUTION_SIZE;

    private Double finalRouteLength;
    private Map map;

    private ArrayList<ArrayList<Integer>> population = new ArrayList<>();
    private int iterations = 0;

    GeneticAlgorithm(Map map, int sizeOfPopulation, int mutationProbability, int crossingProbability, int tournamentSize, int max_iterations) {
        this.map = map;
        this.MAX_ITERATIONS = max_iterations;
        this.SOLUTION_SIZE = this.map.getDIMENSION();
        this.POPULATION_SIZE = sizeOfPopulation;
        this.MUTATION_PROBABILITY = mutationProbability;
        this.CROSSING_PROBABILITY = crossingProbability;
        this.TOURNAMENT_SIZE = tournamentSize;
    }

    private ArrayList<ArrayList<Integer>> createPopulation() {
        ArrayList<ArrayList<Integer>> pop = new ArrayList<>();
        int iterator = 0;
        while (iterator != POPULATION_SIZE) {
            pop.add(randomSolution());
            iterator++;
        }
        return pop;
    }

    private Double distanceBetweenCities(City city, City city2) {
        float x = Math.abs(city.getX() - city2.getX());
        float y = Math.abs(city.getY() - city2.getY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Double routeLength(ArrayList<Integer> path) {
        double routeLength = IntStream
                .range(0, SOLUTION_SIZE - 1)
                .filter(i -> i != SOLUTION_SIZE)
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

    private ArrayList<ArrayList<Integer>> sortSolutions(ArrayList<ArrayList<Integer>> population) {
        population.sort(Comparator.comparingDouble(this::routeLength));
        return population;
    }

    private ArrayList<ArrayList<Integer>> drawRandomSolutionsToTournament(ArrayList<ArrayList<Integer>> population) {
        ArrayList<ArrayList<Integer>> tournamentPopulation = new ArrayList<>();
        for (int i = 0; i < GENOTYPES_TO_CROSSING; i++) {
            tournamentPopulation.add(population.get(Helper.generateRandomIntIntRange(0, population.size() - 1)));
        }
        return tournamentPopulation;
    }

    private ArrayList<Integer> tournamentSelection(ArrayList<ArrayList<Integer>> population) {
        sortSolutions(population);
        return population.get(0);
    }

    private ArrayList<ArrayList<Integer>> crossingGenotypes(ArrayList<ArrayList<Integer>> population) {

        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();

        for (int i = 0; i < GENOTYPES_TO_CROSSING - 1; i = i + 2) {
            if (Helper.generateRandomIntIntRange(1, 100) < CROSSING_PROBABILITY) {

                int pointOfCrossing = Helper.generateRandomIntIntRange(1, SOLUTION_SIZE - 1);

                ArrayList<Integer> child1 = crossing(population.get(i), population.get(i + 1), pointOfCrossing);
                ArrayList<Integer> child2 = crossing(population.get(i + 1), population.get(i), pointOfCrossing);

                arrayLists.add(child1);
                arrayLists.add(child2);
            } else {
                arrayLists.add(population.get(i));
                arrayLists.add(population.get(i + 1));
            }
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

    private ArrayList<ArrayList<Integer>> mutationGenotypes(ArrayList<ArrayList<Integer>> population) {

        for (ArrayList<Integer> integers : population) {
            if (Helper.generateRandomIntIntRange(1, 100) < MUTATION_PROBABILITY) {
                int pointOfCrossing = Helper.generateRandomIntIntRange(1, SOLUTION_SIZE - 1);
                int pointOfCrossing2 = Helper.generateRandomIntIntRange(1, SOLUTION_SIZE - 1);
                Collections.swap(integers, pointOfCrossing, pointOfCrossing2);
            }
        }
        return population;
    }

    public ArrayList<Integer> algorithm() {
        population = new ArrayList<>(createPopulation());
        while (iterations < MAX_ITERATIONS) {

            ArrayList<ArrayList<Integer>> tournamentPopulation = new ArrayList<>();
            ArrayList<ArrayList<Integer>> crossedPopulation;
            ArrayList<ArrayList<Integer>> mutatedPopulation;

            while (tournamentPopulation.size() != GENOTYPES_TO_CROSSING) {
                tournamentPopulation.add(tournamentSelection(drawRandomSolutionsToTournament(population)));
            }
            crossedPopulation = crossingGenotypes(tournamentPopulation);
            population = new ArrayList<>(sortSolutions(population).subList(0,POPULATION_SIZE - GENOTYPES_TO_CROSSING));
            population.addAll(crossedPopulation);
            population = sortSolutions(population);
            mutatedPopulation = mutationGenotypes(population);
            population = sortSolutions(mutatedPopulation);
            iterations++;
        }
        finalRouteLength = routeLength(population.get(0));
        return population.get(0);
    }
}

