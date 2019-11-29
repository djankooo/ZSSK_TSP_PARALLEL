package TSP_GA;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

@Getter
@Setter
class Data {

    private String pathToDirectory;
    private String filename;

    private File file;
    private BufferedReader bufferedReader;
    private Pattern pattern = Pattern.compile("[0-9][ ][0-9]*[.][0-9]*[ ][0-9]*[.][0-9]*");

    private Map map = new Map();

    Data(String path, String filename) {
        this.pathToDirectory = path;
        this.filename = filename;
    }

    void checkAndReadFile() throws IOException {
        if (pathToDirectory != null && filename != null)
            file = new File(getPathToDirectory() + getFilename());
        readMapFromFile();
    }

    private void readMapFromFile() throws IOException {

        bufferedReader = new BufferedReader(new FileReader(file));

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains(":")) {

                String[] split = line.split(":");

                if (split[0].contains("NAME")) {
                    map.setNAME(split[1]);
                }
                if (split[0].contains("TYPE")) {
                    map.setTYPE(split[1]);
                }
                if (split[0].contains("COMMENT")) {
                    map.setCOMMENT(split[1]);
                }
                if (split[0].contains("DIMENSION")) {
                    map.setDIMENSION(Integer.parseInt(split[1].replaceAll("\\s+", "")));
                }
                if (split[0].contains("EDGE_WEIGHT_TYPE")) {
                    map.setEDGE_WEIGHT_TYPE(split[1]);
                }

            }
            if (line.contains("NODE_COORD_SECTION")) {
                continue;
            }

            if (pattern.matcher(line).find()) {
                String[] cityData = line.split(" ");
                map.addCity(new City(Float.parseFloat(cityData[1]), Float.parseFloat(cityData[2])));
            }
        }
    }
}



