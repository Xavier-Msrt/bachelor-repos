package be.vinci.domaine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MoniteurImplTest {
    private Moniteur moniteur;
    private  Stage stage;
    private Sport sport;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        moniteur = new MoniteurImpl("Nathan");
        stage = Mockito.mock(Stage.class);
        sport = Mockito.mock(Sport.class);

        Mockito.when(stage.getNumeroDeSemaine()).thenReturn(7);
        Mockito.when(sport.contientMoniteur(moniteur)).thenReturn(true);
        Mockito.when(stage.getMoniteur()).thenReturn(null);
        Mockito.when(stage.getSport()).thenReturn(sport);
    }

    private void amenerALEtat(int etat, Moniteur moniteur){
        for (int i = 1; i <= etat; i++) {
            Stage s = Mockito.mock(Stage.class);

            Mockito.when(s.getNumeroDeSemaine()).thenReturn(i);
            Mockito.when(s.getMoniteur()).thenReturn(null);
            Mockito.when(s.getSport()).thenReturn(sport);

            moniteur.ajouterStage(s);
        }
    }

    @DisplayName("TestMoniteurTC1")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC1() {
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(1, moniteur.nombreDeStages()),
                () -> Mockito.verify(stage).enregistrerMoniteur(moniteur)
        );


    }

    @DisplayName("TestMoniteurTC2")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC2() {
        amenerALEtat(1, moniteur);
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(2, moniteur.nombreDeStages()),
                () -> Mockito.verify(stage).enregistrerMoniteur(moniteur)
        );
    }




    @DisplayName("TestMoniteurTC3")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC3() {
        amenerALEtat(2, moniteur);
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(3, moniteur.nombreDeStages()),
                () -> Mockito.verify(stage).enregistrerMoniteur(moniteur)
        );
    }

    @DisplayName("TestMoniteurTC4")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC4() {
        amenerALEtat(3, moniteur);
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(4, moniteur.nombreDeStages()),
                () -> Mockito.verify(stage).enregistrerMoniteur(moniteur)
        );
    }

    @DisplayName("TestMoniteurTC5")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC5() {
        amenerALEtat(3, moniteur);
        moniteur.ajouterStage(stage);
        assertAll(
                () -> assertFalse(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(4, moniteur.nombreDeStages()),
                () -> Mockito.verify(stage).enregistrerMoniteur(moniteur) // call one time and not 2 time
        );
    }

    @DisplayName("TestMoniteurTC6")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC6() {
        amenerALEtat(4, moniteur);

        Stage s = Mockito.mock(Stage.class);
        Mockito.when(s.getNumeroDeSemaine()).thenReturn(2);
        Mockito.when(s.getMoniteur()).thenReturn(null);
        Mockito.when(s.getSport()).thenReturn(sport);

        assertAll(
                () -> assertFalse(moniteur.ajouterStage(s)),
                () -> assertEquals(4, moniteur.nombreDeStages()),
                () -> Mockito.verify(s, Mockito.never()).enregistrerMoniteur(moniteur) // not call on 's'
        );
    }

    @DisplayName("TestMoniteurTC7")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC7() {
        amenerALEtat(4, moniteur);

        Stage s = Mockito.mock(Stage.class);
        Mockito.when(s.getNumeroDeSemaine()).thenReturn(2);
        Mockito.when(s.getMoniteur()).thenReturn(new MoniteurImpl("Ludo"));
        Mockito.when(s.getSport()).thenReturn(sport);


        assertAll(
                () -> assertFalse(moniteur.ajouterStage(s)),
                () -> assertFalse(moniteur.contientStage(s)),
                () -> assertEquals(4, moniteur.nombreDeStages()),
                () -> Mockito.verify(s, Mockito.never()).enregistrerMoniteur(moniteur)

        );
    }


    @DisplayName("TestMoniteurTC8")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC8() {
        amenerALEtat(4, moniteur);

        Stage s = Mockito.mock(Stage.class);
        Mockito.when(s.getNumeroDeSemaine()).thenReturn(5);
        Mockito.when(s.getMoniteur()).thenReturn(moniteur);
        Mockito.when(s.getSport()).thenReturn(sport);


        assertAll(
                () -> assertTrue(moniteur.ajouterStage(s)),
                () -> assertTrue(moniteur.contientStage(s)),
                () -> assertEquals(5, moniteur.nombreDeStages()),
                () -> Mockito.verify(s, Mockito.never()).enregistrerMoniteur(moniteur)
        );
    }

    @DisplayName("TestMoniteurTC9")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC9() {

        Sport sp = Mockito.mock(Sport.class);
        Mockito.when(sp.contientMoniteur(moniteur)).thenReturn(false); // For this sport monitor is not qualified

        Stage st  = Mockito.mock(Stage.class);
        Mockito.when(st.getSport()).thenReturn(sp);
        Mockito.when(st.getNumeroDeSemaine()).thenReturn(1);
        Mockito.when(st.getMoniteur()).thenReturn(moniteur);

        assertAll(
                () -> assertFalse(moniteur.ajouterStage(st)),
                () -> assertFalse(moniteur.contientStage(st)),
                () -> assertEquals(0, moniteur.nombreDeStages()),
                () -> Mockito.verify(st, Mockito.never()).enregistrerMoniteur(moniteur)
        );



    }


}