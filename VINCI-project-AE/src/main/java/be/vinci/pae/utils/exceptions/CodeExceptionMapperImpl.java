package be.vinci.pae.utils.exceptions;

/**
 * Interface for code exception mapper.
 */
public class CodeExceptionMapperImpl implements CodeExceptionMapper {

  @Override
  public String codeMapper(String code) {
    return switch (code) {
      case "MISSING_FIELD" -> "Veuillez remplir tous les champs obligatoires";

      case "INCORRECT_VALUE" -> "Une ou plusieurs valeurs sont incorrectes";
      case "INCORRECT_EMAIL" -> "L'adresse e-mail est incorrecte";
      case "INCORRECT_VINCI_EMAIL" -> "L'adresse e-mail n'est pas sous le format @vinci.be";
      case "INCORRECT_EMAIL_PWD" -> "L'adresse e-mail et/ou le mot de passe sont incorrects";
      case "INVALID_ROLE_EMAIL" -> "L'adresse e-mail ne correspond pas avec le rôle";
      case "INCORRECT_PHONE_NUMBER" -> "Le numéro de téléphone est incorrect";

      case "EMAIL_USED" -> "Cette adresse e-mail est déjà utilisée";
      case "COMPANY_EXISTS" -> "L'entreprise existe déjà";
      case "INTERNSHIP_EXISTS" -> "Le stage existe déjà";
      case "SUPERVISOR_EXISTS" -> "Ce maître de stage existe déjà";

      case "TURNED_DOWN_NO_REFUSAL" ->
          "Pour refuser un contact, la raison du refus est obligatoire";
      case "ADMITTED_NO_MEETING" ->
          "Pour accepter un contact, le lieu de rencontre est obligatoire";
      case "ACCEPTED_INTERNSHIP" -> "Le contact ne peut pas être \"accepté\" "
          + "(uniquement possible par la création d'un stage)";
      case "IN_PROCESS_STATE" ->
          "Le contact doit être \"initié\" ou \"pris\" pour effectuer cette action";
      case "ADMITTED_STATE" -> "Le contact doit être \"pris\" pour effectuer cette action";
      case "ACCEPTED_STATE" -> "Le contact doit être \"accepté\" pour effectuer cette action";
      case "SAME_STATE" -> "Le contact est déjà dans cet état";
      case "CONTACT_ATTACH" -> "Aucun contact n'est attaché à ce stage";
      case "COMPANY_BLACKLIST" -> "Action refusée car l'entreprise est black-listée";

      case "NOT_FOUND" -> "Impossible de trouver la ressource demandée";

      case "CONFLICT_ERROR" -> "Un conflit a été rencontré, veuillez actualiser la page";
      case "UNAUTHORIZED" -> "Accès refusé, vous devez être connecté";
      case "ACCESS_ERROR" -> "Accès refusé, vous n'avez pas les autorisations nécessaires";
      default -> "Un problème s'est produit de notre côté, veuillez réessayer";

    };
  }

}
