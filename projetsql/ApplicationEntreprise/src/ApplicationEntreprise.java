import java.sql.*;
import java.util.Scanner;

import static java.sql.DriverManager.*;


public class ApplicationEntreprise {

    private Scanner scanner;
    private String identifiant;
    private PreparedStatement connexion;
    private PreparedStatement ajouterOffreStage ;
    private PreparedStatement voirMotCle;
    private PreparedStatement ajouterMotCle;
    private PreparedStatement voirOffreStage;
    private PreparedStatement afficherCandidatures;
    private PreparedStatement selectionnerEtudiant;
    private PreparedStatement annulerOffreStage;


    public ApplicationEntreprise(){

        scanner = new Scanner(System.in);

        // ajouter Build Path
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL manquant !");
            System.exit(1);
        }

        String url = "jdbc:postgresql://172.24.2.6:5432/dbxaviermassart";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, "chisomubah", "JOVNQI5YU");
        } catch (
                SQLException e) {
            System.out.println("Impossible de joindre le server !");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            //1
            ajouterOffreStage = conn.prepareStatement("SELECT projet.ajouter_offre_stage (?, ?, ?)");

            //2
            voirMotCle= conn.prepareStatement("SELECT mc.mot_cle FROM projet.mots_cle mc");

            //3
            ajouterMotCle = conn.prepareStatement("SELECT projet.ajout_mot_cle(?,?,?)");

            //4
            voirOffreStage = conn.prepareStatement("SELECT offreur, code, description, semestre, etat, nb_candidat, attribue FROM projet.voir_offres_stage os WHERE os.offreur = ? ");

            //5
            afficherCandidatures = conn.prepareStatement("SELECT offreur, code, etat, nom, prenom, email, motivation FROM projet.voir_candidature_offre vco  WHERE vco.offreur = ? AND vco.code = ?;");

            //6
            selectionnerEtudiant = conn.prepareStatement("SELECT projet.selectionner_etudiant_offre(?, ?, ?)");

            //7
            annulerOffreStage= conn.prepareStatement("SELECT projet.annuler_offre_stage(?,?)");

            //8
            connexion = conn.prepareStatement("SELECT en.mdp FROM projet.entreprises en WHERE en.identifiant = ?");
        }
        catch (SQLException se){
            System.out.println("Erreur lors de la creation d'offre de stage ");
            se.printStackTrace();
            System.exit(1);
        }
    }


    public void demarrer() {
        while (true) {
            System.out.println("|-------< Application Entreprise >-------|");
            System.out.println("\t1) Se connecter");
            System.out.println("\tAutre chiffre -> Quitter");
            System.out.println();

            String input;
            do {
                System.out.print("Entrez votre choix : ");
                input = scanner.nextLine();
            } while (!input.matches("\\d+"));

            int choix = Integer.parseInt(input);

            System.out.println();
            switch (choix) {
                case 1:
                    seConnecter();
                    break;
                default:
                    return;
            }
            break;
        }
    }

    public void menu() {

        int choix;

        do{
            System.out.println();
            System.out.println("-------< Menu >-------");
            System.out.println("\t1) Encoder un offre de stage.");
            System.out.println("\t2) Voir les mots-clés disponibles.");
            System.out.println("\t3) Ajouter un mot-clé à une de ses offres de stage.");
            System.out.println("\t4) Voir ses offres de stages.");
            System.out.println("\t5) Voir les candidatures pour une de ses offres de stages en donnant son code.");
            System.out.println("\t6) Sélectionner un étudiant pour une de ses offres de stage.");
            System.out.println("\t7) Annuler une offre de stage en donnant son code.");
            System.out.println("\t8) Se déconnecter");
            System.out.println("\tAutre chiffre -> Fermer le menu");

            System.out.print("Entrer votre choix : ");
            choix = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (choix) {
                case 1:
                    demandeCreationOS();
                    break;
                case 2:
                    getMotscles();
                    break;
                case 3:
                    demandeCreationMC();
                    break;
                case 4:
                    afficherOS();
                    break;
                case 5:
                    afficherLesCandidatures();
                    break;
                case 6:
                    selectionnerUnEtudiant();
                    break;
                case 7:
                    annulerUneOffreStage();
                    break;
                case 8:
                    identifiant = null;
                    seConnecter();
                default:
                    break;
            }
            System.out.println();
        }while (choix > 0 && choix < 9);
    }

    public void seConnecter() {

        System.out.println("----< Se connecter >----");

        System.out.print("Entrer votre identifant : ");
        String username = scanner.nextLine().toUpperCase();

        System.out.print("Entrer votre mot de passe : ");
        String mdp = scanner.nextLine();

        try {
            connexion.setString(1, username);
             ResultSet rs = connexion.executeQuery();
            if(rs.next()) {
                if(BCrypt.checkpw(mdp, rs.getString(1))) {
                    identifiant = username;
                    menu();
                }
            }
            if(identifiant == null || identifiant.isEmpty() ) {
                System.out.println("Votre mot de passe ou votre identifiant est incorrect");
                seConnecter();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void demandeCreationOS() {

        System.out.println("------< Encoder une offre de stage >------");

        System.out.print("Entrer le semestre : ");
        String semestre = scanner.nextLine().toUpperCase();

        System.out.print("Entrer la description : ");
        String description = scanner.nextLine();

        try {
            ajouterOffreStage.setString(1, identifiant);
            ajouterOffreStage.setString(2, description);
            ajouterOffreStage.setString(3, semestre);

            ResultSet rs = ajouterOffreStage.executeQuery();
            if(rs.next()){
                System.out.println("La creation de l'offre de stage a fonctionné");
            }
        }
        catch (SQLException se){
            System.out.println(se.getMessage());
        }
    }


    public void getMotscles() {
        System.out.println("------< Voir les mots clé disponible >------");

        try (ResultSet rs = voirMotCle.executeQuery()) {
            while (rs.next()) {
                System.out.println("---");
                System.out.println("Mots clé : "+rs.getString(1));
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public void demandeCreationMC() {
        System.out.println("------< Ajouter un mot-clé à une de ses offres de stage >------");

        System.out.print("Entrer le nouveau mot clé : ");
        String motCle = scanner.nextLine().toLowerCase();

        System.out.print("Entrer le code de l'offre : ");
        String code = scanner.nextLine().toUpperCase();

        try {
            ajouterMotCle.setString(1, identifiant);
            ajouterMotCle.setString(2, motCle);
            ajouterMotCle.setString(3, code);

            ResultSet rs =  ajouterMotCle.executeQuery();
            if(rs.next()){
                System.out.println("La creation du mots clé a fonctionné");
            }
        }
        catch (SQLException se){
            System.out.println(se.getMessage());
        }
    }


    public void afficherOS(){
        System.out.println("------< Voir ses offres de stages >------");


        try{
            voirOffreStage.setString(1,identifiant);
            ResultSet rs = voirOffreStage.executeQuery();

            while (rs.next()){
                System.out.println("---");
                System.out.println("Code de l'offre : "+rs.getString(2));
                System.out.println("\tDescription de l'offre : "+rs.getString(3));
                System.out.println("\tSemestre de l'offre : "+rs.getString(4));
                System.out.println("\tEtat de l'offre : "+rs.getString(5));
                System.out.println("\tNombre de candidature en attente : "+rs.getString(6));
                System.out.println("\tNom de l'etudiant attribué : "+rs.getString(7));
            }
        }
        catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public void afficherLesCandidatures(){
        System.out.println("------< Voir les candidatures pour une de ses offres de stages >------");

        System.out.print("Entrer le code de l'offre : ");
        String code = scanner.nextLine().toUpperCase();

        try{
            afficherCandidatures.setString(1, identifiant);
            afficherCandidatures.setString(2, code);
            ResultSet rs = afficherCandidatures.executeQuery();

            if(rs.next()){
                do{
                    System.out.println("---");
                    System.out.println("Etat de la candidature : "+rs.getString(3));
                    System.out.println("\tNom de l'étudiant : "+rs.getString(4));
                    System.out.println("\tPrenom de l'étudiant : "+rs.getString(5));
                    System.out.println("\tEmail de l'étudiant : "+rs.getString(6));
                    System.out.println("\tMotivation de l'étudiant : "+rs.getString(7));
                }while(rs.next());
            }else{
                System.out.println("Il n'y a pas de candidatures pour cette offre ou vous n'avez pas d'offre ayant ce code");
            }

        }
        catch (SQLException se){
            System.out.println(se.getMessage());
        }
    }


    public void selectionnerUnEtudiant(){
        System.out.println("------< Sélectionner un étudiant pour une de ses offres de stage >------");

        System.out.print("Entrer le code de l’offre : ");
        String code = scanner.nextLine().toUpperCase();

        System.out.print("Entrer l'adresse mail de l'étudiant : ");
        String email = scanner.nextLine();

        try{
            selectionnerEtudiant.setString(1,identifiant);
            selectionnerEtudiant.setString(2,code);
            selectionnerEtudiant.setString(3,email);

            ResultSet rs = selectionnerEtudiant.executeQuery();
            if(rs.next()){
                System.out.println("Vous avez sélectionnez un étudiant avec success !");
            }
        }
        catch (SQLException se){
            System.out.println(se.getMessage());
        }
    }
    public void annulerUneOffreStage(){
        System.out.println("------< Annuler une offre de stage en donnant son code >------");

        System.out.print("Entrer le code de l'offre : ");
        String code = scanner.nextLine().toUpperCase();

        try{
            annulerOffreStage.setString(1,identifiant);
            annulerOffreStage.setString(2,code);
            ResultSet rs = annulerOffreStage.executeQuery();
            if(rs.next()){
                System.out.println("Vous avez annuler une offre de stage avec success !");
            }
        }
        catch (SQLException se){
            System.out.println(se.getMessage());
        }
    }

}


