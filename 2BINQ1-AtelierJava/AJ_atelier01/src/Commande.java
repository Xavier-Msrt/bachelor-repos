import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Iterator;

public class Commande implements Iterable<LigneDeCommande>{
    private static int numeroSuivant = 1;
    private int numero;
    private Client client;
    private LocalDateTime date;
    private ArrayList<LigneDeCommande> ligneDeCommandes;

    public Commande(Client client) {
        if (client == null)
            throw new IllegalArgumentException();

        if(client.getCommandeEnCours() != null)
            throw new IllegalArgumentException("impossible de créer une commande pour un client ayant encore une commande en cours");
        this.client = client;
        client.enregister(this);
        this.numero = numeroSuivant;
        this.numeroSuivant++;
        this.date = LocalDateTime.now();
        this.ligneDeCommandes = new ArrayList<>();

    }

    public int getNumero() {
        return numero;
    }

    public Client getClient() {
        return client;
    }

    public LocalDateTime getDate() {
        return date;
    }


    public boolean ajouter(Pizza pizza, int quantite){
        if (pizza == null)
            throw new IllegalArgumentException();
        if (quantite <= 0)
            throw new IllegalArgumentException();
        if(client.getCommandeEnCours() != this)
            return false;

        for(LigneDeCommande ligneDeCommande: ligneDeCommandes){
            if(ligneDeCommande.getPizza().equals(pizza)){
                ligneDeCommande.setQuantite(ligneDeCommande.getQuantite()+ quantite);
                return true;
            }
        }

        return ligneDeCommandes.add(new LigneDeCommande(pizza, quantite));

    }

    public boolean ajouter(Pizza pizza){
        return ajouter(pizza, 1);
    }
    public double calculerMontantTotal(){
        double somme = 0;
        for(LigneDeCommande ligneDeCommande: ligneDeCommandes){
            somme +=  ligneDeCommande.calculerPrixTotal();
        }
        return somme;
    }

    public String detailler(){
        String text = "";
        for(LigneDeCommande ligneDeCommande: ligneDeCommandes){
            text = text + "\n" + ligneDeCommande.toString();
        }
        return text;
    }

    public boolean retirer(Pizza pizza, int quantite){
        if (pizza == null)
            throw new IllegalArgumentException();
        if (quantite <= 0)
            throw new IllegalArgumentException();

        if(client.getCommandeEnCours() == null)
            return false;

        LigneDeCommande ligneDeCommande = null;
        for (LigneDeCommande ligne: ligneDeCommandes){
            if(ligne.getPizza().equals(pizza)){
                int quantiteLigne = ligne.getQuantite();

                if(quantiteLigne < quantite)
                    return false;
                if(quantiteLigne == quantite) {
                    ligneDeCommande = ligne;
                    break;
                }
                ligne.setQuantite(quantiteLigne - quantite);
                return true;
            }

        }
        if (ligneDeCommande != null){
           return ligneDeCommandes.remove(ligneDeCommande);
        }
        return false;
    }

        public boolean retirer(Pizza pizza){
            return retirer(pizza, 1);
        }

        public boolean supprimer(Pizza pizza){
            if (pizza == null)
                throw new IllegalArgumentException();

            if(client.getCommandeEnCours() == null)
                return false;

            LigneDeCommande ligneDeCommande = null;
            for (LigneDeCommande ligne: ligneDeCommandes){
                if(ligne.getPizza().equals(pizza)){
                    ligneDeCommande = ligne;
                    break;
                }
            }
            if (ligneDeCommande != null){
                return ligneDeCommandes.remove(ligneDeCommande);
            }
            return false;
        }




    @Override
    public Iterator<LigneDeCommande> iterator() {
        return ligneDeCommandes.iterator();
    }


    public String toString() {
        DateTimeFormatter formater = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        String encours = "";
        if (client.getCommandeEnCours() == this)
            encours = " (en cours)";
        return "Commande n° " + numero + encours + " du " + client + "\ndate : " + formater.format(date);
    }
}
