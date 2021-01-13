package Model;

public class Bus extends Vehicle {
    private static final String PREFIX = "bus_";
    protected static int currentBusNumber = 0;

    public Bus(String name) {
        super(name);
        this.currentBusNumber += 1;
        // bus ID has pattern bus_### where ### is auto generated and incremental
        this.id = PREFIX + currentBusNumber;
        this.length = super.length * 3; // bus has length of 3 times the car's length
        this.position = -length;
    }

}
