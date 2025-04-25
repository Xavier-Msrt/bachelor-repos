import java.sql.*;
import java.util.Scanner;

import static java.sql.DriverManager.getConnection;

public class ApplicationProfesseur {
    Connection conn = null;

    Scanner scanner = new Scanner(System.in);
    PreparedStatement encoderEtudiant;
    PreparedStatement encoderEntreprise;
    PreparedStatement encoderMotCle;
    PreparedStatement voirOffreStageNonValidee;
    PreparedStatement valideeOffreStage;
    PreparedStatement voirOffreStageValidee;
    PreparedStatement voirEtudiantSansStage;
    PreparedStatement voirOffreStageAttribuee;


    ApplicationProfesseur() {

        // load Driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL manquant !");
            System.exit(1);
        }

        String url = "jdbc:postgresql://172.24.2.6:5432/dbxaviermassart";

        try {
            conn = DriverManager.getConnection(url, "xaviermassart", "ZGD3NBW8A");
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println("Impossible de joindre le server !");
            System.exit(1);
        }

        // PreapreStatement
        try {
            encoderEtudiant = conn.prepareStatement("SELECT projet.encoder_etudiant(?, ?, ?, ?, ?);");
            encoderEntreprise = conn.prepareStatement("SELECT projet.encoder_entreprise(?, ?, ?, ?, ?);");
            encoderMotCle = conn.prepareStatement("SELECT projet.encoder_mot_cle(?);");
            voirOffreStageNonValidee = conn.prepareStatement("SELECT code, semestre, nom, description FROM projet.voir_offre_stage_non_validee;");
            valideeOffreStage = conn.prepareStatement("SELECT projet.valider_offre_stage(?);");
            voirOffreStageValidee = conn.prepareStatement("SELECT code, semestre, nom, description FROM projet.voir_offre_stage_validee;");
            voirEtudiantSansStage = conn.prepareStatement("SELECT nom, prenom, email, semestre, nb_candidature_attente FROM projet.voir_etudiants_pas_stage;");
            voirOffreStageAttribuee = conn.prepareStatement("SELECT  code, nom_entreprise, nom_etudiant, prenom_etudiant FROM projet.voir_offre_stage_attribee;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

   

    public void demarrer() {
        System.out.println("|-------< Application Professeur >-------|\n");

        int choix;
        do {

            System.out.println("-------< Menu >-------");
            System.out.println("\t1) Encoder un étudiant.");
            System.out.println("\t2) Encoder une entreprise.");
            System.out.println("\t3) Encoder un mot-clé.");
            System.out.println("\t4) Voir les offres de stage non validées.");
            System.out.println("\t5) Valider une offre de stage.");
            System.out.println("\t6) Voir les offres de stage validées.");
            System.out.println("\t7) Voir les étudiants qui n'ont pas de stage.");
            System.out.println("\t8) Voir les offres de stage attribuées.");
            System.out.println("\tAutre Chiffre => Fermer le menu");

            System.out.print("Veuillez entrer votre choix : ");
            choix = Integer.parseInt(scanner.nextLine());

            System.out.println();
            switch (choix) {
                case 1:
                    encoderEtudiant();
                    break;
                case 2:
                    encoderEntreprise();
                    break;
                case 3:
                    encoderMotCle();
                    break;
                case 4:
                    voirOffreStageNonValidee();
                    break;
                case 5:
                    valideeOffreStage();
                    break;
                case 6:
                    voirOffreStageValidee();
                    break;
                case 7:
                    voirEtudiantSansStage();
                    break;
                case 8:
                    voirOffreStageAttribuee();
                    break;
                default:
                    break;
            }
            System.out.println();
        } while (choix > 0 && choix < 9);
    }


    public void encoderEtudiant() {
        System.out.println("------< Encoder un etudiant >------");

        System.out.print("Entrer le nom : ");
        String nom = scanner.nextLine();

        System.out.print("Entrer le prenom : ");
        String prenom = scanner.nextLine();

        System.out.print("Entrer l'email : ");
        String email = scanner.nextLine();

        System.out.print("Entrer le semestre : ");
        String semestre = scanner.nextLine().toUpperCase();

        System.out.print("Entrer le mots de passe : ");
        String mdp = scanner.nextLine();

        String sel = BCrypt.gensalt();
        String mdpCrypt = BCrypt.hashpw(mdp, sel);


        try {
            encoderEtudiant.setString(1, nom);
            encoderEtudiant.setString(2, prenom);
            encoderEtudiant.setString(3, email);
            encoderEtudiant.setString(4, semestre.toUpperCase());
            encoderEtudiant.setString(5, mdpCrypt);

            ResultSet rs = encoderEtudiant.executeQuery();
            if (rs.next()) {
                System.out.println("L'étudiant a été encodé avec succès");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void encoderEntreprise() {
        System.out.println("------< Encodage d'une entreprise >------");

        System.out.print("Entrer le nom : ");
        String nom = scanner.nextLine();

        System.out.print("Entrer l'adresse postale : ");
        String adressePostal = scanner.nextLine();

        System.out.print("Entrer l'adresse e-mail : ");
        String adresseMail = scanner.nextLine().toLowerCase();

        System.out.print("Entrer le code : ");
        String code = scanner.nextLine().toUpperCase();

        System.out.print("Entrer le mot de passe : ");
        String mdp = scanner.nextLine();

        String sel = BCrypt.gensalt();
        String mdpCrypt = BCrypt.hashpw(mdp, sel);

        try {
            encoderEntreprise.setString(1, code);
            encoderEntreprise.setString(2, nom);
            encoderEntreprise.setString(3, adressePostal);
            encoderEntreprise.setString(4, adresseMail);
            encoderEntreprise.setString(5, mdpCrypt);

            ResultSet rs = encoderEntreprise.executeQuery();
            if (rs.next()) {
                System.out.println("L'entreprise a été encodée avec succès.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public void encoderMotCle() {
        System.out.println("------< Encodage d'un mot-clé >------");

        System.out.print("Entrer le mot-clé : ");
        String mot = scanner.nextLine();

        try {
            encoderMotCle.setString(1, mot.toLowerCase());

            ResultSet rs = encoderMotCle.executeQuery();
            if (rs.next()) {
                System.out.println("Le mot-clé a été encodé avec succès.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public void voirOffreStageNonValidee() {

        System.out.println("------< Voir les offres de stage non validées >------");

        try (ResultSet rs = voirOffreStageNonValidee.executeQuery()) {

            if (rs.next()) {
                do {
                    System.out.println("---");
                    System.out.println("Code de l'offre de stage : "+rs.getString(1));
                    System.out.println("\tLe semestre de l'offre : "+rs.getString(2));
                    System.out.println("\tNom de l'entreprise : "+rs.getString(3));
                    System.out.println("\tDescription de l'offre : "+ rs.getString(4));

                } while (rs.next());
            } else {
                System.out.println("Il n'y a actuellement aucun stage non validé.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void valideeOffreStage() {
        System.out.println("------< Valider une offre de stage. >------");

        System.out.print("Entrer le code : ");
        String code = scanner.nextLine();

        try {
            valideeOffreStage.setString(1, code.toUpperCase());

            ResultSet rs = valideeOffreStage.executeQuery();
            if (rs.next()) {
                System.out.println("L'offre a été validée avec succès.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void voirOffreStageValidee() {
        System.out.println("------< Voir les offres de stage validées. >------");

        try (ResultSet rs = voirOffreStageValidee.executeQuery()) {
            if (rs.next()) {
                do {
                    System.out.println("---");
                    System.out.println("Code de l'offre de stage : "+rs.getString(1));
                    System.out.println("\tLe semestre de l'offre : "+rs.getString(2));
                    System.out.println("\tNom de l'entreprise : "+rs.getString(3));
                    System.out.println("\tDescription de l'offre : "+ rs.getString(4));
                } while (rs.next());
            } else {
                System.out.println("Il n'y a actuellement aucun stage validé.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void voirEtudiantSansStage() {
        System.out.println("------< Voir les étudiants qui n'ont pas de stage >------");

        try (ResultSet rs = voirEtudiantSansStage.executeQuery()) {
            if (rs.next()) {
                do {
                    System.out.println("---");
                    System.out.println("Nom de l'etudiant : "+rs.getString(1));
                    System.out.println("\tPrénom de l'etudiant : "+rs.getString(2));
                    System.out.println("\tEmail de l'etudiant : "+rs.getString(3));
                    System.out.println("\tSemestre de l'etudiant : "+ rs.getString(4));
                    System.out.println("\tNombre de candidatures de l'étudiant : "+ rs.getString(5));
                } while (rs.next());
            } else {
                System.out.println("Il n'y a pas d'étudiant sans stage.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void voirOffreStageAttribuee() {
        System.out.println("------< Voir les offres de stage dans l'état attribué >------");

        try (ResultSet rs = voirOffreStageAttribuee.executeQuery()) {
            if (rs.next()) {
                do {
                    System.out.println("---");
                    System.out.println("Code de l'offre de stage : "+rs.getString(1));
                    System.out.println("\tNom de l'entreprise : "+rs.getString(2));
                    System.out.println("\tNom de l'etudiant : "+rs.getString(3));
                    System.out.println("\tPrénom de l'étudiant : "+ rs.getString(4));;
                } while (rs.next());

            } else {
                System.out.println("Il n'y a pas d'offre de stage attribuée.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
