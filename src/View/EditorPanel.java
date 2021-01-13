package View;

import Model.Road;
import Model.TrafficLight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class EditorPanel extends JPanel {

    private ArrayList<Road> roads;
    private ArrayList<TrafficLight> lights;
    private int scale;

    public void newMap() {
        roads = new ArrayList<>();
        lights = new ArrayList<>();
        MouseAdapter mouseLis = new MouseAdapter() {
            String newRoadId;
            @Override
            public void mouseClicked(MouseEvent e) {
                int xValue = e.getX() / scale;
                int yValue = e.getY() / scale;
                System.out.println("X: " + xValue);
                System.out.println("Y: " + yValue);
                String[] orientationOptions = {"Horizontal", "Vertical"};

                int orientationSelection = JOptionPane.showOptionDialog(null, "Choose Orientation:",
                        "Orientation Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, orientationOptions, roads);
                switch (orientationSelection) {
                    case 0:
                        Road horizontalRoad = new Road(Integer.toString(roads.size()), 1, 36, new int[]{xValue,
                                yValue}, Road.Orientation.HORIZONTAL);
                        roads.add(horizontalRoad);
                        TrafficLight horizontalLight = new TrafficLight();
                        horizontalLight.setRoadAttachedTo(horizontalRoad);
                        lights.add(horizontalLight);
                        newRoadId = horizontalRoad.getId();
                        break;
                    case 1:
                        Road verticalRoad = new Road(Integer.toString(roads.size()), 1, 36, new int[]{xValue,
                                yValue}, Road.Orientation.VERTICAL);
                        roads.add(verticalRoad);
                        TrafficLight verticalLight = new TrafficLight();
                        verticalLight.setRoadAttachedTo(verticalRoad);
                        lights.add(verticalLight);
                        newRoadId = verticalRoad.getId();
                }

                if (roads.size() > 1) {
                    String[] connectionOptions = new String[roads.size() - 1];
                    for (int i = 0; i < connectionOptions.length; i++) {
                        if (!roads.get(i).getId().equals(newRoadId)) {
                            connectionOptions[i] = Integer.toString(i);
                        }
                    }
                    int connectionSelection = JOptionPane.showOptionDialog(null,
                            "Choose Connected Roads:",
                            "Connections Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, connectionOptions, connectionOptions[0]);
                    if (connectionSelection >= 0) {
                        roads.get(connectionSelection).getConnectedRoads().add(roads.get(roads.size() - 1));
                    }
                }

                repaint();

                System.out.println("number of roads: " + roads.size() + ", number of lights: " + lights.size());
            }
        };
        addMouseListener(mouseLis);
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public void setRoads(ArrayList<Road> roads) {
        this.roads = roads;
        if ((this.roads != null) && (this.roads.size() > 0)) { repaint(); }
    }

    public ArrayList<TrafficLight> getLights() {
        return lights;
    }

    public void setLights(ArrayList<TrafficLight> lights) {
        this.lights = lights;
        if ((this.lights != null) && (this.lights.size() > 0)) { repaint(); }
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return this.scale;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println("paintComponent..., road size: " + roads.size() + ", lights size: " + lights.size());

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (Road road : roads) {
            road.draw(g, scale);
        }

        for (TrafficLight light : lights) {
            light.draw(g, scale);
        }
    }
}
