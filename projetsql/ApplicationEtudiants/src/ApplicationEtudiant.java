import java.sql.*;
import java.util.Scanner;

import static java.sql.DriverManager.getConnection;

public class ApplicationEtudiant {
    private int idEtudiant = 0;
    private Connection conn = null;
    private Scanner scanner = new Scanner(System.in);
    private PreparedStatement seConnecter;
    private PreparedStatement voirOffresStagesValideeEtudiant;
    private PreparedStatement rechercheOffreStageMotCle;
    private PreparedStatement poserCandidature;
    private PreparedStatement voirOffresStagesCandidaturesDepose;
    private PreparedStatement annulerCandidature;

    ApplicationEtudiant() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL manquant !");
            System.exit(1);
        }

        String url = "jdbc:postgresql://172.24.2.6:5432/dbxaviermassart";

        try {
            conn = DriverManager.getConnection(url, "thibautdevos", "7CQFVY45O");
        } catch (SQLException e) {
            System.out.println("Impossible de joindre le server !");
            System.exit(1);
        }

        // Create preapreStatement
        try {
            seConnecter = conn.prepareStatement("SELECT id_etudiant, mdp FROM projet.etudiants WHERE email = ?");
            voirOffresStagesValideeEtudiant = conn.prepareStatement("SELECT code, nom_entreprise, adresse_entreprise, description_stage, mots_cle FROM projet.voir_offres_stages_validee(?) t(code VARCHAR(50), nom_entreprise VARCHAR(100), adresse_entreprise VARCHAR(100), description_stage TEXT, mots_cle TEXT)");
            rechercheOffreStageMotCle = conn.prepareStatement("SELECT code, nom_entreprise, adresse_entreprise, description_stage, mots_cle FROM projet.recherche_offre_stage_mot_cle(?,?) t(code VARCHAR(50), nom_entreprise VARCHAR(100), adresse_entreprise VARCHAR(100), description_stage TEXT, mots_cle TEXT)");
            poserCandidature = conn.prepareStatement("SELECT * FROM projet.poser_candidature(?,?,?)");
            voirOffresStagesCandidaturesDepose = conn.prepareStatement("SELECT * FROM projet.voir_offres_stages_candidatures_depose od WHERE od.etudiant = ?");
            annulerCandidature = conn.prepareStatement("SELECT * FROM projet.annuler_candidature(?,?)");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void demarrer() {
        while (true) {
            System.out.println("|-------< Application Etudiant >-------|");
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
            System.out.println("\t1) Voir toutes les offres de stage validée");
            System.out.println("\t2) Rechercher une offre de stage par mot clé");
            System.out.println("\t3) Poser sa candidature");
            System.out.println("\t4) Voir les offres de stage pour lesquels j'ai déposé ma candidature");
            System.out.println("\t5) Annuler une candidature");
            System.out.println("\t6) Se déconnecter");
            System.out.println("\tAutre chiffre -> Quitter");

            String input;
            do {
                System.out.print("Entrez votre choix : ");
                input = scanner.nextLine();
            } while(!input.matches("\\d+"));

            choix = Integer.parseInt(input);
            System.out.println();
            switch (choix) {
                case 1:
                    voirOffresStagesValideeEtudiant();
                    break;
                case 2:
                    rechercheOffreStageMotCle();
                    break;
                case 3:
                    poserCandidature();
                    break;
                case 4:
                    voirOffresStagesCandidaturesDepose();
                    break;
                case 5:
                    annulerCandidature();
                    break;
                case 6:
                    this.idEtudiant = 0;
                    seConnecter();
                default:
                    return;
            }

        }while (choix > 0 && choix < 7);
    }

    public void seConnecter() {
        System.out.println();
        System.out.println("----< Se connecter >----");

        System.out.print("Entrez votre email : ");
        String email = scanner.nextLine();

        System.out.print("Entrez votre mot de passe : ");
        String mdp = scanner.nextLine();

        try {
            seConnecter.setString(1, email);

            ResultSet rs = seConnecter.executeQuery();
            if (rs.next()) {
                if (BCrypt.checkpw(mdp, rs.getString(2))) {
                    idEtudiant = rs.getInt(1);
                }
            }

            if (idEtudiant == 0) {
                System.out.println("L'email ou le mot de passe est incorrect");
                seConnecter();
            } else {
                menu();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void voirOffresStagesValideeEtudiant() {

        System.out.println("------< Voir les offres de stages validées pendant mon semestre >------");

        try {
            voirOffresStagesValideeEtudiant.setInt(1, idEtudiant);

            ResultSet rs = voirOffresStagesValideeEtudiant.executeQuery();
            while (rs.next()) {
                System.out.println("---");
                System.out.println("Code de l'offre de stage : " + rs.getString(1) + "\nNom de l'entreprise : " + rs.getString(2) + "\nAdresse de l'entreprise : " + rs.getString(3) + "\nDescription de l'offre : " + rs.getString(4) + "\nMots clés : " + rs.getString(5));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void rechercheOffreStageMotCle() {

        System.out.println("------< Voir les offres de stages validées pendant mon semestre par mot clé >------");

        System.out.print("Entrer un mot clé : ");
        String motCle = scanner.nextLine().toLowerCase();

        try {
            rechercheOffreStageMotCle.setInt(1, idEtudiant);
            rechercheOffreStageMotCle.setString(2, motCle);

            ResultSet rs = rechercheOffreStageMotCle.executeQuery();
            while (rs.next()) {
                System.out.println("---");
                System.out.println("Code de l'offre de stage : " + rs.getString(1) + "\nNom de l'entreprise : " + rs.getString(2) + "\nAdresse de l'entreprise : " + rs.getString(3) + "\nDescription de l'offre : " + rs.getString(4) + "\nMots clés : " + rs.getString(5));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void poserCandidature() {

        System.out.println("------< Poser une candidature >------");

        System.out.print("Entrer le code de l'offre de stage : ");
        String codeOffre = scanner.nextLine().toUpperCase();

        System.out.print("Entrer vos motivations : ");
        String motivations = scanner.nextLine();

        try {
            poserCandidature.setInt(1, idEtudiant);
            poserCandidature.setString(2, codeOffre);
            poserCandidature.setString(3, motivations);

            ResultSet rs = poserCandidature.executeQuery();
            while (rs.next()) {
                System.out.println("Candidature déposée");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void voirOffresStagesCandidaturesDepose() {

        System.out.println("------< Voir mes candidatures déposées >------");

        try {
            voirOffresStagesCandidaturesDepose.setInt(1, idEtudiant);

            ResultSet rs = voirOffresStagesCandidaturesDepose.executeQuery();
            while (rs.next()) {
                System.out.println("---");
                System.out.println("Code de l'offre de stage : " + rs.getString(2) + "\nNom de l'entreprise : " + rs.getString(3) + "\nEtat : " + rs.getString(4));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void annulerCandidature() {

        System.out.println("------< Annuler une candidature >------");

        System.out.print("Entrez le code de l'offre de stage : ");
        String codeOffre = scanner.nextLine().toUpperCase();

        try {
            annulerCandidature.setString(1, codeOffre);
            annulerCandidature.setInt(2, idEtudiant);

            ResultSet rs = annulerCandidature.executeQuery();
            if(rs.next()) {
                System.out.println("Candidature annulée");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}