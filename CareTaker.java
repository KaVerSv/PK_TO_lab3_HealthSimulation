import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private List<Memento> mementos;

    CareTaker() {
        this.mementos = new ArrayList<>();
    }

    public void saveMemento(Memento memento) {
        mementos.add(memento);
    }

    public Memento getMemento(int index) {
        return mementos.get(index);
    }

    public Memento getLast() {
        return mementos.get(mementos.size() - 1);
    }
}
