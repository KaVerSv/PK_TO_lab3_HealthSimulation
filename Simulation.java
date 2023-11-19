import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

import vectors_custom.Vector2D;

public class Simulation extends JFrame implements KeyListener{
    public static final double length = 100;
    public static final double width = 100;
    private int populationSize;
    private AtomicInteger step;

    private ArrayList<Person> population;
    private ArrayList<InfectionProgress> spreadProgressList;

    private Random random;
    private final double income = 0.9;

    //działanie symulacji
    //synchronizacja między wątkami
    private volatile boolean simulationRunning = false;

    //saving
    private String content;

    public Simulation(int populationSize, boolean immune) {
        // Inicjalizacja okna, dodanie KeyListener
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        this.populationSize = populationSize;
        this.step = new AtomicInteger(0);

        this.population = new ArrayList<Person>();
        this.spreadProgressList = new ArrayList<InfectionProgress>();
        

        this.random = new Random();

        for (int i = 0; i < this.populationSize; i++) {
            double x = random.nextDouble(width + 1);
            double y = random.nextDouble(length + 1);
            population.add(new Person(immune, new Vector2D(x, y)));
        }
    }

    public Integer getCurrentStep() {
        return this.step.get();
    }

    public void startSimulation() {
        simulationRunning = true;
        // Tworzenie i start wątku
        Thread simulationThread = new Thread(() -> {
            while (simulationRunning) {
                update();
                repaint(); // Potrzebne, jeśli korzystasz z rysowania na ekranie
                try {
                    Thread.sleep(100); // Odczekaj 100 milisekund przed kolejnym krokiem symulacji
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        simulationThread.start();
    }
 
    //krok symulacji
    private void update() {
      
        movement();
        spreadDisease();
        updateRecoveryProgress(); 
        //wykonanie kroku
        this.step.addAndGet(1); 
    }

    //ruch jednostek Person
    private void movement() {
    java.util.Iterator<Person> iterator = this.population.iterator();
    while (iterator.hasNext()) {
        Person person = iterator.next();
        //opuszczenie obszaru symulacji
        if (person.move()) {
            iterator.remove();
        }
        //przyrost populacji
        if (growPopulation()) {
            this.population.add(new Person());
        }
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
            for (int j = 0; j < this.populationSize; j++) {
                if (this.population.get(j).getHealth().isInfected() && InfectedList.getInstance().getInfectedList().get(i).getLocation().spreadPossible(this.population.get(j).getLocation())) {
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

    //obsługa przycisków
    @Override
    public void keyTyped(KeyEvent e) {
        // Niepotrzebne implementacje KeyListener, ale wymagane zgodnie z interfejsem
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Jeśli naciśniesz klawisz P, zmień stan symulacji
        if (e.getKeyCode() == KeyEvent.VK_P) {
            simulationRunning = !simulationRunning;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Niepotrzebne implementacje KeyListener, ale wymagane zgodnie z interfejsem
    }
}