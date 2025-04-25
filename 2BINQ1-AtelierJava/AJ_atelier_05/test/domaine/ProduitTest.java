package domaine;

import exceptions.DateDejaPresenteException;
import exceptions.PrixNonDisponibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProduitTest {

    private Prix prixAucune;
    private Prix prixPub;
    private Prix prixSolde;

    private Produit produit1;
    private Produit produit2;

    @BeforeEach
    void setUp() {
        prixAucune = new Prix();
        prixPub = new Prix(TypePromo.PUB, 5);
        prixSolde = new Prix(TypePromo.SOLDE, 6);
        produit1 = new Produit("apple", "nature", "y2");
        produit2 = new Produit("chocolat","milka", "G8");

        produit1.ajouterPrix(LocalDate.now().minusDays(1), prixAucune);
        produit1.ajouterPrix(LocalDate.now().minusDays(2), prixPub);
        produit1.ajouterPrix(LocalDate.now(), prixSolde);
    }


    @DisplayName("test constructor throw illegal argument execption when nom is null")
    @Test
    void testProduitForNom(){
        assertThrows(IllegalArgumentException.class, () -> new Produit(null, "test", "test"));
    }
    @DisplayName("test constructor throw illegal argument execption when marque is null")
    @Test
    void testProduitForMarque(){
        assertThrows(IllegalArgumentException.class, () -> new Produit("test", null, "test"));
    }
    @DisplayName("test constructor throw illegal argument execption when rayon is null")
    @Test
    void testProduitForRayon(){
        assertThrows(IllegalArgumentException.class, () -> new Produit("test", "test", null));
    }

    @DisplayName("test constructor throw illegal argument execption when nom is empty or nothing")
    @ParameterizedTest
    @ValueSource(strings = {"\n", "\t", " ", "" })
    void testProduitForNomEmpty(String nom){
        assertThrows(IllegalArgumentException.class, () -> new Produit(nom, "test", "test"));
    }

    @DisplayName("test constructor throw illegal argument execption when marque is empty or nothing")
    @ParameterizedTest
    @ValueSource(strings = {"\n", "\t", " "})
    void testProduitForMarqueEmpty(String marque){
        assertThrows(IllegalArgumentException.class, () -> new Produit("test", marque, "test"));
    }

    @DisplayName("test constructor throw illegal argument execption when rayon is empty or nothing")
    @ParameterizedTest
    @ValueSource(strings = {"\n", "\t", " "})
    void testProduitFoRayonEmpty(String rayon){
        assertThrows(IllegalArgumentException.class, () -> new Produit("test", "test", rayon));
    }


    @DisplayName("test Marque when initialised")
    @Test
    void getMarqueForInitialised() {
        assertEquals("nature", produit1.getMarque());
    }

    @DisplayName("test Nom when initialised")
    @Test
    void getNom() {
        assertEquals("apple", produit1.getNom());
    }

    @DisplayName("test rayon when initialised")
    @Test
    void getRayon() {
        assertEquals("y2", produit1.getRayon());
    }
    @DisplayName("test ajouterPrix throws illegalArgumentException when prix is null")
    @Test
    void ajouterPrixTestPrixNull() {
        assertThrows(IllegalArgumentException.class, () -> produit1.ajouterPrix(LocalDate.now(), null));
    }

    @DisplayName("test ajouterPrix throws illegalArgumentException when date is allready enter")
    @Test
    void ajouterPrixTestDateSame() {
        LocalDate date = LocalDate.now();
        produit2.ajouterPrix(date, prixSolde);
        assertThrows(DateDejaPresenteException.class, () ->{
            produit2.ajouterPrix(date, prixSolde);

        });
    }

    @DisplayName("test ajouterPrix with getPrix to check")
    @Test
    void getPrix(){
        LocalDate date = LocalDate.now();
        produit2.ajouterPrix(date, prixSolde);
        assertEquals(prixSolde, produit2.getPrix(date));
    }


    @DisplayName("test ajouterPrix before date ajouterPrix date")
    @Test
    void getPrixForDateBeforAdd(){
        LocalDate date = LocalDate.now().minusYears(10);
        assertThrows(PrixNonDisponibleException.class,() -> produit1.getPrix(date));
    }


    @DisplayName("test ajouterPrix on produit Without price added")
    @Test
    void getPrixForProduitWithoutPriceAdded(){
        assertThrows(PrixNonDisponibleException.class,() -> produit2.getPrix(LocalDate.now()));
    }


    @DisplayName("test ajouterPrix in range of date")
    @Test
    void getPrixRange(){
        LocalDate date = LocalDate.now().minusYears(2);
        LocalDate date2 = LocalDate.now().plusYears(2);
        produit2.ajouterPrix(date, prixAucune);
        produit2.ajouterPrix(date2, prixSolde);
        assertEquals(prixAucune, produit2.getPrix(LocalDate.now()));
    }

    @DisplayName("test equals same object = same nom, same marque, same rayon")
    @Test
    void testEqualsForSameParam(){
        assertEquals(produit1, new Produit("apple", "nature", "y2"));
    }

    @DisplayName("test equals  object != not same nom, same marque, same rayon")
    @Test
    void testEqualsForSameParamButNomDifferente(){
        assertNotEquals(produit1, new Produit("pear", "nature", "y2"));
    }

    @DisplayName("test equals  object !=  same nom, not same marque, same rayon")
    @Test
    void testEqualsForSameParamButMarqueDifferente(){
        assertNotEquals(produit1, new Produit("apple", "pasNature", "y2"));
    }

    @DisplayName("test equals  object !=  same nom,  same marque, not same rayon")
    @Test
    void testEqualsForSameParamButRayonDifferente(){
        assertNotEquals(produit1, new Produit("apple", "nature", "b2"));
    }

    @DisplayName("test hashCode same for object with same attribut")
    @Test
    void testHashCode(){
        Produit produitCurrent = new Produit("apple", "nature", "y2");
        assertEquals(produitCurrent.hashCode(), produit1.hashCode());
    }




}