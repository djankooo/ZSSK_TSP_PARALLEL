package TSP_GA;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        Data data = new Data();

        data.setPathToDirectory("/home/djankooo/IdeaProjects/ZSSK/src/main/java/resources/");
        data.setPathToFile("berlin52.tsp");
        data.checkAndSetFile();
        Map map = data.saveMap();

        System.out.println("Hello World!");
    }
}
