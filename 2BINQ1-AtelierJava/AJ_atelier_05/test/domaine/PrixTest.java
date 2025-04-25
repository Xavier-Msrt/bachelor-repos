package domaine;

import exceptions.QuantiteNonAutoriseeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrixTest {
    private Prix prixAucune;
    private Prix prixPub;
    private Prix prixSolde;
    @BeforeEach
    void setUp() {
        prixAucune = new Prix();
        prixPub = new Prix(TypePromo.PUB, 5);
        prixSolde = new Prix(TypePromo.SOLDE, 6);

        prixAucune.definirPrix(1,20.0);
        prixAucune.definirPrix(10, 10.0);

        prixPub.definirPrix(3,15.0);
    }

    @DisplayName("test Prix constructor throws IllegalArgumentException when typePromo is null")
    @Test
    void testPrix(){
        assertThrows(IllegalArgumentException.class, () -> new Prix(null ,1));
    }

    @DisplayName("test Prix constructor throws IllegalArgumentException when price is negative")
    @ParameterizedTest
    @ValueSource (ints = {-10,0,-50})
    void testPrix(int prix){
        assertThrows(IllegalArgumentException.class, () -> new Prix(TypePromo.PUB, prix));
    }






    @DisplayName("test valeurPromo is intialised at 0 when nothing is give in constructor")
    @Test
    void getValeurPromoBaseInitAtZero() {
        assertEquals(0, prixAucune.getValeurPromo());
    }

    @DisplayName("test valeurPromo is intialised at same value as give in constuctor")
    @Test
    void getValeurPromoInitSameAsConstuctor() {
        assertEquals(5, prixPub.getValeurPromo());
    }

    @DisplayName("test TypePromo is intialised at null when no param")
    @Test
    void getTypePromoInitTypeNull() {
        assertNull(prixAucune.getTypePromo());
    }


    @DisplayName("test TypePromo is intialised at same value as in constructor")
    @Test
    void getTypePromoInitTypeConstruct() {
        assertEquals(TypePromo.PUB, prixPub.getTypePromo());
    }

    @DisplayName("test definirPrix when qty is negative")
    @ParameterizedTest
    @ValueSource(ints = {0, -15, -2})
    void definirPrix(int qty) {
        assertThrows(IllegalArgumentException.class, () -> prixAucune.definirPrix(qty, 10));
    }

    @DisplayName("test definirPrix when price is negative")
    @ParameterizedTest
    @ValueSource(doubles = {0, -15, -2})
    void definirPrix(double prix) {
        assertThrows(IllegalArgumentException.class, () -> prixAucune.definirPrix(10, prix));
    }

    @DisplayName("test definirPrix set 10 for 6€ check when you take 10 is 6€")
    @Test
    void definirPrix() {
        prixAucune.definirPrix(10 ,6);
        assertEquals(6, prixAucune.getPrix(10));
    }


    @DisplayName("test getPrix when qty is negative")
    @ParameterizedTest
    @ValueSource(ints = {0, -12, -20})
    void getPrix(int qty) {
        assertThrows(IllegalArgumentException.class, () -> prixAucune.getPrix(qty));
    }


    @DisplayName("test getPrix for all minus 10")
    @ParameterizedTest
    @ValueSource(ints = {1,5,9})
    void getPrixMinusTen(int qty) {
        assertEquals(20, prixAucune.getPrix(qty));
    }

    @DisplayName("test getPrix for all plus 10 ")
    @ParameterizedTest
    @ValueSource(ints = {10,15,20,25})
    void getPrixPlus10(int qty) {
        assertEquals(10, prixAucune.getPrix(qty));
    }
    @DisplayName("test getPrix for prixPub min 2")
    @Test
    void getPrixMin2ForPrixPub() {
        assertThrows(QuantiteNonAutoriseeException.class, () -> prixPub.getPrix(2));
    }

    @DisplayName("test getPrix for prixSolde min 1")
    @Test
    void getPrixMin1ForPrixSolde() {
        assertThrows(QuantiteNonAutoriseeException.class, () -> prixSolde.getPrix(1));
    }





}