package be.vinci.domaine;

import java.util.Set;

public class StageStub implements Stage {

    private int nbSemaine;
    private Sport sport;
    private  Moniteur moniteur;

    public StageStub(int nbSemaine, Sport sport, Moniteur moniteur) {
        this.nbSemaine = nbSemaine;
        this.sport = sport;
        this.moniteur = moniteur;
    }

    @Override
    public String getIntitule() {
        return null;
    }

    @Override
    public String getLieu() {
        return null;
    }

    @Override
    public int getNumeroDeSemaine() {
        return nbSemaine;
    }

    @Override
    public Sport getSport() {
        return sport;
    }

    @Override
    public boolean enregistrerMoniteur(Moniteur moniteur) {
        return false;
    }

    @Override
    public boolean supprimerMoniteur() {
        return false;
    }

    @Override
    public Moniteur getMoniteur() {
        return moniteur;
    }

    @Override
    public boolean ajouterEnfant(Enfant enfant) {
        return false;
    }

    @Override
    public boolean supprimerEnfant(Enfant enfant) {
        return false;
    }

    @Override
    public boolean contientEnfant(Enfant enfant) {
        return false;
    }

    @Override
    public int nombreDEnfants() {
        return 0;
    }

    @Override
    public Set<Enfant> enfants() {
        return null;
    }
}
