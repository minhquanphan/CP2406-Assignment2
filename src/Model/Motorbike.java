package Model;

public class Motorbike extends Vehicle {
    private static final String PREFIX = "motorbike_";
    protected static int currentMotorbikeNumber = 0;

    public Motorbike(String name) {
        super(name);
        this.currentMotorbikeNumber += 1;
        // Motorbike ID has pattern motorbike_### where ### is auto generated and incremental
        this.id = PREFIX + currentMotorbikeNumber;
        setLength(super.getLength() / 2);
        breadth = super.getBreadth() / 2;
        position = -length;
    }
}
