import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainPizzeria {

    public static void main(String[] args) {
        Client emmeline = new Client("Leconte", "Emmeline", "0488/98.23.85");
        Client stephanie = new Client("Ferneeuw", "Stéphanie", "0475/51.30.80");
        Commande commandeEmmeline = new Commande(emmeline);
        System.out.println("Commande en cours d'Emmeline : " +emmeline.getCommandeEnCours());
        System.out.println();
        try {
            new Commande(emmeline);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        System.out.println();
        commandeEmmeline.ajouter(MenuPizzeria.PIZZA_DUCHEF, 2);
        PizzaComposable pizzaComposable = new PizzaComposable(emmeline);
        pizzaComposable.ajouter(Ingredients.AUBERGINES);
        pizzaComposable.ajouter(Ingredients.TOMATE);
        pizzaComposable.ajouter(Ingredients.GORGONZOLA);
        emmeline.cloturerCommandeEnCours();
        System.out.println(commandeEmmeline);
        System.out.println(commandeEmmeline.detailler());
        System.out.println();
        System.out.println("Commande en cours d'Emmeline : " +emmeline.getCommandeEnCours());
        System.out.println();
        System.out.println("ajout d'une pizza à une commande clôturée : " +commandeEmmeline.ajouter(MenuPizzeria.PIZZA_4FROMAGES) );
        System.out.println();
        Commande commandeStephanie = new Commande(stephanie);
        commandeStephanie.ajouter(MenuPizzeria.PIZZA_MARGARITA, 1);
        commandeStephanie.ajouter(MenuPizzeria.PIZZA_MARGARITA);
        commandeStephanie.ajouter(MenuPizzeria.PIZZA_MARINIERE, 3);
        System.out.println(commandeStephanie);
        System.out.println(commandeStephanie.detailler());
        System.out.println("Montant de la commande de Stéphanie : " + commandeStephanie.calculerMontantTotal());
        System.out.println();
        Commande commandeEmmeline2 = new Commande(emmeline);
        pizzaComposable = new PizzaComposable(emmeline);
        pizzaComposable.ajouter(Ingredients.JAMBON);
        pizzaComposable.ajouter(Ingredients.TOMATE);
        pizzaComposable.ajouter(Ingredients.OLIVES);
        pizzaComposable.ajouter(Ingredients.MOZZARELLA);
        System.out.println(commandeEmmeline2);


        System.out.println("------------------------------------> Test Commmande passe ");
        printCommandePassePar(emmeline);
        printCommandePassePar(stephanie);
        stephanie.cloturerCommandeEnCours();
        printCommandePassePar(stephanie);


        System.out.println("------------------------------------> Test retirer pizza");
        emmeline.cloturerCommandeEnCours();
        Commande commandeEmmeline3 = new Commande(emmeline);
        commandeEmmeline3.ajouter(MenuPizzeria.PIZZA_DUCHEF, 2);
        commandeEmmeline3.ajouter(MenuPizzeria.PIZZA_4FROMAGES, 1);
        System.out.println(commandeEmmeline3.detailler());
        System.out.println("On retire 1 chef");
        commandeEmmeline3.retirer(MenuPizzeria.PIZZA_DUCHEF);
        System.out.println(commandeEmmeline3.detailler());
        System.out.println("On retire 2 PIZZA_4FROMAGES");
        System.out.println("Retirer 2 4 formage alors que il y a que 1: "+commandeEmmeline3.retirer(MenuPizzeria.PIZZA_4FROMAGES, 2));

        System.out.println(commandeEmmeline3.detailler());
        commandeEmmeline3.retirer(MenuPizzeria.PIZZA_4FROMAGES, 1);
        System.out.println(commandeEmmeline3.detailler());


        System.out.println("------------------------------------> Test supprimer pizza");
        emmeline.cloturerCommandeEnCours();

        System.out.println("Peux t'on supprimer une pizza d'une commande terminer "+ commandeEmmeline3.supprimer(MenuPizzeria.PIZZA_DUCHEF));
        Commande commandeEmmeline4 = new Commande(emmeline);
        commandeEmmeline4.ajouter(MenuPizzeria.PIZZA_4FROMAGES);
        System.out.println(commandeEmmeline3.detailler());
        System.out.println(commandeEmmeline4.supprimer(MenuPizzeria.PIZZA_4FROMAGES));
        System.out.println(commandeEmmeline4.detailler());




    }

     static void printCommandePassePar(Client client){
        System.out.println("=======================");
        ArrayList<Commande> commandePasse = client.getCommandesPasses();
        if(commandePasse.isEmpty())
            System.out.println("Pas de commande passe pour "+client.getNom());
        else
            System.out.println("Le ou les commandes passe de" +client.getNom()+ "sont ");
        for (Commande commande: commandePasse){
            System.out.println(commande);
        }
    }

}
