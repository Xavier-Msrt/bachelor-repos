DROP SCHEMA IF EXISTS projet CASCADE;
CREATE SCHEMA projet;

-- enum
CREATE TYPE projet.etat_offre_stage AS ENUM ('non validée', 'validée', 'attribuée', 'annulée');

CREATE TYPE projet.etat_candidatures AS ENUM ('en attente', 'acceptée', 'refusée', 'annulée');

CREATE TYPE projet.semestre AS ENUM ('Q1','Q2');


-- table
CREATE TABLE projet.entreprises
(
    identifiant CHAR(3) PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    adresse     VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    mdp         VARCHAR(255) NOT NULL,
    CHECK ( identifiant <> '' AND identifiant SIMILAR TO '[A-Z]{3}' ),
    CHECK ( nom <> '' ),
    CHECK ( adresse <> '' ),
    CHECK ( email <> '' AND email SIMILAR TO '%_@%_.__%'),
    CHECK ( mdp <> '' )
);

CREATE TABLE projet.etudiants
(
    id_etudiant            SERIAL PRIMARY KEY,
    nom                    VARCHAR(100)        NOT NULL,
    prenom                 VARCHAR(100)        NOT NULL,
    email                  VARCHAR(100) UNIQUE NOT NULL,
    semestre               projet.semestre     NOT NULL,
    mdp                    VARCHAR(255)        NOT NULL,
    nb_candidature_attente INTEGER             NOT NULL DEFAULT 0,
    CHECK (nom <> ''),
    CHECK (prenom <> ''),
    CHECK (email <> '' AND email SIMILAR TO '_+._+@student.vinci.be'),
    CHECK (mdp <> ''),
    CHECK ( nb_candidature_attente >= 0 )
);

CREATE TABLE projet.offres_stage
(
    id_offre_stage SERIAL PRIMARY KEY,
    code           VARCHAR(50)             NOT NULL,
    etat           projet.etat_offre_stage NOT NULL DEFAULT 'non validée',
    semestre       projet.semestre         NOT NULL,
    offreur        CHARACTER(3)            NOT NULL REFERENCES projet.entreprises (identifiant),
    description    TEXT                    NOT NULL,
    UNIQUE (code),
    CHECK ( description <> '' ),
    CHECK ( code <> '' ),
    CHECK ( code SIMILAR TO '[A-Z]{3}[1-9]+[0-9]*')
);

CREATE TABLE projet.candidatures
(
    offre_stage INTEGER REFERENCES projet.offres_stage (id_offre_stage) NOT NULL,
    etudiant    INTEGER REFERENCES projet.etudiants (id_etudiant)       NOT NULL,
    etat        projet.etat_candidatures                                NOT NULL DEFAULT 'en attente',
    motivation  TEXT                                                    NOT NULL,
    PRIMARY KEY (offre_stage, etudiant),
    CHECK ( motivation <> '' )
);

CREATE TABLE projet.mots_cle
(
    id_mots_cle SERIAL PRIMARY KEY,
    mot_cle     VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE projet.mots_cle_stage
(
    mots_cle INTEGER NOT NULL REFERENCES projet.mots_cle (id_mots_cle),
    stage    INTEGER NOT NULL REFERENCES projet.offres_stage (id_offre_stage),
    PRIMARY KEY (mots_cle, stage)
);

/*
######################################################################################
####################################### TRIGGER ######################################
######################################################################################
*/

CREATE OR REPLACE FUNCTION projet.mise_a_jour_nb_candidature() RETURNS TRIGGER AS
$$
DECLARE
BEGIN
    UPDATE projet.etudiants e
    SET nb_candidature_attente = nb_candidature_attente + 1
    WHERE NEW.etudiant = e.id_etudiant;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_mise_a_jour_nb_candidature
    AFTER INSERT
    ON projet.candidatures
    FOR EACH ROW
EXECUTE PROCEDURE projet.mise_a_jour_nb_candidature();


CREATE FUNCTION projet.triggerCodeOffreStage() RETURNS TRIGGER AS
$$
DECLARE
    value  VARCHAR(50);
    cpt    INTEGER := 1;
    record RECORD;
BEGIN
    FOR record IN SELECT os.code FROM projet.offres_stage os WHERE os.offreur = NEW.offreur
        LOOP
            cpt := cpt + 1;
        END LOOP;
    value := concat(NEW.offreur, cpt);
    NEW.code := value;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_code_offre_stage
    BEFORE INSERT
    ON projet.offres_stage
    FOR EACH ROW
EXECUTE PROCEDURE projet.triggerCodeOffreStage();

CREATE OR REPLACE FUNCTION projet.mise_a_jour_candidature_etudiant() RETURNS TRIGGER AS
$$
DECLARE
BEGIN
    IF (NEW.etat = 'acceptée') THEN
        UPDATE projet.candidatures
        SET etat = 'annulée'
        WHERE NEW.offre_stage <> offre_stage
          AND NEW.etudiant = etudiant;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_mise_a_jour_candidature_etudiant
    AFTER UPDATE
    ON projet.candidatures
    FOR EACH ROW
EXECUTE PROCEDURE projet.mise_a_jour_candidature_etudiant();


CREATE OR REPLACE FUNCTION projet.mise_a_jour_offre_stage() RETURNS TRIGGER AS
$$
DECLARE
BEGIN
    IF (NEW.etat = 'attribuée') THEN
        UPDATE projet.candidatures
        SET etat = 'refusée'
        WHERE etat <> 'acceptée'
          AND offre_stage = NEW.id_offre_stage;

        UPDATE projet.offres_stage
        SET etat = 'annulée'
        WHERE offreur = NEW.offreur
          AND semestre = NEW.semestre
          AND id_offre_stage <> NEW.id_offre_stage;
    END IF;

    IF (NEW.etat = 'annulée') THEN
        UPDATE projet.candidatures
        SET etat = 'refusée'
        WHERE NEW.id_offre_stage = offre_stage
          AND etat = 'en attente';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_mise_a_jour_offre_stage
    AFTER UPDATE
    ON projet.offres_stage
    FOR EACH ROW
EXECUTE PROCEDURE projet.mise_a_jour_offre_stage();


/*
######################################################################################
############################### Application professeur ###############################
######################################################################################
*/

--1
CREATE OR REPLACE FUNCTION projet.encoder_etudiant(
    _nom VARCHAR(100),
    _prenom VARCHAR(100),
    _email VARCHAR(100),
    _semestre CHAR(2),
    _mdp VARCHAR(255)
) RETURNS VOID AS
$$
DECLARE
BEGIN
    INSERT INTO projet.etudiants (nom, prenom, email, semestre, mdp)
    VALUES (_nom, _prenom, _email, _semestre::projet.semestre, _mdp);

END;
$$ LANGUAGE plpgsql;


--2
CREATE OR REPLACE FUNCTION projet.encoder_entreprise(
    _identifiant CHARACTER(3),
    _nom VARCHAR(100),
    _adresse VARCHAR(100),
    _email VARCHAR(100),
    _mdp VARCHAR(255)
) RETURNS VOID AS
$$
DECLARE
BEGIN
    INSERT INTO projet.entreprises (identifiant, nom, adresse, email, mdp)
    VALUES (_identifiant, _nom, _adresse, _email, _mdp);
END;
$$ LANGUAGE plpgsql;


--3
CREATE OR REPLACE FUNCTION projet.encoder_mot_cle(
    _mot_cle VARCHAR(20)
) RETURNS VOID AS
$$
DECLARE
BEGIN
    INSERT INTO projet.mots_cle (mot_cle)
    VALUES (_mot_cle);
END;
$$ LANGUAGE plpgsql;

--4
CREATE VIEW projet.voir_offre_stage_non_validee AS
SELECT os.code, os.semestre, e.nom, os.description
FROM projet.offres_stage os,
     projet.entreprises e
WHERE os.offreur = e.identifiant
  AND os.etat = 'non validée';


--5
CREATE OR REPLACE FUNCTION projet.valider_offre_stage(
    _code VARCHAR(50)
) RETURNS VOID AS
$$
DECLARE
    stage RECORD;
BEGIN
    SELECT os.* FROM projet.offres_stage os WHERE os.code = _code INTO stage;

    IF NOT FOUND THEN
        RAISE 'Impossible de faire valider un offre de stage qui n''existe pas';
    END IF;

    IF ('non validée' = stage.etat) THEN
        UPDATE projet.offres_stage SET etat = 'validée' WHERE code = _code ;
    ELSE
        RAISE 'Impossible de faire valider une offre de stage si elle n''est pas dans état non validée';
    END IF;

END

$$ LANGUAGE plpgsql;


--6
CREATE VIEW projet.voir_offre_stage_validee AS
SELECT os.code, os.semestre, e.nom, os.description
FROM projet.offres_stage os,
     projet.entreprises e
WHERE os.offreur = e.identifiant
  AND os.etat = 'validée';

--7
CREATE VIEW projet.voir_etudiants_pas_stage AS
SELECT e.nom, e.prenom, e.email, e.semestre, e.nb_candidature_attente
FROM projet.etudiants e
WHERE e.id_etudiant NOT IN (SELECT e2.id_etudiant
                            FROM projet.etudiants e2,
                                 projet.candidatures c
                            WHERE e2.id_etudiant = c.etudiant
                              AND c.etat = 'acceptée');


--8
CREATE VIEW projet.voir_offre_stage_attribee AS
SELECT os.code AS code, en.nom AS nom_entreprise, et.nom AS nom_etudiant, et.prenom AS prenom_etudiant
FROM projet.offres_stage os,
     projet.entreprises en,
     projet.candidatures c,
     projet.etudiants et
WHERE os.id_offre_stage = c.offre_stage
  AND os.offreur = en.identifiant
  AND c.etudiant = et.id_etudiant
  AND os.etat = 'attribuée'
  AND c.etat = 'acceptée';

/*
######################################################################################
############################### Application entreprises ##############################
######################################################################################
*/


-- 1
CREATE OR REPLACE FUNCTION projet.ajouter_offre_stage(
    _offreur VARCHAR(3),
    _description TEXT,
    _semestre CHAR(2)
) RETURNS VOID AS
$$
DECLARE
    stage RECORD;
BEGIN
    SELECT os.*
    FROM projet.offres_stage os
    WHERE os.offreur = _offreur
      AND os.semestre = _semestre::projet.semestre
    INTO stage;

    IF (stage.etat = 'attribuée') THEN
        RAISE 'Vous avez déjà une offre de stage attribuée durant ce semestre';
    END IF;

    INSERT INTO projet.offres_stage (semestre, offreur, description)
    VALUES (_semestre::projet.semestre, _offreur, _description);
END;
$$ Language plpgsql;

-- 2
/*SELECT mc.mot_cle FROM projet.mots_cle mc;*/

-- 3
CREATE OR REPLACE FUNCTION projet.ajout_mot_cle(
    _offreur CHAR(3),
    _motcle VARCHAR(20),
    _code VARCHAR(50)
) RETURNS VOID AS
$$
DECLARE
    offre_stage INTEGER := 0;
    entreprises CHAR(3);
    etatCorrect projet.etat_offre_stage;
    record      RECORD;
    cpt         INTEGER := 0;
    id_motcle   INTEGER := 0;
BEGIN

    SELECT os.id_offre_stage FROM projet.offres_stage os WHERE os.code = _code INTO offre_stage;

    IF (offre_stage = 0) THEN
        RAISE 'Cette offre de stage n''exist pas !';
    end if;

    SELECT DISTINCT os.offreur FROM projet.offres_stage os WHERE os.id_offre_stage = offre_stage INTO entreprises;

    SELECT DISTINCT os.etat FROM projet.offres_stage os WHERE os.id_offre_stage = offre_stage INTO etatCorrect;

    FOR record IN SELECT DISTINCT mcs.mots_cle
                  FROM projet.mots_cle_stage mcs
                  WHERE mcs.stage = offre_stage
        LOOP
            cpt := 1 + cpt;
        END LOOP;

    IF (cpt = 3) THEN
        RAISE 'Vous avez atteint le maximum de mots-clés pour une offre de stage';
    END IF;

    IF (etatCorrect = 'attribuée') THEN
        RAISE 'L''état de l''offre de stage est attribuée ';
    END IF;

    IF (etatCorrect = 'annulée') THEN
        RAISE 'L''état de l''offre de stage est annulée';
    END IF;

    IF (entreprises != _offreur) THEN
        RAISE 'Vous n''avez pas le droit de modifier l''offre d''un autre entreprise';
    END IF;

    SELECT mc.id_mots_cle FROM projet.mots_cle mc WHERE mc.mot_cle = _motcle INTO id_motcle;

    IF (NOT FOUND) THEN
        RAISE 'Ce mots n''exist pas !';
    END IF;

    INSERT INTO projet.mots_cle_stage(mots_cle, stage) VALUES (id_motcle, offre_stage);

END
$$ LANGUAGE plpgsql;


--4
CREATE VIEW projet.nb_candidature_attente AS
SELECT os.id_offre_stage, COUNT(c.etudiant) AS nb_candidat
FROM projet.offres_stage os
         LEFT OUTER JOIN projet.candidatures c
                         on os.id_offre_stage = c.offre_stage
                             AND c.offre_stage = os.id_offre_stage
                             AND c.etat = 'en attente'
GROUP BY os.id_offre_stage;

CREATE VIEW projet.voir_offres_stage AS
SELECT os.offreur,
       os.code,
       os.description,
       os.semestre,
       os.etat,
       nca.nb_candidat,
       COALESCE(e.nom, 'non attribuée') AS attribue
FROM projet.nb_candidature_attente nca,
     projet.offres_stage os
         LEFT OUTER JOIN projet.candidatures c
                         ON os.id_offre_stage = c.offre_stage
                             AND c.etat = 'acceptée'
         LEFT OUTER JOIN projet.etudiants e
                         ON c.etudiant = e.id_etudiant
WHERE nca.id_offre_stage = os.id_offre_stage;



-- 5
CREATE OR REPLACE VIEW projet.voir_candidature_offre AS
SELECT os.offreur, os.code, c.etat, e.nom, e.prenom, e.email, c.motivation
FROM projet.candidatures c,
     projet.etudiants e,
     projet.offres_stage os
WHERE c.offre_stage = os.id_offre_stage
  AND e.id_etudiant = c.etudiant;


--6
CREATE OR REPLACE FUNCTION projet.selectionner_etudiant_offre(
    _entreprise CHAR(3),
    _stage VARCHAR(100),
    _etudiant VARCHAR(100)
) RETURNS VOID AS
$$
DECLARE

    stage       RECORD;
    etudiant_id INTEGER;
    candidature RECORD;

BEGIN

    SELECT os.* FROM projet.offres_stage os WHERE os.code = _stage AND os.offreur = _entreprise INTO stage;

    IF (NOT FOUND) THEN
        RAISE 'Cette offre de stage n’est pas une offre de l’entreprise';
    END IF;

    IF (stage.etat != 'validée') THEN
        RAISE 'Cette offre doit etre dans l''etat validée';
    END IF;

    SELECT e.id_etudiant FROM projet.etudiants e WHERE e.email = _etudiant INTO etudiant_id;

    SELECT c.*
    FROM projet.candidatures c
    WHERE c.etudiant = etudiant_id
      AND c.offre_stage = stage.id_offre_stage
    INTO candidature;

    IF NOT FOUND THEN
        RAISE 'L''etudiant n''a pas desposer sa canditature pour cette offre de stage';
    END IF;

    IF (candidature.etat != 'en attente') THEN
        RAISE 'La candidature n''est pas en attente ';
    END IF;


    UPDATE projet.offres_stage
    SET etat = 'attribuée'
    WHERE id_offre_stage = stage.id_offre_stage;

    UPDATE projet.candidatures
    SET etat = 'acceptée'
    WHERE etudiant = etudiant_id
      AND offre_stage = stage.id_offre_stage;

END
$$ LANGUAGE plpgsql;


--7
CREATE OR REPLACE FUNCTION projet.annuler_offre_stage(_entreprise CHAR(3), _code VARCHAR(50))
    RETURNS VOID AS
$$
DECLARE
    stage RECORD;
BEGIN

    SELECT DISTINCT os.* FROM projet.offres_stage os WHERE os.code = _code INTO stage;

    IF NOT FOUND THEN
        RAISE 'Cette offre de stage n''appartient pas à l''entreprise';
    END IF;

    IF (_entreprise != stage.offreur) THEN
        RAISE 'Cette offre ne vous appartient pas!';
    END IF;

    IF (stage.etat = 'attribuée') THEN
        RAISE 'Cette offre est déjà à l''etat attribuée';
    END IF;

    IF (stage.etat = 'annulée') THEN
        RAISE 'Cette offre est déjà à l''etat annulée';
    END IF;

    UPDATE projet.offres_stage
    SET etat = 'annulée'
    WHERE etat != 'annulée'
      AND code = _code;
END
$$ LANGUAGE plpgsql;


/*
######################################################################################
################################ Application étudiant ################################
######################################################################################
*/

CREATE OR REPLACE FUNCTION projet.voir_offres_stages_validee(_etudiant INTEGER) RETURNS SETOF RECORD AS
$$
DECLARE
    _offre_stage RECORD;
    _mot_cle     RECORD;
    _sortie      RECORD;
    _sep         VARCHAR;
    _texte       TEXT;
BEGIN
    FOR _offre_stage IN SELECT os.*
                        FROM projet.offres_stage os,
                             projet.etudiants et
                        WHERE os.etat = 'validée'
                          AND et.id_etudiant = _etudiant
                          AND et.semestre = os.semestre
        LOOP

            _texte := '';_sep := '';
            FOR _mot_cle IN SELECT mc.mot_cle
                            FROM projet.mots_cle_stage ms,
                                 projet.mots_cle mc
                            WHERE ms.stage = _offre_stage.id_offre_stage
                              AND ms.mots_cle = mc.id_mots_cle
                LOOP
                    _texte := _texte || _sep || _mot_cle.mot_cle;
                    _sep := ', ';
                END LOOP;

            SELECT _offre_stage.code, e.nom, e.adresse, _offre_stage.description, _texte
            FROM projet.entreprises e
            WHERE e.identifiant = _offre_stage.offreur
            INTO _sortie;

            RETURN NEXT _sortie;

        END LOOP;
    RETURN;
END;
$$ LANGUAGE 'plpgsql';


CREATE OR REPLACE FUNCTION projet.recherche_offre_stage_mot_cle(_etudiant INTEGER, _mot_cle VARCHAR(20)) RETURNS SETOF RECORD AS
$$
DECLARE
    _offre_stage  RECORD;
    _list_mot_cle RECORD;
    _sortie       RECORD;
    _sep          VARCHAR;
    _texte        TEXT;
BEGIN
    FOR _offre_stage IN SELECT os.*
                        FROM projet.offres_stage os,
                             projet.etudiants et,
                             projet.mots_cle mc,
                             projet.mots_cle_stage ms
                        WHERE os.etat = 'validée'
                          AND et.id_etudiant = _etudiant
                          AND et.semestre = os.semestre
                          AND mc.mot_cle = _mot_cle
                          AND mc.id_mots_cle = ms.mots_cle
                          AND ms.stage = os.id_offre_stage
        LOOP

            _texte := '';_sep := '';
            FOR _list_mot_cle IN SELECT mc.mot_cle
                                 FROM projet.mots_cle_stage ms,
                                      projet.mots_cle mc
                                 WHERE ms.stage = _offre_stage.id_offre_stage
                                   AND ms.mots_cle = mc.id_mots_cle
                LOOP
                    _texte := _texte || _sep || _list_mot_cle.mot_cle;
                    _sep := ', ';
                END LOOP;

            SELECT _offre_stage.code, e.nom, e.adresse, _offre_stage.description, _texte
            FROM projet.entreprises e
            WHERE identifiant = _offre_stage.offreur
            INTO _sortie;

            RETURN NEXT _sortie;

        END LOOP;
    RETURN;
END;
$$ LANGUAGE 'plpgsql';



CREATE OR REPLACE FUNCTION projet.poser_candidature(_etudiant INTEGER, _code_stage VARCHAR(20), _motivations TEXT) RETURNS VOID AS
$$
DECLARE
    _result RECORD;
BEGIN
    SELECT c.*
    FROM projet.candidatures c
    WHERE c.etudiant = _etudiant
      AND c.etat = 'acceptée'
    INTO _result;

    IF FOUND THEN
        RAISE 'Vous possédez déjà une candidature acceptée.';
    END IF;

    SELECT o.*
    FROM projet.offres_stage o
    WHERE o.code = _code_stage
    INTO _result;

    IF (_result.etat != 'validée') THEN
        RAISE 'Cette offre n''est pas dans l''état validée';
    END IF;

    SELECT o.*
    FROM projet.offres_stage o,
         projet.etudiants e
    WHERE o.code = _code_stage
      AND e.id_etudiant = _etudiant
      AND o.semestre = e.semestre
    INTO _result;

    IF NOT FOUND THEN
        RAISE 'Le semestre de l''étudiant ne correspond pas.';
    END IF;

    INSERT INTO projet.candidatures (offre_stage, etudiant, etat, motivation)
    VALUES (_result.id_offre_stage, _etudiant, 'en attente', _motivations);
END
$$ LANGUAGE 'plpgsql';


CREATE VIEW projet.voir_offres_stages_candidatures_depose AS
SELECT c.etudiant, o.code, e.nom, c.etat
FROM projet.entreprises e,
     projet.offres_stage o,
     projet.candidatures c
WHERE o.offreur = e.identifiant
  AND o.id_offre_stage = c.offre_stage;



CREATE OR REPLACE FUNCTION projet.annuler_candidature(_code_stage VARCHAR(50), _etudiant INTEGER) RETURNS VOID AS
$$
DECLARE
    _id_offre_stage INTEGER;
BEGIN
    SELECT o.id_offre_stage
    FROM projet.offres_stage o
    WHERE o.code = _code_stage
    INTO _id_offre_stage;

    IF NOT FOUND THEN
        RAISE 'Cette offre de stage n''existe pas';
    END IF;

    IF ('en attente' = (SELECT c.etat
                        FROM projet.candidatures c
                        WHERE c.offre_stage = _id_offre_stage
                          AND c.etudiant = _etudiant)) THEN
        UPDATE projet.candidatures SET etat = 'annulée' WHERE offre_stage = _id_offre_stage AND etudiant = _etudiant;
    ELSE
        RAISE 'Impossible de modifier l''état d''une candidature qui n''est pas en attente';
    END IF;
END;
$$ LANGUAGE 'plpgsql';



-- GRANT

-- Application Etudiant

GRANT CONNECT ON DATABASE dbxaviermassart TO thibautdevos;
GRANT USAGE ON SCHEMA projet TO thibautdevos;

GRANT SELECT ON projet.etudiants TO thibautdevos;
-- 1
GRANT EXECUTE ON FUNCTION  projet.voir_offres_stages_validee(INTEGER) TO thibautdevos;
GRANT SELECT ON projet.offres_stage TO thibautdevos;
GRANT SELECT On projet.mots_cle_stage TO thibautdevos;
GRANT SELECT ON projet.mots_cle TO thibautdevos;
GRANT SELECT ON projet.entreprises TO thibautdevos;
--2
GRANT EXECUTE ON FUNCTION projet.recherche_offre_stage_mot_cle(INT, VARCHAR(20)) TO thibautdevos;
--3
GRANT EXECUTE ON FUNCTION projet.poser_candidature(INTEGER,  VARCHAR(20),  TEXT) TO thibautdevos;
GRANT SELECT ON projet.candidatures TO thibautdevos;
GRANT INSERT ON projet.candidatures TO thibautdevos;
GRANT UPDATE ON projet.candidatures TO thibautdevos;
GRANT UPDATE ON projet.etudiants TO thibautdevos;


GRANT SELECT ON  projet.voir_offres_stages_candidatures_depose TO thibautdevos;
GRANT EXECUTE ON FUNCTION projet.annuler_candidature(VARCHAR(50), INTEGER) TO thibautdevos;





-- Application Societe

GRANT CONNECT ON DATABASE dbxaviermassart TO chisomubah;
GRANT USAGE ON SCHEMA projet TO chisomubah;

GRANT SELECT ON projet.entreprises TO chisomubah;

--1
GRANT EXECUTE ON Function projet.ajouter_offre_stage( VARCHAR(3), TEXT ,CHAR(2)) TO chisomubah;
GRANT INSERT ON projet.offres_stage TO chisomubah;
GRANT SELECT ON projet.offres_stage TO chisomubah;
GRANT USAGE, SELECT ON projet.offres_stage_id_offre_stage_seq TO chisomubah;

--2
GRANT SELECT ON projet.mots_cle TO chisomubah;
--3
GRANT EXECUTE ON FUNCTION projet.ajout_mot_cle(CHAR(3), VARCHAR(20), VARCHAR(50)) TO chisomubah;
GRANT SELECT ON projet.mots_cle_stage TO chisomubah;
GRANT INSERT ON projet.mots_cle_stage TO chisomubah;
--4
GRANT SELECT ON projet.voir_offres_stage TO chisomubah;
--5
GRANT SELECT ON projet.voir_candidature_offre TO chisomubah;
GRANT SELECT ON projet.candidatures TO chisomubah;
GRANT SELECT ON projet.etudiants TO chisomubah;

--6
GRANT EXECUTE ON FUNCTION projet.selectionner_etudiant_offre( CHAR(3), VARCHAR(100), VARCHAR(100)) TO chisomubah;
GRANT UPDATE ON projet.offres_stage TO chisomubah;
GRANT UPDATE ON projet.candidatures TO chisomubah;

--7
GRANT EXECUTE ON FUNCTION projet.annuler_offre_stage(_entreprise CHAR(3), VARCHAR(50)) TO chisomubah;


-- Scénario de démo
--Ajout de Jean de
SELECT projet.encoder_etudiant('De', 'Jean', 'j.d@student.vinci.be', 'Q2',
                               '$2a$10$FIV5iSkZz8gDeK9wsN2nXe2oySp2XTrwrTaHco0DIK7rZr8MndRa6');
--mdp: mdp

--Ajout de Marc du
SELECT projet.encoder_etudiant('Du', 'Marc', 'm.d@student.vinci.be', 'Q1',
                               '$2a$10$xcv3RmYch9clNgaHrAUe7u5Ops07n/pMOnvKHGIGTTKNKqiCU1lji');
--mdp: mdp


-- Ajout de mots clé
SELECT projet.encoder_mot_cle('java');
SELECT projet.encoder_mot_cle('web');
SELECT projet.encoder_mot_cle('python');


-- Vinci
SELECT projet.encoder_entreprise('VIN', 'Haute École Léonard de Vinci', 'Pl. de l Alma 3, 1200 Woluwe-Saint-Lambert',
                                 'support@vinci.be',
                                 '$2a$10$YsQQcNKU/lAVcDiSHlmEzOL4tzFxmt3GA/GDC6iZy.4kY0u6G..iK'); --mdp: mdp

SELECT projet.ajouter_offre_stage('VIN', 'stage SAP', 'Q2'); -- VIN1
SELECT projet.ajouter_offre_stage('VIN', 'stage BI', 'Q2'); -- VIN2
SELECT projet.ajouter_offre_stage('VIN', 'stage Unity', 'Q2'); -- VIN3
SELECT projet.ajouter_offre_stage('VIN', 'stage IA', 'Q2'); -- VIN4
SELECT projet.ajouter_offre_stage('VIN', 'stage mobile', 'Q1'); -- VIN5


SELECT projet.ajout_mot_cle('VIN', 'java', 'VIN3');
SELECT projet.ajout_mot_cle('VIN', 'java', 'VIN5');

SELECT projet.valider_offre_stage('VIN1');
SELECT projet.valider_offre_stage('VIN4');
SELECT projet.valider_offre_stage('VIN5');

SELECT projet.poser_candidature(1, 'VIN4', 'Je suis motivé'); -- Jean De
SELECT projet.poser_candidature(2, 'VIN5', 'Je suis motivé');
-- Marc du


-- ULB
SELECT projet.encoder_entreprise('ULB', 'Université libre de Bruxelles',
                                 'Avenue Franklin Roosevelt 50 - 1050 Bruxelles', 'support@ulb.be',
                                 '$2a$10$HcX89w4B6ZPh1jPEjWdXm..c9xkGRZCr3UAjAXcSPWQFBrwoUli/G'); --mdp: mdp

SELECT projet.ajouter_offre_stage('ULB', 'stage javascript', 'Q2'); -- ULB1

SELECT projet.valider_offre_stage('ULB1');
