package Test;

import Model.Bus;
import Model.Road;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class BusTest {
    Bus bus = new Bus("SBSTransit");

    @Test
    @Order(1)
    void getId() {
        assertEquals("bus_1", bus.getId());
    }

    @Test
    @Order(2)
    void getLength() {
        assertEquals(12, bus.getLength());
    }

    @Test
    @Order(3)
    void testInheritance() {
        assertEquals(0, bus.getSpeed());
        assertEquals(0, bus.getPosition());

        Road road = new Road("Bugis Street", 1, 6,
                new int[]{0, 0}, Road.Orientation.VERTICAL);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bus.setCurrentRoad(road);
        });
        road.setLength(24);
        bus.setCurrentRoad(road);
        assertEquals(road, bus.getCurrentRoad());
        bus.move();
        bus.printStatus();
        assertEquals(1, bus.getPosition());
        assertEquals(1, bus.getSpeed());
        bus.move();
        bus.printStatus();
        assertEquals(2, bus.getPosition());
        assertEquals(1, bus.getSpeed());

        bus.setLength(5);
        assertEquals(5, bus.getLength());

        bus.setBreadth(2);
        assertEquals(2, bus.getBreadth());

        bus.setSpeed(6);
        assertEquals(6, bus.getSpeed());

        bus.setPosition(0);
        assertEquals(0, bus.getPosition());

        bus.setName("SMRT");
        assertEquals("SMRT", bus.getName());
    }
}