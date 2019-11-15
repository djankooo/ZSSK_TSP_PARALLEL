package TSP_GA;

import java.io.IOException;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws IOException {

        Data data = new Data();
        GeneticAlgorithm geneticAlgorithm;

        String janPath = "/home/max/ZSSK/ZSSK_TSP_PARALLEL/src/main/java/resources/";

        data.setPathToDirectory(janPath);
        data.setPathToFile("berlin52.tsp");
        data.checkAndSetFile();
        data.saveMap();

        geneticAlgorithm = new GeneticAlgorithm(data.getMap());
        geneticAlgorithm.createPopulation();

        ArrayList tournamentWinner=geneticAlgorithm.tournamentSelection(geneticAlgorithm.getPopulation());
        System.out.println(tournamentWinner);


    }

    private static void printSolution(GeneticAlgorithm geneticAlgorithm) {
        for (ArrayList<Integer> integers : geneticAlgorithm.getPopulation()) {
            System.out.print(integers);
            System.out.print(" -> " + geneticAlgorithm.routeLength(integers));
            System.out.println();
        }
        System.out.println();
    }
}