import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

import java.util.Iterator;
import java.util.List;

import vectors_custom.Vector2D;

public class Simulation extends JPanel{
    public static final double length = 10;
    public static final double width = 10;
    private int step;

    private List<Person> population;
    private List<InfectionProgress> spreadProgressList;

    private Random random;
    //szansa na przyrost populacji
    private final double income = 10;

    //działanie symulacji
    private boolean simulationRunning = false;

    //saving
    private String content;

    public Simulation(int populationSize, boolean immune) {

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
        
        while (this.step < 500 && this.simulationRunning) {
            NextStep();
            update(getGraphics());
            //repaint();
        }
    }

    //krok symulacji
    private void NextStep() {
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

        /* 
        // przyrost populacji
        if (growPopulation()) {
            this.population.add(new Person());
        }
        */
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
        int range = this.random.nextInt() * 100;

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

    private static final int SCALE_FACTOR = 50;  // Współczynnik skalowania
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<Person> currentPopulation = new ArrayList<>(population);
        for (Person person : currentPopulation) {
            int x = (int) (person.getLocation().getComponents()[0] * SCALE_FACTOR);
            int y = (int) (person.getLocation().getComponents()[1] * SCALE_FACTOR);
            g.setColor(person.getHealth().isInfected() ? Color.red : Color.blue);
            g.fillOval(x, y, 5, 5); // Kropka reprezentująca osobę
        }
    }
}