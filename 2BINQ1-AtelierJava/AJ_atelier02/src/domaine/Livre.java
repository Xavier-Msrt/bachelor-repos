package domaine;

import util.Util;

import java.util.*;

public class Livre  {
    HashMap<Plat.Type, TreeSet<Plat>> plats = new HashMap<>();


    /**
     * Ajoute un plat dans le livre, s'il n'existe pas déjà dedans.
     * Il faut ajouter correctement le plat en fonction de son type.
     * @param plat le plat à ajouter
     * @return true si le plat a été ajouté, false sinon.
     */
    public boolean ajouterPlat(Plat plat) {
        Util.checkObject(plat);

        Plat.Type type = plat.getType();
        TreeSet<Plat> treeSet = plats.get(type);

        if(treeSet == null){ // pas de ce type dans la HashMap
            treeSet =  new TreeSet<Plat>(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Plat p1 = (Plat) o1;
                    Plat p2 = (Plat) o2;
                    int value = p1.getNiveauDeDifficulte().compareTo(p2.getNiveauDeDifficulte());
                    if(value == 0){
                        value = p1.getNom().compareToIgnoreCase(p2.getNom());
                    }
                    return value;
                }
            });
            plats.put(type, treeSet);
        }else{
            if(treeSet.contains(plat)){
                return false;
            }
        }
        return treeSet.add(plat);
    }

    /**
     * Supprime un plat du livre, s'il est dedans.
     * Si le plat supprimé est le dernier de ce type de plat, il faut supprimer ce type de
     * plat de la Map.
     * @param plat le plat à supprimer
     * @return true si le plat a été supprimé, false sinon.
     */
    public boolean supprimerPlat (Plat plat) {
        if(plat == null)
            return false;

        Plat.Type type = plat.getType();
        TreeSet<Plat> treeSet = plats.get(type);

        if(treeSet != null){
           boolean isRemove = treeSet.remove(plat);
            if(treeSet.isEmpty()){
                plats.remove(type);
            }
            return isRemove;
        }
        return false;
    }


    /**
     * Renvoie un ensemble contenant tous les plats d'un certain type.
     * L'ensemble n'est pas modifable.
     * @param type le type de plats souhaité
     * @return l'ensemble des plats
     */
    public SortedSet<Plat> getPlatsParType(Plat.Type type) {
        // L'ensemble renvoyé ne doit pas être modifiable !
        return Collections.unmodifiableSortedSet(plats.get(type));
    }


    /**
     * Renvoie true si le livre contient le plat passé en paramètre. False
     sinon.
     * Pour cette recherche, un plat est identique à un autre si son type, son niveau de
     * difficulté et son nom sont identiques.
     * @param plat le plat à rechercher
     * @return true si le livre contient le plat, false sinon.
     */
    public boolean contient(Plat plat) {
        // Ne pas utiliser 2 fois la méthode get() de la map, et ne pas déclarer de variable locale !
        for (Plat currentPlat : plats.get(plat.getType())){
            if(currentPlat.getNiveauDeDifficulte().equals(currentPlat.getNiveauDeDifficulte())
                    && currentPlat.getNom().equals(plat.getNom())){
                return true;
            }
        }
        return false;
    }



    /**
     * Renvoie un ensemble contenant tous les plats du livre. Ils ne doivent pas être triés.
     * @return l'ensemble de tous les plats du livre.
     */
    public Set<Plat> tousLesPlats() {
        // Ne pas utiliser la méthode keySet() ou entrySet() ici !
        HashSet<Plat> allPlats = new HashSet<>();
        for (TreeSet<Plat> plat : plats.values()){
            allPlats.addAll(plat);
        }
        return allPlats;
    }













    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        for (Map.Entry<Plat.Type, TreeSet<Plat>> elem : plats.entrySet()){
            text.append(elem.getKey());
            text.append("\n=====");
            for (Plat plat : elem.getValue()){
                text.append( "\n "+plat.getNom());
            }
        }

        return text.toString();
    }


}
