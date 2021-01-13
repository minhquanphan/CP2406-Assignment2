package Model;

import java.util.ArrayList;
import java.util.UUID;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;

public class SaveFile {

    private ArrayList<Road> roads; // traffic lights are placed on roads, so just need to get the roads attrs
    private int scale;

    public SaveFile(ArrayList<Road> roads, int scale) {
        this.roads = roads;
        this.scale = scale;
    }

    public void save() {
        if ((roads == null) || (roads.size() == 0)) {
            System.out.println("roads are empty/null, nothing to save");
            return;
        }
        UUID uuid=UUID.randomUUID();
        Path file = Paths.get("src/SaveFile-" + uuid + ".txt");
        // example: 8;bmw;vertical;8;16;2;50,50;0,1,2;red-56;green-50
        String roadData = "scale;road.name;road.orientation;road.width;road.length;road.speedLimit;road.startLocation;roads.connectedRoadNames;light.state-light.position";
        for (Road road : roads) {
            roadData += System.lineSeparator() + this.scale + ";" + road.toString();
            for(TrafficLight light : road.getLightsOnRoad()) {
                roadData += ";" + light.toString();
            }
        }
        byte[] data = roadData.getBytes();
        OutputStream output;
        try {
            output = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            output.write(data);
            output.flush();
            output.close();
        } catch (FileAlreadyExistsException e) {
            System.out.println("File already exists");
        } catch (Exception e) {
            System.out.println("Message: " + e);
        }
    }
}
