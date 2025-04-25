package domaine;

import util.Util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;


public class Plat {

    private String nom;
    private  int nbPersonnes;
    private Difficulte niveauDeDifficulte;
    private Cout cout;
    private Duration dureeEnMinutes;

    private HashSet<IngredientQuantifie> ingredients;
    private ArrayList<Instruction> recette;

    private Type type;

    public Plat(String nom, int nbPersonnes, Difficulte niveauDeDifficulte, Cout cout, Type type) {
        this.nom = nom;
        this.nbPersonnes = nbPersonnes;
        this.niveauDeDifficulte = niveauDeDifficulte;
        this.cout = cout;
        this.dureeEnMinutes = Duration.ofMinutes(0);
        this.ingredients = new HashSet<IngredientQuantifie>();
        this.recette = new ArrayList<Instruction>();
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public Difficulte getNiveauDeDifficulte() {
        return niveauDeDifficulte;
    }

    public Cout getCout() {
        return cout;
    }

    public Duration getDureeEnMinutes() {
        return dureeEnMinutes;
    }




    public void insererInstruction(int position, Instruction instruction){
        Util.checkStrictlyPositive(position);
        Util.checkObject(instruction);

        if (position > recette.size() + 1)
            throw new IllegalArgumentException();

        recette.add(position - 1,instruction);
        this.dureeEnMinutes =  dureeEnMinutes.plus(instruction.getDureeEnMinutes());
    }
    public void ajouterInstruction (Instruction instruction){
        Util.checkObject(instruction);
        recette.add(instruction);
        this.dureeEnMinutes =  dureeEnMinutes.plus(instruction.getDureeEnMinutes());
    }

    public Instruction remplacerInstruction (int position, Instruction instruction){
        Util.checkStrictlyPositive(position);
        Util.checkObject(instruction);

        if( recette.size() < position)
            throw new IllegalArgumentException();

        Instruction instructionRemplacee = recette.set(position - 1,instruction);
        dureeEnMinutes = dureeEnMinutes.minus(instructionRemplacee.getDureeEnMinutes());
        dureeEnMinutes = dureeEnMinutes.plus(instruction.getDureeEnMinutes());

        return  instructionRemplacee;
    }

    public Instruction supprimerInstruction (int position){
        Util.checkStrictlyPositive(position);
        if( recette.size() < position)
            throw new IllegalArgumentException();

        Instruction instruction = recette.remove(position-1);
        this.dureeEnMinutes = dureeEnMinutes.minus(instruction.getDureeEnMinutes());
        return instruction;
    }

    public Iterator<Instruction> instructions(){

        return Collections.unmodifiableList(recette).iterator();

    }

    public boolean ajouterIngredient(Ingredient ingredient, int quantite, Unite unite) {
        IngredientQuantifie IngredientQuantifie = new IngredientQuantifie(ingredient, quantite, unite);
        if(ingredients.contains(ingredient))
            return false;
        return ingredients.add(IngredientQuantifie);


    }
    public boolean ajouterIngredient(Ingredient ingredient, int quantite) {
        return ajouterIngredient(ingredient, quantite, Unite.NEANT);
    }
    public boolean modifierIngredient(Ingredient ingredient, int quantite, Unite unite) {
        for(IngredientQuantifie iq: ingredients){
            if(iq.getIngredient().equals(ingredient)){
                iq.setQuantite(quantite);
                iq.setUnite(unite);
                return true;
            }
        }

        return false;

    }
    public boolean supprimerIngredient(Ingredient ingredient) {
        for(IngredientQuantifie iq: ingredients){
            if(iq.getIngredient().equals(ingredient)){
                ingredients.remove(iq);
                return true;
            }
        }
        return false;

    }
    public IngredientQuantifie trouverIngredientQuantifie(Ingredient ingredient) {
        for(IngredientQuantifie iq: ingredients){
            if(iq.getIngredient().equals(ingredient)){
                return iq;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String hms = String.format("%d h %02d m", dureeEnMinutes.toHours(), dureeEnMinutes.toMinutes()%60);
        String res = this.nom + "\n\n";
        res += "Pour " + this.nbPersonnes + " personnes\n";
        res += "Difficulté : " + this.niveauDeDifficulte + "\n";
        res += "Coût : " + this.cout + "\n";
        res += "Durée : " + hms + " \n\n";
        res += "Ingrédients :\n";
        for (IngredientQuantifie ing : this.ingredients) {
            res += ing + "\n";
        }
        int i = 1;
        res += "\n";
        for (Instruction instruction : this.recette) {
            res += i++ + ". " + instruction + "\n";
        }
        return res;
    }





    public enum Difficulte {
        X, XX, XXX, XXXX, XXXXX;

        @Override
        public String toString() {
            return "Difficulté : " + super.toString().replace("X", "*");
        }
    }

    public enum Cout {
        $,$$,$$$,$$$$,$$$$$;
        @Override
        public String toString() {
            return "Coût : " + super.toString().replace("$", "€");
        }
    }

    public enum Type {
        ENTREE, PLAT, DESSERT
    }


}
