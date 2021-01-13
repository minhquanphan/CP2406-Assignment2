package Model;

import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class LoadFile {
    private final int ROAD_NAME_INDEX = 1;
    private final int ROAD_ORIENTATION_INDEX = 2;
    private final int ROAD_LENGTH_INDEX = 4;
    private final int ROAD_SPEED_LIMIT_INDEX = 5;
    private final int START_LOCATION_INDEX = 6;
    private final int CONNECTED_ROADS_INDEX = 7;

    private ArrayList<Road> roads = new ArrayList<>();
    private ArrayList<TrafficLight> lights = new ArrayList<>();
    private ArrayList<String[]> roadData = new ArrayList<>();

    public ArrayList<Road> getRoads() { return roads; }

    public ArrayList<TrafficLight> getLights()  { return lights; }

    private void setConnectedRoads() {
        if ((roads != null) && (roads.size() > 1) && (roadData.size() > 0)) {
            Road road = null;
            for(String[] data : roadData) {
                for(Road storedRoad : roads) {
                    if (data[ROAD_NAME_INDEX].equals(storedRoad.getName())) {
                        road = storedRoad;
                    }
                }
                ArrayList<Road> connectedRoads = new ArrayList<>();
                String[] connectedRoadNames = data[CONNECTED_ROADS_INDEX].split(",");
                if ((road != null) && (connectedRoadNames.length > 0)) {
                    for(String roadName : connectedRoadNames) {
                        for(Road connectedRoad : roads) {
                            if (connectedRoad.getName().equals(roadName)) {
                                connectedRoads.add(connectedRoad);
                            }
                        }
                    }
                }
                road.setConnectedRoads(connectedRoads);
            }
        }
    }

    public void load() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select a text file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt file only", "txt");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            try {
                File file = new File(path);
                Scanner input = new Scanner(file);
                input.nextLine(); // skip the first line which is the header
                while (input.hasNextLine()) {
                    // "scale;name;orientation;width;length;speedLimit;startLocation;connectedNames;light.state-light.position"
                    // example: 8;bmw;vertical;8;16;2;50,50;red-56;green-50
                    String[] data = input.nextLine().split(";");
                    this.roadData.add(data);
                    String[] startLocationStr = data[START_LOCATION_INDEX].split(",");
                    int[] startLocation = new int[]{Integer.parseInt(startLocationStr[0]), Integer.parseInt(startLocationStr[1])};
                    // int scale = Integer.parseInt(data[0]);
                    Road.Orientation ori = Road.Orientation.VERTICAL;
                    if (data[ROAD_ORIENTATION_INDEX].toLowerCase().equals("vertical")) {
                        ori = Road.Orientation.VERTICAL;
                    } else if (data[ROAD_ORIENTATION_INDEX].toLowerCase().equals("horizontal")) {
                        ori = Road.Orientation.HORIZONTAL;
                    }
                    // Road(String name, int speedLimit, int length, int[] startLocation, Orientation orientation)
                    Road road = new Road(data[ROAD_NAME_INDEX], Integer.parseInt(data[ROAD_SPEED_LIMIT_INDEX]),
                            Integer.parseInt(data[ROAD_LENGTH_INDEX]), startLocation, ori);
                    TrafficLight light = new TrafficLight();
                    light.setRoadAttachedTo(road);
                    this.lights.add(light);
                    this.roads.add(road);
                }
                setConnectedRoads();
                for(Road road : this.roads) {
                    System.out.println(road.toString());
                }
                input.close();
            } catch (FileNotFoundException e) {
                System.out.println("Unable to read the content of file " + path);
                e.printStackTrace();
            }
        }
    }
}
