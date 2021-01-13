package Test;

import Model.Motorbike;
import Model.Road;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MotorbikeTest {
    Motorbike bike = new Motorbike("Ducati");

    @Test
    @Order(1)
    void getId() {
        assertEquals("motorbike_1", bike.getId());
    }

    @Test
    @Order(2)
    void getLength() {
        assertEquals(2, bike.getLength());
    }

    @Test
    @Order(3)
    void testInheritance() {
        assertEquals(0, bike.getSpeed());
        assertEquals(-2, bike.getPosition());

        Road road = new Road("Woodlands Circle", 1, 5, new int[]{0, 0},
                Road.Orientation.VERTICAL);
        bike.setCurrentRoad(road);
        assertEquals(road, bike.getCurrentRoad());
        bike.move();
        bike.printStatus();
        assertEquals(-1, bike.getPosition());
        assertEquals(1, bike.getSpeed());
        bike.move();
        bike.printStatus();
        assertEquals(0, bike.getPosition());
        assertEquals(1, bike.getSpeed());
        bike.move();
        bike.printStatus();
        assertEquals(1, bike.getPosition());
        assertEquals(1, bike.getSpeed());
        bike.move();
        bike.printStatus();
        assertEquals(2, bike.getPosition());
        assertEquals(1, bike.getSpeed());
        bike.move();
        bike.printStatus();
        assertEquals(3, bike.getPosition());
        assertEquals(1, bike.getSpeed());
        bike.move();
        bike.printStatus();
        assertEquals(3, bike.getPosition());
        assertEquals(0, bike.getSpeed());

        bike.setLength(5);
        assertEquals(5, bike.getLength());

        bike.setBreadth(2);
        assertEquals(2, bike.getBreadth());

        bike.setSpeed(6);
        assertEquals(6, bike.getSpeed());

        bike.setPosition(0);
        assertEquals(0, bike.getPosition());

        bike.setName("Kawasaki");
        assertEquals("Kawasaki", bike.getName());
    }
}