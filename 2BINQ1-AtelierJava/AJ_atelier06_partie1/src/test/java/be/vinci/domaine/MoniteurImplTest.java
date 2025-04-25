package be.vinci.domaine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoniteurImplTest {
    private Moniteur moniteur;
    private  Stage stage;
    private Sport sport;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        moniteur = new MoniteurImpl("Nathan");
        sport = new SportStub(true);
        stage = new StageStub(7, sport, moniteur);
    }

    private void amenerALEtat(int etat, Moniteur moniteur){
        for (int i = 1; i <= etat; i++) {
            moniteur.ajouterStage(new StageStub(i, new SportStub(true), moniteur));
        }
    }

    @DisplayName("TestMoniteurTC1")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC1() {
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(1, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC2")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC2() {
        amenerALEtat(1, moniteur);
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(2, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC3")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC3() {
        amenerALEtat(2, moniteur);
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(3, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC4")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC4() {
        amenerALEtat(3, moniteur);
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(stage)),
                () -> assertTrue(moniteur.contientStage(stage)),
                () -> assertEquals(4, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC5")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC5() {
        amenerALEtat(3, moniteur);
        moniteur.ajouterStage(stage);
        assertAll(
                () -> assertTrue(moniteur.supprimerStage(stage)),
                () -> assertFalse(moniteur.contientStage(stage)),
                () -> assertEquals(3, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC6")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC6() {
        amenerALEtat(2, moniteur);
        moniteur.ajouterStage(stage);
        assertAll(
                () -> assertTrue(moniteur.supprimerStage(stage)),
                () -> assertFalse(moniteur.contientStage(stage)),
                () -> assertEquals(2, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC7")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC7() {
        amenerALEtat(1, moniteur);
        moniteur.ajouterStage(stage);
        assertAll(
                () -> assertTrue(moniteur.supprimerStage(stage)),
                () -> assertFalse(moniteur.contientStage(stage)),
                () -> assertEquals(1, moniteur.nombreDeStages())
        );
    }


    @DisplayName("TestMoniteurTC8")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC8() {
        moniteur.ajouterStage(stage);
        assertAll(
                () -> assertTrue(moniteur.supprimerStage(stage)),
                () -> assertFalse(moniteur.contientStage(stage)),
                () -> assertEquals(0, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC9")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC9() {
        amenerALEtat(3, moniteur);
        moniteur.ajouterStage(stage);
        assertAll(
                () -> assertFalse(moniteur.ajouterStage(stage)),
                () -> assertEquals(4, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC10")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC10() {
        amenerALEtat(4, moniteur);
        Stage stageCurrent = new StageStub(3,new SportStub(true),moniteur);
        assertAll(
                () -> assertFalse(moniteur.ajouterStage(stageCurrent)),
                () -> assertEquals(4, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC11")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC11() {
        amenerALEtat(4, moniteur);
        assertAll(
                () -> assertFalse(moniteur.supprimerStage(stage)),
                () -> assertEquals(4, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC12")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC12() {
        amenerALEtat(4, moniteur);
        Stage currentStage = new StageStub(5,new SportStub(true),new MoniteurImpl("Ludo"));
        assertAll(
                () -> assertFalse(moniteur.ajouterStage(currentStage)),
                () -> assertEquals(4, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC13")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC13() {
        amenerALEtat(4, moniteur);
        assertAll(
                () -> assertTrue(moniteur.ajouterStage(new StageStub(5,sport,moniteur))),
                () -> assertEquals(5, moniteur.nombreDeStages())
        );
    }

    @DisplayName("TestMoniteurTC14")
    @org.junit.jupiter.api.Test
    void TestMoniteurTC14() {
        amenerALEtat(4, moniteur);
            assertAll(
                () -> assertFalse(moniteur.ajouterStage(new StageStub(5,new SportStub(false),null))),
                () -> assertEquals(4, moniteur.nombreDeStages())
        );
    }














}
