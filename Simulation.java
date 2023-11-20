import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;


import javax.swing.*;

import java.util.Iterator;
import java.util.List;

import vectors_custom.Vector2D;

public class Simulation extends JFrame{
    public static final double length = 100;
    public static final double width = 100;
    private int step;

    private ArrayList<Person> population;
    private ArrayList<InfectionProgress> spreadProgressList;

    private Random random;
    private final double income = 0.9;

    //działanie symulacji
    private boolean simulationRunning = false;

    //saving
    private String content;

    public Simulation(int populationSize, boolean immune) {

        SimulationPanel panel = new SimulationPanel();
        add(panel);

        this.step = 0;
        this.population = new ArrayList<Person>();
        this.spreadProgressList = new ArrayList<InfectionProgress>();
        this.random = new Random();

        for (int i = 0; i < populationSize; i++) {
            double x = random.nextDouble(width + 1);
            double y = random.nextDouble(length + 1);
            population.add(new Person(immune, new Vector2D(x, y)));
        }
    }

    public Integer getCurrentStep() {
        return this.step;
    }

    public void stopSimulation() {
        this.simulationRunning = !this.simulationRunning;
    }

    public void startSimulation() {
        this.simulationRunning = true;
        
        while (this.step < 100) {
            update();
            repaint(); // Potrzebne, jeśli korzystasz z rysowania na ekranie
            //this.simulationRunning = false;
            System.out.println(this.step);
        }
    }
 
    //krok symulacji
    private void update() {
        long startTime = System.currentTimeMillis();
    
        movement();
        spreadDisease();
        updateRecoveryProgress(); 
    
        // Wykonanie kroku
        this.step += 1;
    
        long elapsedTime = System.currentTimeMillis() - startTime;
        long sleepTime = Math.max(0, 40 - elapsedTime); // 1000 ms / 25 fps = 40 ms
    
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //ruch jednostek Person
    private void movement() {
        Iterator<Person> iterator = this.population.iterator();

        while (iterator.hasNext()) {
            Person person = iterator.next();
            // opuszczenie obszaru symulacji
            if (person.move()) {
                iterator.remove();
            }
        }

        // przyrost populacji
        if (growPopulation()) {
            this.population.add(new Person());
        }
    }

    //sprawdzanie warunków zarażenia
    private void spreadDisease() {

        for (int i = 0; i < this.spreadProgressList.size(); i++) {
            if (!this.spreadProgressList.get(i).updateProgress()) {
                this.spreadProgressList.remove(i);
            }
        }

        for (int i = 0; i < InfectedList.getInstance().getInfectedList().size(); i++) {
            for (int j = 0; j < this.population.size(); j++) {
                if (j < population.size() && this.population.get(j).getHealth().isInfected() && InfectedList.getInstance().getInfectedList().get(i).getLocation().spreadPossible(this.population.get(j).getLocation())) {
                    this.spreadProgressList.add(new InfectionProgress(InfectedList.getInstance().getInfectedList().get(i), this.population.get(j)));
                }
            }
        }
    }

    //progress powrotu do zdrowia
    private void updateRecoveryProgress() {
        for (int i = 0; i < InfectedList.getInstance().getInfectedList().size(); i++) {
            InfectedList.getInstance().getInfectedList().get(i).getHealth().updateRecoveryTime();
        }
    }

    //przyrost populacji
    private boolean growPopulation() {
        this.random = new Random();
        //zakres od 0 do 100
        double range = this.random.nextDouble() * 100;

        return range < income;
    }

    //zapis
    public void write(String content) {
        this.content = content;
    }

    public Memento save() {
        return new Memento(content);
    }

    public void restore(Memento memento) {
        this.content = memento.getState();
    }

    public String getContent() {
        return content;
    }

    //wykres
    public SimulationPanel getSimulationPanel() {
        return new SimulationPanel();
    }

    private class SimulationPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
    
            List<Person> currentPopulation = new ArrayList<>(population);
    
            for (Person person : currentPopulation) {
                int x = (int) person.getLocation().getComponents()[0];
                int y = (int) person.getLocation().getComponents()[1];
    
                g.setColor(person.getHealth().isInfected() ? Color.red : Color.blue);
                g.fillOval(x, y, 5, 5); // Kropka reprezentująca osobę
            }
        }
    }
}