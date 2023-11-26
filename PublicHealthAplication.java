import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PublicHealthAplication {
    public static void main(String[] args) {
        
        //parametry symulacji
        int population = 50;
        boolean immune = false;
        InfectedList.getInstance();
        Simulation symulacja = new Simulation(population, immune);

        //
        //parametry gui
        //
        JFrame frame = new JFrame("PublicHealthAplication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //pasek narzedzi
        JToolBar toolBar = new JToolBar();
        
        // Przyciski na pasku narzędziowym
        JButton startButton = new JButton("Play Simulation");
        JButton playStopButton = new JButton("Stop Simulation");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        // Dodajemy przyciski do paska narzędziowego
        toolBar.add(startButton);
        toolBar.add(playStopButton);
        toolBar.addSeparator(); // Separator między przyciskami
        toolBar.add(saveButton);
        toolBar.add(loadButton);

        // Dodajemy pasek narzędziowy do górnej części okna
        frame.add(toolBar, BorderLayout.NORTH);
        frame.pack();

        // Dodajemy panel symulacji bezpośrednio do zawartości panelu głównego
        //frame.getContentPane().add(symulacja);
        frame.add(symulacja);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symulacja.startSimulation();
            }
        });
        
        playStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symulacja.stopSimulation();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symulacja.addSave();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symulacja.loadSave();
            }
        });
        
        frame.setVisible(true);
    }
}