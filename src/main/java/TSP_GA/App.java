package TSP_GA;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        Data data = new Data();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

        data.setPathToDirectory("/home/djankooo/IdeaProjects/ZSSK/src/main/java/resources/");
        data.setPathToFile("berlin52.tsp");
        data.checkAndSetFile();
        data.saveMap();

        geneticAlgorithm.setMap(data.getMap());
        geneticAlgorithm.createPopulation();
        geneticAlgorithm.getPopulation().forEach(System.out::println); // TODO : do wyjebania
    }
}