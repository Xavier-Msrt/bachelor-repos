DROP SCHEMA IF EXISTS preprojet CASCADE ; --- super important et facile
CREATE schema preprojet;



CREATE TABLE preprojet.utilisateurs
(
    id_utilisateur SERIAL PRIMARY KEY,
    nom VARCHAR(20) NOT NULL,
    prenom VARCHAR(20) NOT NULL,
    check ( TRIM(nom) <> ''),
    check ( TRIM(prenom) <> '')
);

CREATE TABLE preprojet.comptes
(
    id_compte SERIAL PRIMARY KEY,
    num_compte CHAR(10) UNIQUE NOT NULL,
    utilisateur INTEGER REFERENCES preprojet.utilisateurs(id_utilisateur) NOT NULL,
    check ( num_compte SIMILAR TO '[0-9]{4}-[0-9]{5}' )
);

CREATE TABLE preprojet.transactions
(
    id_transactions SERIAL PRIMARY KEY,
    source INTEGER REFERENCES preprojet.comptes(id_compte) NOT NULL,
    destinataire INTEGER REFERENCES preprojet.comptes(id_compte) NOT NULL,
    date_operation DATE NOT NULL,
    montant DOUBLE PRECISION NOT NULL,
    check ( montant > 0 ),
    check ( source <> destinataire )
);

INSERT INTO preprojet.utilisateurs(id_utilisateur, nom, prenom) VALUES (1,'Damas', 'Christopher');
INSERT INTO preprojet.utilisateurs(id_utilisateur, nom, prenom)  VALUES (2,'Cambron', 'Isabelle');
INSERT INTO preprojet.utilisateurs(id_utilisateur ,nom, prenom) VALUES (3, 'Ferneeuw', 'Stéphanie');

INSERT INTO preprojet.comptes(id_compte, num_compte, utilisateur) VALUES (1, '1234-56789', 1);
INSERT INTO preprojet.comptes(id_compte, num_compte, utilisateur) VALUES (2, '5632-12564', 2);
INSERT INTO preprojet.comptes(id_compte, num_compte, utilisateur) VALUES (3, '1236-02364', 2);
INSERT INTO preprojet.comptes(id_compte, num_compte, utilisateur) VALUES (4, '9876-87654', 1);
INSERT INTO preprojet.comptes(id_compte, num_compte, utilisateur) VALUES (5, '7896-23565', 3);


INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (1, 2, '01/12/2006',100);
INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (2, 3, '02/12/2006', 120);
INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (4, 5, '03/12/2006', 80);
INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (5, 4, '04/12/2006', 80);
INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (3, 5, '05/12/2006', 150);
INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (2, 3, '06/12/2006', 120);
INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (1, 2, '07/12/2006', 100);
INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (4, 5, '08/12/2006', 80);
INSERT INTO preprojet.transactions(source, destinataire, date_operation, montant) VALUES (5, 4, '09/12/2006', 80);

SELECT us.nom AS NomSource, us.prenom AS PrenomSource, cs.num_compte AS CompteSource,
       ud.nom AS NomDestination, ud.prenom AS PrenomDestination, cd.num_compte AS CompteDestination,
       t.date_operation AS DateOpération, t.montant AS Montant
FROM preprojet.comptes cs, preprojet.utilisateurs us, preprojet.comptes cd, preprojet.utilisateurs ud, preprojet.transactions t
WHERE cs.utilisateur = us.id_utilisateur
  AND cd.utilisateur = ud.id_utilisateur
  AND t.source = cs.id_compte
  AND t.destinataire = cd.id_compte
ORDER BY 7;

CREATE FUNCTION preprojet.operation(
    nomSource varchar(20), prenomSource VARCHAR(20), numCompteSource INTEGER,
    nomDest VARCHAR(20), prenomDest varchar(20), numCompteDest INTEGER,
    dateOp DATE, montant DOUBLE PRECISION
) RETURNS bool AS $$
DECLARE
    ligneSource INTEGER;
    ligneDest INTEGER;
BEGIN
    IF (montant < 0) THEN
        RAISE 'montant non possitif';
    END IF;



    SELECT COUNT(*)
    FROM utilisateurs u, comptes c
    WHERE u.id_utilisateur = c.utilisateur
    AND u.nom = nomSource
      AND u.prenom = prenomSource
      AND c.num_compte = numCompteSource
    INTO ligneSource;

    IF (ligneSource = 0) THEN
        RAISE 'source not found';
    END IF;

    SELECT COUNT(*)
    FROM utilisateurs u, comptes c
    WHERE u.id_utilisateur = c.utilisateur
    AND u.nom = nomDest
      AND u.prenom = prenomDest
      AND c.num_compte = numCompteDest
    INTO ligneDest;

    IF (ligneDest = 0) THEN
        RAISE 'dest not found';
    END IF;

    INSERT INTO transactions
    (source, destinataire, date_operation, montant) VALUES
    (numCompteSource, numCompteDest, dateOp, montant);



END;
$$ LANGUAGE plpgsql;