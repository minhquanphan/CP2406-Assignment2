package Model;

import java.awt.*;
import java.util.ArrayList;

public class Road {

    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    private static final int BUS_MIN_LENGTH_FACTOR = 2;
    private static final int BUS_MAX_LENGTH_FACTOR = 5;
    private static int currentRoadNumber;
    private String name;
    private Orientation orientation;
    private String id;
    private int speedLimit;
    private int length;
    private int width;
    private int currentVehiclesLength = 0;
    private int[] startLocation;
    private int[] endLocation;
    private ArrayList<Vehicle> vehiclesOnRoad = new ArrayList<>();
    private ArrayList<TrafficLight> lightsOnRoad = new ArrayList<>();
    private ArrayList<Road> connectedRoads = new ArrayList<>();

    public Road(String name, int speedLimit, int length, int[] startLocation, Orientation orientation) {
        this.id = "road_" + nextRoadNumber(); // road ID has pattern road_### where ### is auto generated and incremental
        this.name = name;
        this.orientation = orientation;
        this.width = 8;
        setSpeedLimit(speedLimit);
        setStartLocation(startLocation);
        setLength(length);
        setEndLocation();
    }

    public void draw(Graphics g, int scale) {
        if (orientation == Orientation.HORIZONTAL) {
            int[] startLocation = this.startLocation;
            int x = startLocation[0] * scale;
            int y = startLocation[1] * scale;
            int width = length * scale;
            int height = this.width * scale;
            g.setColor(Color.darkGray);
            g.fillRect(x, y, width, height);
            //Center Lines
            g.setColor(Color.white);
            g.fillRect(x, y + (height / 2) - scale / 6, width, scale / 6);
            g.fillRect(x, y + (height / 2) + scale / 6, width, scale / 6);
        } else if (orientation == Orientation.VERTICAL) {
            int[] startLocation = this.startLocation;
            int x = startLocation[0] * scale;
            int y = startLocation[1] * scale;
            int width = this.width * scale;
            int height = this.length * scale;
            g.setColor(Color.darkGray);
            g.fillRect(x, y, width, height);
//            //Center Lines
            g.setColor(Color.white);
            g.fillRect(x + (width / 2) - scale / 6, y, scale / 6, height);
            g.fillRect(x + (width / 2) + scale / 6, y, scale / 6, height);
        }
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    // to make sure ID is unique, ID should be generated by the system, not entered by users, so there is only getter for ID
    public String getId() { return id; }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        validateSpeedLimit(speedLimit);
        this.speedLimit = speedLimit;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public void setLength(int length) {
        validateLength(length);
        this.length = length;
        this.setEndLocation(); // need to update endLocation after length is changed
    }

    public void setStartLocation(int[] startLocation) {
        validateLocation(startLocation);
        this.startLocation = startLocation;
        this.setEndLocation();
    }

    private int[] deduceHorizontalEndLocation() {
        return new int[]{this.length + this.startLocation[0], this.startLocation[1]};
    }

    private int[] deduceVerticalEndLocation() {
        return new int[]{this.startLocation[0], this.length + this.startLocation[1]};
    }

    private void setEndLocation() {
        if (this.orientation == Orientation.HORIZONTAL) {
            this.endLocation = deduceHorizontalEndLocation();
        } else if (this.orientation == Orientation.VERTICAL) {
            this.endLocation = deduceVerticalEndLocation();
        }
    }

    public String locationToBePrinted(int[] location) {
        return("(" + location[0] + "," + location[1] + ")");
    }

    public void printRoadInfo() {
        System.out.printf("Road %s has name %s, speed limit at %dm/s with %dm length at location:%s to %s with %s vehicles %n",
                this.getId(), this.getName(),
                this.getSpeedLimit(), this.getLength(), this.locationToBePrinted(this.getStartLocation()),
                this.locationToBePrinted(this.getEndLocation()), this.vehiclesOnRoad.size());
    }

    // "name;orientation;width;length;speedLimit;startLocation;connectedRoadNames"
    public String toString() {
        String connectedRoadName = ",";
        if (this.connectedRoads.size() > 0) {
            connectedRoadName = "";
        }
        for(Road road : this.connectedRoads) {
            connectedRoadName += road.getName() + ",";
        }
        connectedRoadName = connectedRoadName.substring(0, connectedRoadName.length() - 1); // remove the last ,
        return(this.getName() + ";" + this.orientation.toString().toLowerCase() + ";" + this.width + ";" +
                this.length + ";" + this.speedLimit + ";" + this.startLocation[0] + "," + this.startLocation[1] +
                ";" + connectedRoadName);
    }

    public int getCurrentVehiclesLength() { return currentVehiclesLength; }

    public void newVehicleEnteringRoad(Vehicle vehicle) {
        validateVehicleLength(vehicle);
        validateUniqueVehicle(vehicle);
        this.vehiclesOnRoad.add(vehicle);
        this.currentVehiclesLength += vehicle.getLength();
    }

    public void setLightsOnRoad(ArrayList<TrafficLight> lightsOnRoad) {
        this.lightsOnRoad = lightsOnRoad;
    }

    public void setConnectedRoads(ArrayList<Road> connectedRoads) {
        this.connectedRoads = connectedRoads;
    }

    private int nextRoadNumber() {
        currentRoadNumber += 1;
        return currentRoadNumber;
    }

    private void validateLength(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be positive.");
        }
    }

    private void validateLocation(int[] location) {
        if (location.length != 2 || location[0] < 0 || location[1] < 0) {
            throw new IllegalArgumentException("Location takes only 2 values which must not be negative.");
        }
    }

    private void validateSpeedLimit(int speedLimit) {
        if (speedLimit < 1) {
            throw new IllegalArgumentException("Speed limit must be positive.");
        }
    }

    private void validateVehicleLength(Vehicle vehicle) {
        if ((vehicle instanceof Bus) && (this.length < BUS_MIN_LENGTH_FACTOR*vehicle.getLength())) {
            throw new IllegalArgumentException("Bus ID " + vehicle.getId() +
                    " cannot enter this road which has length less than " + BUS_MIN_LENGTH_FACTOR + " times the length of it.");
        }

        if ((vehicle instanceof Bus) && (this.length > BUS_MAX_LENGTH_FACTOR*vehicle.getLength())) {
            throw new IllegalArgumentException("Bus ID" + vehicle.getId() +
                    "cannot enter this road which has length more than " + BUS_MAX_LENGTH_FACTOR + " times the length of it.");
        }

        if ((currentVehiclesLength + vehicle.getLength()) > this.length) {
            throw new IllegalArgumentException("This road has full vehicles driving on. Please wait to be able to enter the road.");
        }
    }

    private void validateUniqueVehicle(Vehicle vehicle) {
        for (Vehicle currentVehicle: this.vehiclesOnRoad) {
            if (currentVehicle.getId().equals(vehicle.getId())){
                throw new IllegalArgumentException("This vehicle has already been added to the road");
            }
        }
    }

    public int[] getStartLocation() {
        return startLocation;
    }

    public int[] getEndLocation() {
        return endLocation;
    }

    public ArrayList<Vehicle> getVehiclesOnRoad() {
        return vehiclesOnRoad;
    }

    public ArrayList<TrafficLight> getLightsOnRoad() {
        return lightsOnRoad;
    }

    public ArrayList<Road> getConnectedRoads() {
        return connectedRoads;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
