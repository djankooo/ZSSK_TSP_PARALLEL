package TSP_GA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class App {

    private static final int SIZE_OF_POPULATION = 100;
    private static final int MUTATION_PROBABILITY = 5;
    private static final int CROSSING_PROBABILITY = 97;
    private static final int TOURNAMENT_SIZE = 8;
    private static final int MAX_GA_ITERATIONS = 10000;
    private static final int NUMBER_OF_ISLANDS = 4;
    private static final int NUMBER_OF_SOLUTIONS_TO_MIGRATE = 5;
    private static final String PATH = "/home/djankooo/projects/java_projects/ZSSK_TSP_PARALLEL/src/main/java/resources/";
    private static final String FILE_NAME = "berlin52.tsp";
    private static int THREAD_ITERATIONS = 2;
    private static List<Future<ArrayList<ArrayList<Integer>>>> results = new ArrayList<>();
    private static List<GeneticAlgorithm> callables = new ArrayList<>();
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    private static Data data = new Data(PATH, FILE_NAME);

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        prepareData();
        for (int i = 0; i < NUMBER_OF_ISLANDS; i++) {
            createCallables();
        }
        runCallables();

        while (checkIfEveryThreadIsAlive() && THREAD_ITERATIONS >= 0) {

            exchangeSolutions();

            callables.clear();

            results.forEach(result -> {
                try {
                    createCallables(result.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });

            runCallables();

            THREAD_ITERATIONS--;
        }

        results.forEach(r -> {
            try {
                System.out.println(r.get().get(0) + " ->" + routeLength(r.get().get(0)));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        System.out.println("done");
    }

    private static void exchangeSolutions() throws ExecutionException, InterruptedException {
        int solution1 = drawSolutionToMigrate();
        int solution2 = drawSolutionToMigrate();

        while (solution1 == solution2) {
            solution2 = drawSolutionToMigrate();
        }

        ArrayList<ArrayList<Integer>> sol1_1 = new ArrayList<>(results.get(solution1).get().subList(0, NUMBER_OF_SOLUTIONS_TO_MIGRATE));
        ArrayList<ArrayList<Integer>> sol1_2 = new ArrayList<>(results.get(solution1).get().subList(NUMBER_OF_SOLUTIONS_TO_MIGRATE, SIZE_OF_POPULATION));

        ArrayList<ArrayList<Integer>> sol2_1 = new ArrayList<>(results.get(solution2).get().subList(0, NUMBER_OF_SOLUTIONS_TO_MIGRATE));
        ArrayList<ArrayList<Integer>> sol2_2 = new ArrayList<>(results.get(solution2).get().subList(NUMBER_OF_SOLUTIONS_TO_MIGRATE, SIZE_OF_POPULATION));

        results.get(solution1).get().clear();
        results.get(solution1).get().addAll(sol1_1);
        results.get(solution1).get().addAll(sol2_2);

        results.get(solution2).get().clear();
        results.get(solution2).get().addAll(sol2_1);
        results.get(solution2).get().addAll(sol1_2);
    }

    private static int drawSolutionToMigrate() {
        return Helper.generateRandomIntIntRange(0, NUMBER_OF_ISLANDS - 1);
    }

    private static boolean checkIfEveryThreadIsAlive() {
        return results.stream().allMatch(Future::isDone);
    }

    private static void runCallables() throws InterruptedException {
        results = executor.invokeAll(callables);
    }

    private static void createCallables() {
        callables.add(new GeneticAlgorithm(data.getMap(), SIZE_OF_POPULATION, MUTATION_PROBABILITY, CROSSING_PROBABILITY, TOURNAMENT_SIZE, MAX_GA_ITERATIONS));
    }

    private static void createCallables(ArrayList<ArrayList<Integer>> list) {
        callables.add(new GeneticAlgorithm(list, data.getMap(), SIZE_OF_POPULATION, MUTATION_PROBABILITY, CROSSING_PROBABILITY, TOURNAMENT_SIZE, MAX_GA_ITERATIONS));
    }

    private static void prepareData() throws IOException {
        data.checkAndReadFile();
    }


    private static Double distanceBetweenCities(City city, City city2) {
        float x = Math.abs(city.getX() - city2.getX());
        float y = Math.abs(city.getY() - city2.getY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    private static Double routeLength(ArrayList<Integer> path) {
        double routeLength = IntStream
                .range(0, data.getMap().getDIMENSION() - 1)
                .filter(i -> i != data.getMap().getDIMENSION())
                .mapToDouble(i -> distanceBetweenCities(data.getMap().getCITIES().get(path.get(i)), data.getMap().getCITIES().get(path.get(i + 1))))
                .sum();
        routeLength += distanceBetweenCities(data.getMap().getCITIES().get(path.get(data.getMap().getDIMENSION() - 1)), data.getMap().getCITIES().get(path.get(0)));
        return routeLength;
    }

}