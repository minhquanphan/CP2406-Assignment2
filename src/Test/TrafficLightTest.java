package Test;

import Model.Road;
import Model.TrafficLight;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TrafficLightTest {
    TrafficLight light = new TrafficLight();

    @Test
    @Order(1)
    void getId() {
        assertEquals("light_1", light.getId());
    }

    @Test
    @Order(2)
    void testOperate() {
        Road road = new Road("Jalan Besar", 1, 5, new int[]{0, 0}
                , Road.Orientation.VERTICAL);
        light.setRoadAttachedTo(road);
        light.operate(3515);
        light.printLightStatus();
        assertEquals("green", light.getState());
    }

    @Test
    @Order(3)
    void getState() {
        assertEquals("red", light.getState());
    }

    @Test
    @Order(5)
    void getRoad() {
        Road road = new Road("Jalan Besar", 1, 5, new int[]{0, 0}
                , Road.Orientation.VERTICAL);
        light.setRoadAttachedTo(road);
        assertEquals(road, light.getRoadAttachedTo());
    }

    @Test
    @Order(6)
    void setRoadAttachedTo() {
        Road road = new Road("Jalan Besar", 1, 5, new int[]{0, 0}
                , Road.Orientation.VERTICAL);
        light.setRoadAttachedTo(road);
        assertEquals(road, light.getRoadAttachedTo());
        assertEquals(road.getLength(), light.getPosition());
    }

    @Test
    @Order(7)
    void getPosition() {
        assertEquals(0, light.getPosition());
    }

    @Test
    @Order(8)
    void setPosition() {
        light.setPosition(6);
        assertEquals(6, light.getPosition());
    }
}