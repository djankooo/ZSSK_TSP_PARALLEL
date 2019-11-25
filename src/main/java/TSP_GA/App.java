package TSP_GA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {

    private static final int NUMBER_OF_ISLANDS = 100;
    private static List<Future<ArrayList<Integer>>> results = new ArrayList<>();
    private static List<Solution> callables = new ArrayList<>();
    private static ExecutorService executor = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        for (int i = 0; i < NUMBER_OF_ISLANDS; i++) {
            callables.add(new Solution());
        }

        results = executor.invokeAll(callables);

        while (results.stream().allMatch(Future::isDone)) {
            for (Future r : results) {
                System.out.println(r.get());
            }
            break;
        }
    }

    private static int drawSolutionToMigrate() {
        return Helper.generateRandomIntIntRange(0, NUMBER_OF_ISLANDS - 1);
    }

    public static boolean checkIfEveryThreadIsAlive(ArrayList<Thread> threads) {
        for (Thread t : threads) {
            if (!t.isAlive()) return false;
        }
        return true;
    }
}