import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.Iterator;
import java.util.List;

import vectors_custom.Vector2D;

public class Simulation extends JPanel implements ActionListener{
    public static final int length = 10;
    public static final int width = 10;
    private static final int SCALE_FACTOR = 50;  // Współczynnik skalowania
    private static int step = 0;
    Timer timer;

    private List<Person> population;
    private List<InfectionProgress> spreadProgressList;

    private Random random;
    //szansa na przyrost populacji
    private final double income = 10;

    //działanie symulacji
    private boolean simulationRunning = false;

    //saving
    private String content;

    //symulacja.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
    //solving flickering
    //Graphics bufferGraphics;

    Simulation(int populationSize, boolean immune) {
        this.population = new ArrayList<Person>();
        this.spreadProgressList = new ArrayList<InfectionProgress>();
        this.random = new Random();
        this.timer = new Timer(40, this);

        for (int i = 0; i < populationSize; i++) {
            double x = random.nextDouble(width + 1);
            double y = random.nextDouble(length + 1);
            population.add(new Person(immune, new Vector2D(x, y)));
        }

        this.setPreferredSize(new Dimension(length * SCALE_FACTOR, width * SCALE_FACTOR));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NextStep();
        //update(getGraphics());
        //validate();
        repaint();
    }

    public int getCurrentStep() {
        return step;
    }

    public void stopSimulation() {
        this.simulationRunning = !this.simulationRunning;
        this.timer.stop();
    }

    public void startSimulation() {
        this.timer.start();
        this.simulationRunning = true;
    }

    //krok symulacji
    private void NextStep() {    
        movement();
        spreadDisease();
        updateRecoveryProgress(); 
    
        // Wykonanie kroku
        step += 1;
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
    
    //grafika
    public void update(Graphics g) {
        paint(g); 
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        List<Person> currentPopulation = new ArrayList<>(population);
        for (Person person : currentPopulation) {
            int x = (int) (person.getLocation().getComponents()[0] * SCALE_FACTOR);
            int y = (int) (person.getLocation().getComponents()[1] * SCALE_FACTOR);
            g.setColor(person.getHealth().isInfected() ? Color.red : Color.blue);
            g.fillOval(x, y, 8, 8); // Kropka reprezentująca osobę
        }
    }
}