DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;

CREATE TABLE pae.schoolYears
(
    idSchoolYear SERIAL PRIMARY KEY,
    dateStart    INTEGER NOT NULL,
    dateEnd      INTEGER NOT NULL
);

CREATE TABLE pae.users
(
    idUser           SERIAL PRIMARY KEY,
    versionUser      INTEGER NOT NULL,
    lastNameUser     TEXT    NOT NULL,
    firstNameUser    TEXT    NOT NULL,
    phoneNumberUser  TEXT    NOT NULL,
    emailUser        TEXT    NOT NULL,
    password         TEXT    NOT NULL,
    registrationDate DATE    NOT NULL,
    schoolYearIdUser INTEGER NOT NULL REFERENCES pae.schoolYears (idSchoolYear),
    role             TEXT    NOT NULL
);

CREATE TABLE pae.companies
(
    idCompany           SERIAL PRIMARY KEY,
    versionCompany      INTEGER NOT NULL,
    tradeName           TEXT    NOT NULL,
    designation         TEXT,
    street              TEXT    NOT NULL,
    city                TEXT    NOT NULL,
    postCode            TEXT    NOT NULL,
    boxNumber           TEXT    NOT NULL,
    phoneNumberCompany  TEXT,
    emailCompany        TEXT,
    blackListMotivation TEXT,
    isBlacklisted       BOOLEAN
);

CREATE TABLE pae.internshipSupervisors
(
    idSupervisor          SERIAL PRIMARY KEY,
    versionSupervisor     INTEGER NOT NULL,
    lastNameSupervisor    TEXT    NOT NULL,
    firstNameSupervisor   TEXT    NOT NULL,
    phoneNumberSupervisor TEXT    NOT NULL,
    emailSupervisor       TEXT,
    companyIdSupervisor   INTEGER NOT NULL REFERENCES pae.companies (idCompany)
);

CREATE TABLE pae.contacts
(
    versionContact      INTEGER NOT NULL,
    studentIdContact    INTEGER NOT NULL REFERENCES pae.users (iduser),
    companyIdContact    INTEGER NOT NULL REFERENCES pae.companies (idCompany),
    schoolYearIdContact INTEGER NOT NULL REFERENCES pae.schoolYears (idschoolyear),
    state               TEXT    NOT NULL,
    meetingPlace        TEXT,
    reasonRefusal       TEXT,
    PRIMARY KEY (studentIdContact, companyIdContact)
);

CREATE TABLE pae.internships
(
    idInternship           SERIAL PRIMARY KEY,
    versionInternship      INTEGER NOT NULL,
    studentIdInternship    INTEGER NOT NULL,
    companyIdInternship    INTEGER NOT NULL,
    schoolYearIdInternship INTEGER NOT NULL REFERENCES pae.schoolYears (idschoolyear),
    internSupervisorId     INTEGER NOT NULL REFERENCES pae.internshipSupervisors (idSupervisor),
    signatureDate          DATE    NOT NULL,
    internShipProject      TEXT,
    FOREIGN KEY (studentIdInternship, companyIdInternship) REFERENCES pae.contacts (studentIdContact, companyIdContact)
);


INSERT INTO pae.schoolYears(dateStart, dateEnd)
VALUES ('2020', '2021'),
       ('2021', '2022'),
       ('2022', '2023'),
       ('2023', '2024');


INSERT INTO pae.users(versionUser, lastNameUser, firstNameUser, phoneNumberUser, emailUser,
                      password, registrationDate,
                      schoolYearIdUser, role)
VALUES (1, 'Baroni', 'Raphaël', '0481 01 01 01', 'raphael.baroni@vinci.be',
        '$2a$10$xIxy6zrWOpGW.Pz/dDdXiuV7Qgph/jmf/kEjtjxZy5ko5zTlM5xQ6', '2020-09-21', 1, 'TEACHER'),
       (1, 'Lehmann', 'Brigitte', '0482 02 02 02', 'brigitte.lehmann@vinci.be',
        '$2a$10$xIxy6zrWOpGW.Pz/dDdXiuV7Qgph/jmf/kEjtjxZy5ko5zTlM5xQ6', '2020-09-21', 1, 'TEACHER'),
       (1, 'Leleux', 'Laurent', '0483 03 03 03', 'laurent.leleux@vinci.be',
        '$2a$10$xIxy6zrWOpGW.Pz/dDdXiuV7Qgph/jmf/kEjtjxZy5ko5zTlM5xQ6', '2020-09-21', 1, 'TEACHER'),
       (1, 'Lancaster', 'Annouck', '0484 04 04 04', 'annouck.lancaster@vinci.be',
        '$2a$10$XmEbX5t2061vyqf4NxYG/./XFjvAgidSDBBFqdZ7evah44wL8GJaO', '2020-09-21', 1,
        'ADMINISTRATIVE'),
       (1, 'Skile', 'Elle', '0491 00 00 01', 'elle.skile@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2021-09-21', 2, 'STUDENT'),
       (1, 'Ilotie', 'Basile', '0491 00 00 11', 'basile.ilotie@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2021-09-21', 2, 'STUDENT'),
       (1, 'Frilot', 'Basile', '0491 00 00 21', 'basile.frilot@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2021-09-21', 2, 'STUDENT'),
       (1, 'Ilot', 'Basile', '0492 00 00 01', 'basile.ilot@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2021-09-21', 2, 'STUDENT'),
       (1, 'Dito', 'Arnaud', '0493 00 00 01', 'arnaud.dito@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2021-09-21', 2, 'STUDENT'),
       (1, 'Dilo', 'Arnaud', '0494 00 00 01', 'arnaud.dilo@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2021-09-21', 2, 'STUDENT'),
       (1, 'Dilot', 'Cedric', '0495 00 00 01', 'cedric.dilot@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2021-09-21', 2, 'STUDENT'),
       (1, 'Linot', 'Auristelle', '0496 00 00 01', 'auristelle.linot@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2021-09-21', 2, 'STUDENT'),
       (1, 'Demoulin', 'Basile', '0496 00 00 02', 'basile.demoulin@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2022-09-23', 3, 'STUDENT'),
       (1, 'Moulin', 'Arthur', '0497 00 00 02', 'arthur.moulin@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2022-09-23', 3, 'STUDENT'),
       (1, 'Moulin', 'Hugo', '0497 00 00 03', 'hugo.moulin@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2022-09-23', 3, 'STUDENT'),
       (1, 'Mile', 'Aurèle', '0497 00 00 21', 'aurele.mile@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2022-09-23', 3, 'STUDENT'),
       (1, 'Mile', 'Frank', '0497 00 00 75', 'frank.mile@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2022-09-27', 3, 'STUDENT'),
       (1, 'Dumoulin', 'Basile', '0497 00 00 58', 'basile.dumoulin@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2022-09-27', 3, 'STUDENT'),
       (1, 'Dumoulin', 'Axel', '0497 00 00 97', 'axel.dumoulin@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2022-09-27', 3, 'STUDENT'),
       (1, 'Line', 'Caroline', '0486 00 00 01', 'caroline.line@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2023-09-18', 4, 'STUDENT'),
       (1, 'Ile', 'Achille', '0487 00 00 01', 'ach.ile@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2023-09-18', 4, 'STUDENT'),
       (1, 'Ile', 'Basile', '0488 00 00 01', 'basile.ile@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2023-09-18', 4, 'STUDENT'),
       (1, 'Skile', 'Achille', '0490 00 00 01', 'achille.skile@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2023-09-18', 4, 'STUDENT'),
       (1, 'Skile', 'Carole', '0489 00 00 01', 'carole.skile@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2023-09-18', 4,
        'STUDENT'),
       (1, 'Ile', 'Théophile', '0488 35 33 89', 'theophile.ile@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2024-03-01', 4, 'STUDENT'),
       (1, 'Demoulin', 'Jeremy', '0497 00 00 20', 'jeremy.demoulin@student.vinci.be',
        '$2a$10$nGG2MlPcfxa2drIavhwgwO4j9NffM3oN2wiTQ1VgmQ8MD4KJGLjAa', '2022-09-23', 3, 'STUDENT');


INSERT INTO pae.companies (versionCompany, tradeName, street, city, postCode, boxNumber,
                           phoneNumberCompany, emailCompany, blackListMotivation, isBlacklisted)
VALUES (1, 'Assyst Europe', 'Avenue du Japon', 'Braine-l''Alleud', '1420', '1/B9', '02.609.25.00',
        'global@example.com', NULL, FALSE),
       (1, 'AXIS SRL', 'Avenue de l''Hélianthe', 'Uccle', '1880', '63', '02 752 17 60 63', NULL, NULL, FALSE),
       (1, 'Infrabel', 'Rue Bara', 'Bruxelles', '1070', '135', '02 525 22 11', NULL, NULL, FALSE),
       (1, 'La route du papier', 'Avenue des Mimosas', 'Woluwe-Saint-Pierre', '1150', '83', '02 586 16 65', NULL, NULL,
        FALSE)
        ,
       (1, 'LetsBuild', 'Chaussée de Bruxelles', 'La Hulpe', '1310', '135A', '014 54 67 54',
        'healthtech@example.com', NULL, FALSE),
       (1, 'Niboo', 'Boulevard du Souverain', 'Watermael-Boisfort', '1170', '24', '0487 02 79 13',
        'creative@example.com', NULL, FALSE),
       (1, 'Sopra Steria', 'Avenue Arnaud Fraiteur', 'Bruxelles', '1050', '15/23', '02 566 66 66',
        'food@example.com', NULL, FALSE),
       (1, 'The Bayard Partnership', 'Grauwmeer', 'Leuven', '3001', '1/57 bte 55', '02 309 52 45', NULL, NULL, FALSE);

INSERT INTO pae.internshipSupervisors (versionSupervisor, lastNameSupervisor, firstNameSupervisor,
                                       phoneNumberSupervisor, emailSupervisor, companyIdSupervisor)
VALUES (1, 'Dossche', 'Stéphanie', '014.54.67.54', 'stephanie.dossche@letsbuild.com', 5),
       (1, 'ALVAREZ CORCHETE', 'Roberto', '02.566.60.14', NULL, 7),
       (1, 'Assal', 'Farid', '0474 39 69 09', 'f.assal@assyst-europe.com', 1),
       (1, 'Ile', 'Emile', '0489 32 16 54', NULL, 4),
       (1, 'Hibo', 'Owln', '0456 678 567', NULL, 3),
       (1, 'Barn', 'Henri', '02 752 17 60', NULL, 2);


INSERT INTO pae.contacts (versionContact, studentIdContact, companyIdContact, schoolYearIdContact,
                          state, meetingPlace, reasonRefusal)
VALUES (1, 24, 5, 4, 'ACCEPTED', 'REMOTE', NULL),
       (1, 21, 7, 4, 'ACCEPTED', 'ONSITE', NULL),
       (1, 21, 6, 4, 'TURNED_DOWN', 'REMOTE', 'N''ont pas accepté d''avoir un entretien'),
       (1, 22, 1, 4, 'ACCEPTED', 'ONSITE', NULL),
       (1, 22, 5, 4, 'ON_HOLD', 'REMOTE', NULL),
       (1, 22, 7, 4, 'ON_HOLD', NULL, NULL),
       (1, 22, 6, 4, 'TURNED_DOWN', 'ONSITE', 'ne prennent qu''un seul étudiant'),
       (1, 20, 6, 4, 'TURNED_DOWN', 'REMOTE', 'Pas d''affinité avec l''ERP ODOO'),
       (1, 20, 7, 4, 'INTERRUPTED', NULL, NULL),
       (1, 20, 5, 4, 'ADMITTED', 'REMOTE', NULL),
       (1, 25, 7, 4, 'STARTED', NULL, NULL),
       (1, 25, 6, 4, 'STARTED', NULL, NULL),
       (1, 25, 5, 4, 'STARTED', NULL, NULL),
       (1, 23, 7, 4, 'STARTED', NULL, NULL),
       (1, 5, 4, 2, 'ACCEPTED', 'REMOTE', NULL),
       (1, 8, 7, 2, 'INTERRUPTED', NULL, NULL),
       (1, 7, 8, 2, 'TURNED_DOWN', 'REMOTE', 'Ne prennent pas de stage'),
       (1, 9, 7, 2, 'ACCEPTED', 'ONSITE', NULL),
       (1, 10, 7, 2, 'ACCEPTED', 'ONSITE', NULL),
       (1, 11, 1, 2, 'ACCEPTED', 'ONSITE', NULL),
       (1, 11, 7, 2, 'TURNED_DOWN', 'ONSITE', 'Choix autre étudiant'),
       (1, 12, 3, 2, 'ACCEPTED', 'REMOTE', NULL),
       (1, 12, 7, 2, 'ON_HOLD', NULL, NULL),
       (1, 12, 6, 2, 'TURNED_DOWN', 'REMOTE', 'Choix autre étudiant'),
       (1, 13, 1, 3, 'ACCEPTED', 'REMOTE', NULL),
       (1, 14, 2, 3, 'ACCEPTED', 'ONSITE', NULL),
       (1, 15, 2, 3, 'ACCEPTED', 'ONSITE', NULL),
       (1, 16, 2, 3, 'ACCEPTED', 'REMOTE', NULL),
       (1, 17, 2, 3, 'ACCEPTED', 'REMOTE', NULL),
       (1, 18, 2, 3, 'TURNED_DOWN', 'ONSITE', 'Entretien n''a pas eu lieu'),
       (1, 18, 6, 3, 'TURNED_DOWN', 'ONSITE', 'Entretien n''a pas eu lieu'),
       (1, 18, 7, 3, 'TURNED_DOWN', 'REMOTE', 'Entretien n''a pas eu lieu'),
       (1, 19, 7, 3, 'ACCEPTED', 'REMOTE', NULL),
       (1, 7, 7, 3, 'TURNED_DOWN', 'REMOTE', 'Choix autre étudiant'),
       (1, 26, 1, 3, 'ACCEPTED', 'REMOTE', NULL);

INSERT INTO pae.internships (versionInternship, studentIdInternship, companyIdInternship,
                             schoolYearIdInternship, internSupervisorId, signatureDate,
                             internShipProject)
VALUES (1, 24, 5, 4, 1, '2023-10-10', 'Un ERP : Odoo'),
       (1, 21, 7, 4, 2, '2023-11-23', 'sBMS project - a complex environment'),
       (1, 22, 1, 4, 3, '2023-10-12', 'CRM : Microsoft Dynamics 365 For Sales'),
       (1, 5, 4, 2, 4, '2021-11-25', 'Conservation et restauration d’œuvres d’art'),
       (1, 9, 7, 2, 2, '2021-11-17', 'L''analyste au centre du développement'),
       (1, 10, 7, 2, 2, '2021-11-17', 'L''analyste au centre du développement'),
       (1, 11, 1, 2, 3, '2021-11-23', 'ERP : Microsoft Dynamics 366'),
       (1, 12, 3, 2, 5, '2021-11-22', 'Entretien des rails'),
       (1, 14, 2, 3, 6, '2022-10-19', 'Un métier : chef de projet'),
       (1, 15, 2, 3, 6, '2022-10-19', 'Un métier : chef de projet'),
       (1, 16, 2, 3, 6, '2022-10-19', 'Un métier : chef de projet'),
       (1, 17, 2, 3, 6, '2022-10-19', 'Un métier : chef de projet'),
       (1, 19, 7, 3, 2, '2022-10-17', 'sBMS project - Java Development'),
       (1, 26, 1, 3, 3, '2022-11-23', 'CRM : Microsoft Dynamics 365 For Sales');
