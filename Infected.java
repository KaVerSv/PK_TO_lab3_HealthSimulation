import java.util.Random;

public class Infected implements Health{
    private boolean symptops;
    private int recoveryTime;
    private Person sick;

    Infected(boolean symptops, Person person) {
        this.symptops = symptops;
        InfectedList.getInstance().addInfected(person);
        this.sick = person;

        Random random = new Random();
        int recoveryTime = (random.nextInt(11) + 20) * 25;
        this.recoveryTime = recoveryTime;
    }

    Infected(Infected source) {
        this.symptops = source.symptops;
        InfectedList.getInstance().addInfected(source.sick);
        this.sick = source.sick;

        Random random = new Random();
        int recoveryTime = (random.nextInt(11) + 20) * 25;
        this.recoveryTime = recoveryTime;
    }

    public void updateRecoveryTime() {
        this.recoveryTime -= 1;
        if (recoveryTime <= 0) {
            this.sick.recover();
        }
    }

    @Override
    public boolean isImmune() {
        return false;
    }

    @Override
    public boolean isInfected() {
        return true;
    }

    @Override
    public boolean haveSymptoms() {
        return this.symptops;
    }
}