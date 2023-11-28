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
    public static final int length = 20;
    public static final int width = 20;
    private static final int SCALE_FACTOR = 30;  // Współczynnik skalowania
    private int step = 0;
    Timer timer;

    private ArrayList<Person> population;
    private CareTaker careTaker;
    
    private Random random;
    //szansa na przyrost populacji
    private final int income = 20;

    Simulation(int populationSize, int immunePercentage) {
        this.population = new ArrayList<Person>();
        this.random = new Random();
        this.timer = new Timer(40, this);
        this.careTaker = new CareTaker();

        for (int i = 0; i < populationSize; i++) {
            double x = random.nextDouble(width);
            double y = random.nextDouble(length);
            boolean immune;
            if (this.random.nextInt(100) < immunePercentage) {
                immune = true;
            } else {
                immune = false;
            }
            population.add(new Person(immune, new Vector2D(x, y)));
        }

        this.setPreferredSize(new Dimension(length * SCALE_FACTOR + 20, width * SCALE_FACTOR + 20));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NextStep();
        repaint();
    }

    public int getCurrentStep() {
        return step;
    }

    public void stopSimulation() {
        this.timer.stop();
    }

    public void startSimulation() {
        this.timer.start();
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
       Iterator<InfectionProgress> spreadProgressList = InfectedList.getInstance().getInfections().iterator();

        while (spreadProgressList.hasNext()) {
            InfectionProgress infection = spreadProgressList.next();
            if (!infection.updateProgress()) {
                spreadProgressList.remove();
            }
        }

        ArrayList<Person> infectedList = InfectedList.getInstance().getInfectedList();

        for (int i = 0; i < infectedList.size(); i++) {
            for (int j = 0; j < this.population.size(); j++) {
                if (infectedList.get(i).getLocation().spreadPossible(this.population.get(j).getLocation())) {
                    if (InfectedList.getInstance().containsPPL(infectedList.get(i),this.population.get(j))) {
                        continue;
                    } else {
                        InfectedList.getInstance().getInfections().add(new InfectionProgress(infectedList.get(i), this.population.get(j)));
                    }
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
        int range = this.random.nextInt(100);
        return range < income;
    }

    public void addSave() {
        this.careTaker.saveMemento(save());
    }

    public void loadSave() {
        restore(this.careTaker.getLast());
        repaint();
    }

    //zapis
    public Memento save() {  
        return new Memento(this.population, step, InfectedList.getInstance().getInfections(), InfectedList.getInstance().getInfectedList());
    }
    
    public void restore(Memento memento) {
        step = memento.getStep();
        this.population = memento.getPopulation();
        InfectedList.getInstance().setInfectedList(memento.getInfectedList());
        InfectedList.getInstance().setInfections(memento.getInfections());
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
            g.setColor(person.getHealth().isInfected() ? Color.red : (person.getHealth().isImmune() ? Color.GREEN : Color.blue));
            g.fillOval(x, y, 8, 8); // Kropka reprezentująca osobę
        }
    }
}