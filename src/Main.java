import Model.SaveFile;
import Model.LoadFile;
import View.EditorPanel;
import View.SimulationPanel;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class Main {

    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 1024;
    private static SimulationPanel simulationPanel = new SimulationPanel();
    private static EditorPanel editorPanel = new EditorPanel();
    private static final int SCALE = 8;

    public static void main(String[] args) {
        // Simulation Window setup:
        JFrame mainWindow = new JFrame("Traffic Simulator");
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        //Status Bar
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 0));
        bottomPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JLabel modeLabel = new JLabel("Mode: ");
        bottomPanel.add(modeLabel);
        JLabel statusLabel = new JLabel("Status: ");
        bottomPanel.add(statusLabel);
        mainWindow.add(bottomPanel, BorderLayout.SOUTH);

        //Menu bar:
        JMenuBar menuBar = new JMenuBar();
        mainWindow.add(menuBar, BorderLayout.NORTH);

        //Editor Menu:
        JMenu editMenu = new JMenu("City Editor");
        MenuListener cityLis = new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                modeLabel.setText("Mode: Editor");
                mainWindow.repaint();
            }

            @Override
            public void menuDeselected(MenuEvent e) {}

            @Override
            public void menuCanceled(MenuEvent e) {}
        };
        editMenu.addMenuListener(cityLis);
        menuBar.add(editMenu);

        JMenuItem newMapItem = new JMenuItem("New");
        newMapItem.addActionListener(e -> {
            simulationPanel.setVisible(false);
            mainWindow.remove(editorPanel);
            editorPanel = new EditorPanel();
            editorPanel.newMap();
            editorPanel.setScale(SCALE);
            mainWindow.add(editorPanel);
            editorPanel.setVisible(true);
            statusLabel.setText("Status: New Map");
            mainWindow.validate();
            mainWindow.repaint();
        });
        editMenu.add(newMapItem);

        JMenuItem openMapItem = new JMenuItem("Open");
        openMapItem.addActionListener(e -> {
            LoadFile loadFile = new LoadFile();
            loadFile.load();
            simulationPanel.setVisible(false);
            mainWindow.remove(editorPanel);
            editorPanel = new EditorPanel();
            editorPanel.newMap();
            editorPanel.setScale(SCALE);
            editorPanel.setRoads(loadFile.getRoads());
            editorPanel.setLights(loadFile.getLights());
            mainWindow.add(editorPanel);
            editorPanel.setVisible(true);
            statusLabel.setText("Status: Map Loaded");
            mainWindow.validate();
            mainWindow.repaint();
        });
        editMenu.add(openMapItem);

        JMenuItem saveMapItem = new JMenuItem("Save");
        saveMapItem.addActionListener(e -> {
            SaveFile saveFile = new SaveFile(editorPanel.getRoads(), editorPanel.getScale());
            saveFile.save();
        });
        editMenu.add(saveMapItem);

        JMenuItem exitProgramItem = new JMenuItem("Exit");
        exitProgramItem.addActionListener(e -> System.exit(0));
        editMenu.add(exitProgramItem);

        //Simulation Menu:
        JMenu simMenu = new JMenu("Simulation");
        MenuListener simLis = new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                modeLabel.setText("Mode: Simulation");
                mainWindow.repaint();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        };
        simMenu.addMenuListener(simLis);


        JMenuItem loadSimItem = new JMenuItem("Load Map");
        simMenu.add(loadSimItem);

        JMenuItem spawnItem = new JMenuItem("Add Vehicles");
        spawnItem.setEnabled(false);
        simMenu.add(spawnItem);

        JMenuItem startSimItem = new JMenuItem("Start");
        startSimItem.setEnabled(false);
        startSimItem.addActionListener(e -> {
            simulationPanel.simulate();
            statusLabel.setText("Status: Simulation Started");
            simulationPanel.setStopSim(false);
            mainWindow.validate();
            mainWindow.repaint();
        });
        simMenu.add(startSimItem);

        spawnItem.addActionListener(e -> {
            boolean invalid = true;
            boolean spawnRateInvalid = true;

            while (invalid) {
                String spawnInput = JOptionPane.showInputDialog("Total number of Vehicles to spawn:");
                try {
                    int spawns = Integer.parseInt(spawnInput);
                    simulationPanel.setVehicleSpawn(spawns);
                    invalid = false;
                } catch (NumberFormatException exp) {
                    System.out.println("Exception: Unable to parse Int user input: " + spawnInput);
                    invalid = true;
                }
            }

            while (spawnRateInvalid) {
                String spawnRateInput = JOptionPane.showInputDialog("Number of Simulation tics between spawns:");
                try {
                    int spawnRate = Integer.parseInt(spawnRateInput);
                    simulationPanel.setVehicleSpawnRate(spawnRate);
                    spawnRateInvalid = false;
                } catch (NumberFormatException exp) {
                    System.out.println("Exception: Unable to parse Int user input: " + spawnRateInput);
                    spawnRateInvalid = true;
                }

            }
        });

        JMenuItem stopSimItem = new JMenuItem("Stop");
        stopSimItem.setEnabled(false);
        stopSimItem.addActionListener(e -> {
            simulationPanel.setStopSim(true);
            statusLabel.setText("Status: Simulation Stopped");
            mainWindow.validate();
            mainWindow.repaint();
        });
        simMenu.add(stopSimItem);

        loadSimItem.addActionListener(e -> {
            statusLabel.setText("Status: Map Loaded!");
            editorPanel.setVisible(false);
            simulationPanel = new SimulationPanel();
            simulationPanel.setScale(SCALE);
            simulationPanel.loadMap(editorPanel.getRoads(), editorPanel.getLights());
            mainWindow.add(simulationPanel);
            startSimItem.setEnabled(true);
            spawnItem.setEnabled(true);
            stopSimItem.setEnabled(true);
            mainWindow.repaint();
        });

        JMenuItem setUpdateRateItem = new JMenuItem("Update Rate");
        setUpdateRateItem.addActionListener(e -> {
            boolean invalid = true;
            while (invalid) {
                String updateRateInput = JOptionPane.showInputDialog("Enter the Update Rate of the Simulation");
                try {
                    int updateRate = Integer.parseInt(updateRateInput);
                    simulationPanel.setUpdateRate(updateRate);
                    statusLabel.setText("Status: Update Rate set to " + updateRate);
                    mainWindow.validate();
                    mainWindow.repaint();
                    invalid = false;
                } catch (NumberFormatException exc) {
                    System.out.println("Exception: Unable to parse Int user input: " + updateRateInput);
                    invalid = true;
                }
            }
        });
        simMenu.add(setUpdateRateItem);

        menuBar.add(simMenu);

        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }
}
