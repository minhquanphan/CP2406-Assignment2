package Test;

import Model.Road;
import Model.Car;
import Model.Bus;
import Model.TrafficLight;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoadTest {
    Road road = new Road("Tanah Merah", 2, 2, new int[]{0, 0},
            Road.Orientation.VERTICAL);

    @Test
    @Order(1)
    void constructor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Road("Watten View", -2, 10, new int[]{0, 0},
                    Road.Orientation.VERTICAL);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Road("York Hill", 2, -10, new int[]{0, 0},
                    Road.Orientation.VERTICAL);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Road("Yishun Central", 2, 10, new int[]{0, 0, 0},
                    Road.Orientation.VERTICAL);
        });
    }

    @Test
    @Order(2)
    void getId() {
        Assertions.assertEquals("road_5", road.getId());
    }

    @Test
    @Order(3)
    void getName() {
        Assertions.assertEquals("Tanah Merah", road.getName());
    }

    @Test
    @Order(4)
    void setName() {
        road.setName("Orchard Road");
        Assertions.assertEquals("Orchard Road", road.getName());
    }

    @Test
    @Order(5)
    void getSpeedLimit() {
        Assertions.assertEquals(2, road.getSpeedLimit());
    }

    @Test
    @Order(6)
    void setSpeedLimit() {
        road.setSpeedLimit(50);
        Assertions.assertEquals(50, road.getSpeedLimit());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            road.setSpeedLimit(-12);
        });
    }

    @Test
    @Order(7)
    void getLength() {
        Assertions.assertEquals(2, road.getLength());
    }

    @Test
    @Order(8)
    void setLength() {
        road.setLength(15);
        Assertions.assertEquals(15, road.getLength());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            road.setLength(-2);
        });
    }

    @Test
    @Order(9)
    void getStartLocation() {
        int[] expected = {0, 0};
        Assertions.assertArrayEquals(expected, road.getStartLocation());
    }

    @Test
    @Order(10)
    void setStartLocation() {
        int[] previousStartLocation = {0,0};
        int[] previousEndLocation = {0, 0 + road.getLength()};
        Assertions.assertArrayEquals(previousStartLocation, road.getStartLocation());
        Assertions.assertArrayEquals(previousEndLocation, road.getEndLocation());
        int[] updatedStartLocation = {2,5};
        int[] updatedEndLocation = {2, 5 + road.getLength()};
        road.setStartLocation(updatedStartLocation);
        Assertions.assertArrayEquals(updatedStartLocation, road.getStartLocation());
        Assertions.assertArrayEquals(updatedEndLocation, road.getEndLocation());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            int[] location = {0, 0, 0};
            road.setStartLocation(location);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            int[] location = {0, -2};
            road.setStartLocation(location);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            int[] location = {-5, 2};
            road.setStartLocation(location);
        });
    }

    @Test
    @Order(11)
    void getEndLocation() {
        int[] expected = {0, 2};
        Assertions.assertArrayEquals(expected, road.getEndLocation());
    }

    @Test
    @Order(12)
    void getVehiclesOnRoad() {
        ArrayList<Car> expected = new ArrayList<>();
        Assertions.assertEquals(expected, road.getVehiclesOnRoad());
    }

    @Test
    @Order(15)
    void getLights() {
        ArrayList<TrafficLight> expected = new ArrayList<>();
        Assertions.assertEquals(expected, road.getLightsOnRoad());
    }

    @Test
    @Order(16)
    void setLights() {
        ArrayList<TrafficLight> expected = new ArrayList<>();
        expected.add(new TrafficLight());
        road.setLightsOnRoad(expected);
        Assertions.assertEquals(expected, road.getLightsOnRoad());
    }

    @Test
    @Order(17)
    void getConnectedRoads() {
        ArrayList<Road> expected = new ArrayList<>();
        Assertions.assertEquals(expected, road.getConnectedRoads());
    }

    @Test
    @Order(18)
    void setConnectedRoads() {
        ArrayList<Road> expected = new ArrayList<>();
        expected.add(new Road("Tank Road", 1, 2, new int[]{0, 0},
                Road.Orientation.VERTICAL));
        road.setConnectedRoads(expected);
        Assertions.assertEquals(expected, road.getConnectedRoads());
    }

    @Test
    @Order(19)
    void newCarEnteringRoad() {
        Bus bus1 = new Bus("Bus1");
        Bus bus2 = new Bus("Bus2");
        Car car1 = new Car("BMW");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            road.newVehicleEnteringRoad(bus1);
        });
        road.setLength(25);
        road.printRoadInfo();
        road.newVehicleEnteringRoad(car1);
        road.newVehicleEnteringRoad(bus1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            road.newVehicleEnteringRoad(bus1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            road.newVehicleEnteringRoad(bus2);
        });
    }

    @Test
    @Order(21)
    void getCurrentVehiclesLength() {
        road.setLength(25);
        Bus bus1 = new Bus("Bus1");
        road.newVehicleEnteringRoad(bus1);
        Assertions.assertEquals(bus1.getLength(), road.getCurrentVehiclesLength());
    }

    @Test
    @Order(22)
    void locationToBePrinted() {
        Assertions.assertEquals("(0,0)", road.locationToBePrinted(road.getStartLocation()));
    }
}