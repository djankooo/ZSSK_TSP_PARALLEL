package TSP_GA;

import java.util.ArrayList;

public class App {

    static final int ISLAND_SIZE = 4;
    private static final int NUMBER_OF_ISLANDS = 4;
    private static ArrayList<Thread> threads = new ArrayList<>();
    private static ArrayList<Runnable> runnables = new ArrayList<>();
    private static volatile ArrayList<ArrayList<Integer>> shared = new ArrayList<>();

    public static void main(String[] args) {
        createRunnable();
        runRunnable();

//        while (checkIfEveryThreadIsAlive(threads)) {
//            int sourceSolution = drawSolutionToMigrate();
//            int destinationSolution = drawSolutionToMigrate();
//
//            while (sourceSolution == destinationSolution) {
//                destinationSolution = drawSolutionToMigrate();
//            }
//        }
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

    public static void createRunnable() {
        for (int i = 0; i < NUMBER_OF_ISLANDS; i++) {
            runnables.add(new Solution());
        }
    }

    public static void runRunnable() {
        runnables.forEach(r -> threads.add(new Thread(r)));
        threads.parallelStream().forEach(Thread::start);
    }
}