package Model;

import java.awt.*;
import java.util.Random;

public abstract class Vehicle {

    private static final int STOPPED = 0; //car speed is 0m/s
    private static final int START_POSITION = 1;
    private static final String PREFIX = "vehicle_";
    private Road currentRoad; // current Road object
    protected static int currentVehicleNumber = 0;
    protected String id; // unique identifier
    protected String name;
    protected int length; // number of segments occupied, 1 for ease of prototype.
    protected int breadth;
    protected int speed; //segments moved per turn
    protected int position; // position on current road
    private Color colour;
    private Random random = new Random();


    public Vehicle(String name) {
        this.currentVehicleNumber += 1;
        // vehicle ID has pattern vehicle_### where ### is auto generated and incremental
        this.id = PREFIX + currentVehicleNumber;
        this.name = name;
        this.length = 4;
        this.breadth = 2;
        this.speed = 0;
        this.colour = randomColour();
    }

    public Vehicle() {
        this.id = "vehicle_000";
        this.length = 0;
        this.breadth = 0;
        this.speed = 0;
        this.position = 0;
    }

    public void move() {
        Random random = new Random();
        int[] vehicleNextRange = new int[]{(this.position + this.currentRoad.getSpeedLimit()),
                (this.position + this.currentRoad.getSpeedLimit() + this.length)};
        this.speed = this.currentRoad.getSpeedLimit();
        //vehicle in front check:
        for (Vehicle nextVehicle : currentRoad.getVehiclesOnRoad()) {
            if ((nextVehicle.getId() != this.getId()) &&
                    (nextVehicle.position >= vehicleNextRange[0]) &&
                    (nextVehicle.position <= vehicleNextRange[1])) {
                System.out.println("Vehicle " + this.getId() + " are blocked! next Vehicle position: " +
                        nextVehicle.position + ", this vehicle next range: [" + vehicleNextRange[0] + ", " + vehicleNextRange[0] + "]");
                this.printStatus();
                this.speed = STOPPED;
                break;
            }
        }

        //red light check:
        if (this.speed != STOPPED) {
            if (!this.currentRoad.getLightsOnRoad().isEmpty() &&
                    vehicleNextRange[0] <= this.currentRoad.getLightsOnRoad().get(0).getPosition() &&
                    (vehicleNextRange[1] + 1) >= this.currentRoad.getLightsOnRoad().get(0).getPosition() &&
                    this.currentRoad.getLightsOnRoad().get(0).getState().equals("red")) {
                System.out.println("Traffic light is red!");
                this.printStatus();
                this.speed = STOPPED;
            } else {
                this.speed = this.currentRoad.getSpeedLimit();
                if (this.currentRoad.getLength() < vehicleNextRange[1] &&
                        !this.currentRoad.getConnectedRoads().isEmpty()) {
                    this.currentRoad.getVehiclesOnRoad().remove(this);
                    int nextRoadIndex = random.nextInt(this.currentRoad.getConnectedRoads().size());
                    this.currentRoad = this.currentRoad.getConnectedRoads().get(nextRoadIndex);
                    this.currentRoad.newVehicleEnteringRoad(this);
                    System.out.println("Vehicles changing to the next road!");
                    this.position = START_POSITION;
                } else if (this.currentRoad.getLength() > vehicleNextRange[1]) {
                    this.position = (this.position + this.speed);
                } else {
                    System.out.println("Vehicles going to the end of the road!, road length: " +
                            this.currentRoad.getLength() + ", vehicleNextRange[1]: " + vehicleNextRange[1]);
                    this.printStatus();
                    this.position = (this.position + this.speed);
                    this.speed = STOPPED;
                }
            }
        }
    }

    public void draw(Graphics g, int scale) {
        int xValue = 0;
        int yValue = 1;
        if (currentRoad.getOrientation() == Road.Orientation.HORIZONTAL) {
            int[] startLocation = getCurrentRoad().getStartLocation();
            int width = getLength() * scale;
            int height = getBreadth() * scale;
            int x = (getPosition() + startLocation[xValue]) * scale;
            int y = (startLocation[yValue] * scale) + scale;
            g.setColor(colour);
            g.fillRect(x, y, width, height);
            this.getCurrentRoad().printRoadInfo();
//            System.out.println("Vehicle " + this.getId() + " at position: " + position + ", fillRect: x=" + (getPosition() + startLocation[xValue]) +
//                    ", y=" + startLocation[yValue] + ", width=" +  getLength() + ", height=" + getBreadth() +
//                    ", on road=" + this.currentRoad.getId());
        } else if (currentRoad.getOrientation() == Road.Orientation.VERTICAL) {
            int[] startLocation = getCurrentRoad().getStartLocation();
            int width = getBreadth() * scale;
            int height = getLength() * scale;
            int x = (startLocation[xValue] * scale) + ((currentRoad.getWidth() * scale) - (width + scale));
            int y = (getPosition() + startLocation[yValue]) * scale;
            g.setColor(colour);
            g.fillRect(x, y, width, height);
            this.getCurrentRoad().printRoadInfo();
//            System.out.println("Vehicle " + this.getId() + " at position: " + position + ", fillRect: x=" + startLocation[xValue] +
//                    ", y=" + (startLocation[yValue] + getPosition()) + ", width=" +  getBreadth() + ", height=" + getLength() +
//                    ", on road=" + this.currentRoad.getId());
        }
    }

    private Color randomColour() {
        int r = random.nextInt(245 + 1) + 10;
        int g = random.nextInt(245 + 1) + 10;
        int b = random.nextInt(245 + 1) + 10;
        return new Color(r, g, b);
    }

    public void setCurrentRoad(Road currentRoad) {
        currentRoad.newVehicleEnteringRoad(this); //add this vehicle to the road its on.
        this.currentRoad = currentRoad;
    }

    public void printStatus() {
        System.out.printf("%s going: %dm/s on %s at position: %s%n", this.getId(), this.getSpeed(), this.getCurrentRoad().
                getId(), this.getPosition());
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getBreadth() {
        return breadth;
    }

    public void setBreadth(int breadth) {
        this.breadth = breadth;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public String getId() {
        return id;
    }

    public String getName() { return this.name; }

    public void setName(String name) {  this.name = name; }
}

