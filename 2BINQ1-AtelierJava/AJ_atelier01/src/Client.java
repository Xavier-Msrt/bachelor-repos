import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Client implements Iterable<Commande>{

    private static int numeroSuivant = 1;
    private int numero;
    private String nom;
    private String prenom;
    private String telephone;
    private Commande commandeEnCours;
    private ArrayList<Commande> commandesPasses;

    public Client(String nom, String prenom, String telephone) {
        if(nom == "" || prenom == "" || telephone == "")
            throw new IllegalArgumentException();


        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.numero = this.numeroSuivant;
        this.numeroSuivant++;
        this.commandeEnCours = null;
        commandesPasses = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public Commande getCommandeEnCours() {
        return commandeEnCours;
    }


    public ArrayList<Commande> getCommandesPasses() {
        return commandesPasses;
    }

    public boolean enregister(Commande commande){
        if (this.commandeEnCours != null)
            return false;
        if(!this.equals(commande.getClient()))
            return false;
        this.commandeEnCours = commande;
        return true;
    }
    public boolean cloturerCommandeEnCours(){
        if(this.commandeEnCours == null)
            return false;
        if(commandesPasses.contains(this.commandeEnCours))
            return false;
        
        commandesPasses.add(this.commandeEnCours);
        this.commandeEnCours = null;
        return true;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return numero == client.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
    @Override
    public String toString() {
        return "client n° " + numero + " (" + prenom  + " " + nom + ", telephone : " + telephone +")";
    }

    @Override
    public Iterator<Commande> iterator() {
        return commandesPasses.iterator();
    }
}
