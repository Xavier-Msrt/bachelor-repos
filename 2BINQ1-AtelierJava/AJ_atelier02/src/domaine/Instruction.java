package domaine;

import java.time.Duration;

public class Instruction {
    private String descrption;
    private Duration dureeEnMinutes;

    public Instruction(String descrption, int dureeEnMinutes) {
        this.descrption = descrption;
        this.dureeEnMinutes = Duration.ofMinutes(dureeEnMinutes);
    }

    @Override
    public String toString() {
        return "("+ dureeEnMinutes.toHours()+":"+dureeEnMinutes.toMinutes()+") "+ descrption;
    }

    public String getDescrption() {
        return descrption;
    }

    public Duration getDureeEnMinutes() {
        return dureeEnMinutes;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public void setDureeEnMinutes(Duration dureeEnMinutes) {
        this.dureeEnMinutes = dureeEnMinutes;
    }
}
