import java.util.Random;
import vectors_custom.*;

public class Person {
    private Health health;
    private Random rand;
    
    private Vector2D location;

    public Person(boolean immune, Vector2D location) {
        this.health = new NotInfected(immune);
        this.rand = new Random();
        this.location = location;
    }

    public Vector2D getLocation() {
        return this.location;
    }

    // for pupulation grow
    public Person() {
        this.rand = new Random();
        if (getInfection()) {
            this.health = new Infected(this.rand.nextBoolean(), this);
        } else {
            this.health = new NotInfected(false);
        }
        
        double x;
        double y;
        
        if (this.rand.nextBoolean()) {
            x = Simulation.width;
            if (this.rand.nextBoolean()) {
                x = -x;
                y = this.rand.nextDouble(Simulation.length);
            } else {
                y = this.rand.nextDouble(Simulation.length);
            }
        } else {
            y = Simulation.length;
            if (this.rand.nextBoolean()) {
                y = -y;
                x = this.rand.nextDouble(Simulation.width);
            } else {
                x = this.rand.nextDouble(Simulation.width);
            }
        }
        this.location = new Vector2D(x, y);
    }

    private boolean getInfection() {
        this.rand = new Random();
        //liczba od 0 do 100
        double range = this.rand.nextDouble() * 100;

        return range < 10;
    }

    public Health getHealth() {
        return this.health;
    }

    public Health setHealth(Health health) {
        return this.health = health;
    }

    public boolean mapEdge() {
        return rand.nextBoolean();
    }

    public void gainInfection(Person infected) { 

        if (this.health.isImmune() == false) {
            if (infected.getHealth().haveSymptoms()) {
                if (rand.nextBoolean()) {
                    setHealth(new Infected(false, this));
                } else {
                    setHealth(new Infected(true, this));
                }
            } else {
                if (rand.nextBoolean()) {
                    if (rand.nextBoolean()) {
                        setHealth(new Infected(false, this));
                    } else {
                        setHealth(new Infected(true, this));
                    }
                }
            }
        }
    }

    public void recover() {
        this.health = new NotInfected(true);
        InfectedList.getInstance().removeInfected(this);
    }

    // on true Person leaves Simulation area
    public Boolean move() {
        //max 2,5 m/s w 25 krokach == 0.1 na krok
        double maksymalnePrzesuniecie = 0.1;

        double x = (this.rand.nextDouble() * 2 - 1) * maksymalnePrzesuniecie;
        double y = (this.rand.nextDouble() * 2 - 1) * maksymalnePrzesuniecie;

        double[] save = this.location.getComponents();
        IVector change = new Vector2D(x, y);
        this.location.add(change);
        
        double[] tmp = this.location.getComponents(); 
        if (tmp[0] > Simulation.width || tmp[0] < 0 || tmp[1] > Simulation.length || tmp[1] < 0) {
            if (this.rand.nextBoolean()) {
                this.location = new Vector2D(save[0], save[1]);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}