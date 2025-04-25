package lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Lambda {

    /**
     * Retourne une liste contenant uniquement les Integer qui correspondent
     * au predicat match
     * @param list La liste d'Integer originale
     * @param match le predicat à respecter
     * @return une liste contenant les integer qui respectent match
     */
    public static <E> List<E> allMatches(List<E> list, Predicate<E> match) {
        List<E> listObject = new ArrayList<>();
        for (E elem : list){
            if(match.test(elem)){
                listObject.add(elem);
            }
        }
        return listObject;
    }

    /**
     * Retourne une liste contenant tous les éléments de la liste originale, transformés
     * par la fonction transform
     * @param list La liste d'Integer originale
     * @param transform la fonction à appliquer aux éléments
     * @return une liste contenant les integer transformés par transform
     */
    public static <E, R> List<R> transformAll(List<E> list, Function<E, R> transform) {
        List<R> listTransform = new ArrayList<>();
        for (E i : list){
            listTransform.add(transform.apply(i));
        }
        return listTransform;
    }


    public static <E> List<E> filter(List<E> list, Predicate<E> match) {
        return list.stream().filter(match).collect(Collectors.toList());
    }

    /**
     * Retourne une liste contenant tous les éléments de la liste originale, transformés
     * par la fonction transform
     * @param list La liste d'Integer originale
     * @param transform la fonction à appliquer aux éléments
     * @return une liste contenant les integer transformés par transform
     */
    public static <E, R> List<R> map(List<E> list, Function<E, R> transform) {
        return list.stream().map(transform).collect(Collectors.toList());
    }

    //  public static <E, R> --> on declare les type pour les utiliser
    //  List<R> map(List<E> list, Function<E, R> transform) --> attention E pourrais etre dans certain cas une autre type donc E, R



}
