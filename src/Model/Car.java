package Model;

public class Car extends Vehicle {
    private static final String PREFIX = "car_";
    protected static int currentCarNumber = 0;

    public Car(String name) {
        super(name);
        setLength(super.getLength());
        this.currentCarNumber += 1;
        // car ID has pattern car_### where ### is auto generated and incremental
        this.id = PREFIX + currentCarNumber;
        this.breadth = length / 2;
        this.position = -length;
    }

}

