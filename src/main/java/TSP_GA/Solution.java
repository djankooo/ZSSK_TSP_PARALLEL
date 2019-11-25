package TSP_GA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class Solution implements Callable<ArrayList<Integer>> {

    Data data;
    GeneticAlgorithm geneticAlgorithm;

    @Override
    public ArrayList<Integer> call() throws IOException {
        final int SIZE_OF_POPULATION = 100;
        final int MUTATION_PROBABILITY = 5;
        final int CROSSING_PROBABILITY = 97;
        final int TOURNAMENT_SIZE = 8;
        final int MAX_ITERATIONS = 10000;

        final String PATH = "/home/djankooo/projects/java_projects/ZSSK_TSP_PARALLEL/src/main/java/resources/";
        final String FILE_NAME = "berlin52.tsp";

        data = new Data(PATH, FILE_NAME);
        data.checkAndReadFile();

        geneticAlgorithm = new GeneticAlgorithm(data.getMap(), SIZE_OF_POPULATION, MUTATION_PROBABILITY, CROSSING_PROBABILITY, TOURNAMENT_SIZE, MAX_ITERATIONS);
        return geneticAlgorithm.algorithm();
    }
}