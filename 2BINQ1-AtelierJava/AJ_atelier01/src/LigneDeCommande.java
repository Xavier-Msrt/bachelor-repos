public class LigneDeCommande {

    private Pizza pizza;
    private int quantite;
    private double prixUnitaire;

    public LigneDeCommande(Pizza pizza, int quantite) {
        if(pizza == null || quantite <= 0)
            throw new IllegalArgumentException();

        this.pizza = pizza;
        this.quantite = quantite;
        this.prixUnitaire = pizza.calculerPrix();
    }

    public Pizza getPizza() {
        return pizza;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        if(quantite <= 0)
            throw new IllegalArgumentException();
        this.quantite = quantite;
    }
    public double calculerPrixTotal(){
        return this.quantite * this.prixUnitaire;
    }

    public String toString() {
        return  quantite + " " + pizza.getTitre() + "  à " + prixUnitaire ;
    }
}
