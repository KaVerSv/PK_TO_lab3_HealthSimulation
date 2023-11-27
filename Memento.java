import java.util.ArrayList;

class Memento {
    private final ArrayList<Person> population;
    private final ArrayList<InfectionProgress> infections;
    private final ArrayList<Person> infectedList;
    private final int step;

    public Memento(ArrayList<Person> population, int step, ArrayList<InfectionProgress> infections, ArrayList<Person> infectedList)  {
        this.step = step;
        this.population = deepcopArrayList(population);
        this.infections = deepcopyInfected(infections);
        this.infectedList = new ArrayList<>();
        for (Person element : infectedList) {
            this.infectedList.add(new Person(element));
        }
    }

    public ArrayList<Person> getPopulation() {
        return new ArrayList<>(population);
    }

    public int getStep() {
        return step;
    }

    public ArrayList<InfectionProgress> getInfections() {
        return new ArrayList<>(infections);
    }

    public ArrayList<Person> getInfectedList() {
        return new ArrayList<>(infectedList);
    }

    private ArrayList<Person> deepcopArrayList(ArrayList<Person> oryginalnaLista) {
        ArrayList<Person> nowaLista = new ArrayList<>();
        
        for (Person element : oryginalnaLista) {
            nowaLista.add(new Person(element));
        }
        return nowaLista;
    }



    private ArrayList<InfectionProgress> deepcopyInfected(ArrayList<InfectionProgress> oryginalnaLista) {
        ArrayList<InfectionProgress> nowaLista = new ArrayList<>();
        
        for (InfectionProgress element : oryginalnaLista) {
            nowaLista.add(new InfectionProgress(element));
        }
        return nowaLista;
    }
}