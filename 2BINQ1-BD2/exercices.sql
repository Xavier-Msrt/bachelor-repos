/*2.a.ii*/

-- 1
SELECT a.au_fname, a.au_id
FROM authors a
WHERE city = 'Oakland';

-- 2
SELECT a.au_lname, a.address, a.au_id
FROM authors a
WHERE upper(a.au_fname ) LIKE 'A%';

--3
SELECT a.au_id, a.au_fname, a.address, a.country, a.state, a.city
FROM authors a
WHERE a.phone = null;

--4
SELECT a.au_fname, a.au_id
FROM authors a
WHERE a.state = 'CA'
  AND a.phone LIKE '415%';

--5
SELECT a.au_fname, a.au_id
FROM authors a
WHERE  a.country = 'BEL' OR a.country = 'LUX' OR a.country = 'NED';

--6
SELECT DISTINCT t.pub_id
FROM titles t
WHERE t.type = 'psychology';

--7
SELECT  DISTINCT t.pub_id
FROM  titles t
WHERE  (t.price < 10 OR t.price > 25 )
  -- AND t.type NOT BETWEEN (10 AND 25); -- () obligatoir avec un OR
  AND t.type = 'psychology';


--8
SELECT DISTINCT a.city
FROM authors a
WHERE  a.state = 'CA'
  AND a.city IS NOT NULL --- !!!!!! attention ici la ville pourrais etre null
  AND (a.au_fname = 'Albert' OR a.au_lname LIKE '%er');

--9
SELECT DISTINCT a.state, a.country
FROM authors a
WHERE  a.state IS NOT NULL
  AND a.country != 'USA'; -- pas besoin de IS NOT NUll pour country car deja une comparaison

--10
SELECT DISTINCT t.type
FROM titles t
WHERE t.price < 15 AND t.type IS NOT NULL; -- !!! Ici t.tpye pourais etre NULL



/*2.b.iv*/

--1
SELECT t.title_id, t.title, t.price, p.pub_name-- pas de DISTINCT car on veux tout les livre  meme ceux qui ont le meme titre....
FROM titles t, publishers p
WHERE t.pub_id = p.pub_id;

--2
SELECT t.title_id, t.title, t.price, p.pub_name
FROM titles t, publishers p
WHERE t.pub_id = p.pub_id
    AND t.type = 'psychology';

--3
SELECT DISTINCT a.au_id, a.au_lname, a.au_fname
FROM authors a, titleauthor ta
WHERE a.au_id = ta.au_id ;

--4
SELECT DISTINCT  a.state
FROM authors a, titleauthor ta
WHERE a.au_id = ta.au_id
  AND a.state IS NOT NULL; -- ici state peux etre null on retire donc lecas ou stat pourais etre null

--5
SELECT DISTINCT st.stor_id, st.stor_name, st.stor_address
FROM stores st, sales sa
WHERE st.stor_id  = sa.stor_id
  AND date_part('year', sa.date) = 1991
  AND date_part('month', sa.date) = 11;

--6
SELECT t.title_id, t.title
FROM publishers p, titles t
WHERE p.pub_id = t.pub_id
    AND t.type = 'psychology'
    AND t.price < 20
    AND p.pub_name NOT LIKE 'Algo%';

--7
SELECT DISTINCT t.title_id, t.title -- DISTINCT car un livre peux etre ecrit pars pluiseurs auteur Californien
FROM  titles t, titleauthor ta, authors a
WHERE t.title_id = ta.title_id
  AND ta.au_id = a.au_id
  AND  a.state = 'CA';

--8
SELECT DISTINCT a.au_id, a.au_fname, a.au_lname -- DIStINCT car un auteur peux avoir plusieur livre
FROM authors a, titleauthor ta, titles t, publishers p
WHERE a.au_id = ta.au_id
  AND ta.title_id = t.title_id
  AND t.pub_id = p.pub_id
  AND p.state = 'CA';

--9
SELECT DISTINCT a.au_id, a.au_lname, a.au_fname
FROM authors a, titleauthor ta, titles t, publishers p
WHERE a.au_id = ta.au_id
  AND ta.title_id = t.title_id
  AND t.pub_id = p.pub_id
  AND p.state = a.state;

--10
SELECT DISTINCT p.pub_id, p.pub_name, p.city, p.state
FROM publishers p, titles t, salesdetail sd, sales sa
WHERE p.pub_id = t.pub_id
  AND t.title_id = sd.title_id
  AND sd.stor_id = sa.stor_id
  AND sa.ord_num = sd.ord_num
  AND ( sa.date > '1/11/1990' OR sa.date < '1/3/1991');

--11
SELECT DISTINCT st.stor_id, st.stor_name, st.state, st.city, st.stor_address
FROM stores st,  salesdetail sd, titles t
WHERE sd.stor_id = st.stor_id
  AND t.title_id = sd.title_id
  AND t.title SIMILAR TO '%[cC]ook%';

--12
SELECT t1.title_id, t1.title, t1.type, t1.pub_id, t1.price, t1.total_sales, t1.pubdate , t2.title_id, t2.title, t2.type, t2.pub_id, t2.price, t2.total_sales, t2.pubdate
FROM titles t1, titles t2
WHERE t1.pub_id =  t2.pub_id
  AND t1.pubdate = t2.pubdate
  AND t1.title_id < t2.title_id; -- ne pas avoir l5 avec l4 et l4 avec l5


--13
SELECT a.au_id, a.au_lname, a.au_fname, a.phone, a.address, a.city, a.state, a.country
FROM authors a, titleauthor ta, titles t
WHERE a.au_id = ta.au_id
  AND ta.title_id = t.title_id
GROUP BY a.au_id
HAVING COUNT(DISTINCT t.pub_id) > 1;
--revoir-------------------------------------------------------------------------------------


--14 --- DISTINCT car un livre peux avoir ete vendu avant la date de parution
SELECT DISTINCT t.title_id, t.title, t.type, t.pub_id, t.price, t.total_sales, t.pubdate
FROM titles t, salesdetail sd, sales sa
WHERE t.title_id = sd.title_id
  AND sd.ord_num = sa.ord_num -- double jointure
  AND sa.stor_id = sd.stor_id -- double jointure
  AND t.pubdate > sa.date; -- attention

--15
SELECT DISTINCT st.stor_id, st.stor_name
FROM stores st,  salesdetail sd,  titleauthor ta, authors a
WHERE st.stor_id = sd.stor_id
  AND ta.au_id = a.au_id
  AND ta.title_id = sd.title_id
  AND a.au_lname = 'Ringer'
  AND a.au_fname = 'Anne';

--16
SELECT DISTINCT  a.state
FROM authors a, titleauthor ta, salesdetail sd, sales sa, stores st
WHERE a.au_id = ta.au_id
  AND ta.title_id = sd.title_id
  AND sd.stor_id = sa.stor_id
  AND st.stor_id = sa.stor_id
  AND st.state = 'CA'
  AND date_part('year', sa.date) = 1991
  AND date_part('month', sa.date) = 2
  AND a.state IS NOT NULL;


--17
SELECT st1.stor_id, st1.stor_name, st1.stor_address, st1.city, st1.state, st1.country
FROM stores st1, salesdetail sd1, stores st2, salesdetail sd2, titleauthor ta1 , titleauthor ta2
WHERE st1.state = st2.state -- meme etat
  AND sd1.title_id = ta1.title_id
  AND st2.stor_id = sd2.stor_id AND sd2.title_id = ta2.title_id
  AND ta1.au_id = ta2.au_id AND st1.stor_id < st2.stor_id AND st1.state = st2.stor_id;



--18
SELECT DISTINCT a1.au_id, a1.au_lname, a1.au_fname, a1.phone, a1.address, a1.city, a1.state, a1.country, a2.au_id, a2.au_lname, a2.au_fname, a2.phone, a2.address, a2.city, a2.state, a2.country
FROM authors a1, titleauthor ta1, authors a2, titleauthor ta2
WHERE ta1.title_id = ta2.title_id
  AND ta1.au_id = a1.au_id
  AND ta2.au_id = a2.au_id
  AND a1.au_id < a2.au_id;


--19
/*
 pas de distinct on remplace pars sd.stor_id, sd.ord_num, sd.title_id en plus
 car on veux garde pour chaque vente le nombre d'achat,.... et le magasin donc pas de distinct sinon on perd des infos
 */
SELECT sd.stor_id, sd.ord_num, sd.title_id, t.title, st.stor_name, t.price, sd.qty,
       (t.price * sd.qty) AS total, (t.price * sd.qty) * 1.02 AS ecotax
FROM salesdetail sd, titles t, stores st
WHERE t.title_id = sd.title_id
  AND sd.title_id = t.title_id;


/*
 !!!! attention
 pour chaque auteur -> on reprend la pk de auteur
 pour chaque livre -> on prend la pk de livre
 .....

 !!! pas de distinct dans les fonction agrege SAUF pour count
 */




/*2.d.ii*/
--1
SELECT AVG(t.price) AS prix_moyen
FROM titles t, publishers p
WHERE t.pub_id = p.pub_id
  AND p.pub_name = 'Algodata Infosystems';

--2 --- pour chaque auteur donc touse meme ceux qui n'on pas ecrit de livre (matiere de plutard)
/*
mettre le a.au_id car sinon on peux comfondre des auteurs avec des noms !!! metre au_id
*/
SELECT a.au_id, a.au_lname, a.au_fname, AVG(t.price) AS prix_moyen
FROM authors a, titleauthor ta, titles t
WHERE a.au_id = ta.au_id
  AND ta.title_id = t.title_id
GROUP BY a.au_id, a.au_lname, a.au_fname;

--3
SELECT t.title_id, t.price, COUNT(ta.au_id)
FROM publishers p, titles t, titleauthor ta
WHERE ta.title_id = t.title_id
  AND t.pub_id = p.pub_id
  AND p.pub_name = 'Algodata Infosystems'
GROUP BY t.title_id, t.price;

--3 correction -- problematique les livre qui n'ont pas d'auteur n'apperetron plus
SELECT t.title_id, t.price, COUNT(ta.au_id)
FROM  titles t LEFT OUTER JOIN titleauthor ta
ON ta.title_id = t.title_id
WHERE t.pub_id IN (SELECT t.pub_id FROM publishers p WHERE p.pub_name = 'Algodata Infosystems')
GROUP BY t.title_id, t.price;

--3 ou sens sub select ~~ bizzars potentiellement faux
SELECT t.title_id, t.price, COUNT(ta.au_id)
FROM  publishers p ,titles t LEFT OUTER JOIN titleauthor ta
ON ta.title_id = t.title_id
WHERE  p.pub_id = t.pub_id AND p.pub_name = 'Algodata Infosystems'
GROUP BY t.title_id, t.price;


--4 --mettre le t.title_id car des livre peux etre confondu (meme nom,...)
-- si un livre n'as jamais ete vendu il ne sera pas repris cause jointure matier plutard
SELECT t.title_id, t.title, t.price, COUNT(DISTINCT sd.stor_id) -- on ne doit pas prendre store car on peux comaprer les id_store dans salesdetails
FROM titles t, salesdetail sd
WHERE t.title_id = sd.title_id
GROUP BY t.title_id, t.title, t.price;

--5 -- important mettre id dans le select pour etre sur de ne pas confondre
SELECT  t.title_id, t.title, t.type, t.pub_id, t.price, t.total_sales, t.pubdate
FROM titles t, salesdetail sd
WHERE t.title_id = sd.title_id
GROUP BY t.title_id, t.title, t.price
HAVING COUNT(DISTINCT sd.stor_id) > 1;

--6
SELECT t.type, COUNT(t.title_id) AS total_livre, AVG(t.price) as prix_moyen
FROM titles t
WHERE t.type IS NOT NULL --attention des livres peuvent n'appartenir a aucun type
GROUP BY t.type;

--7
SELECT t.title_id, t.total_sales, SUM(sd.qty) AS total_vendu
FROM titles t, salesdetail sd
WHERE t.title_id = sd.title_id
GROUP BY t.title_id, t.total_sales;


--8
SELECT t.title_id, t.total_sales, SUM(sd.qty) AS total_vendu
FROM titles t, salesdetail sd
WHERE t.title_id = sd.title_id
GROUP BY t.title_id, t.total_sales
HAVING t.total_sales != SUM(sd.qty);

--9
SELECT t.title_id, t.title
FROM titleauthor ta, titles t
WHERE ta.title_id = t.title_id
GROUP BY t.title_id, t.title
HAVING COUNT(ta.au_id) > 2;

--10 --~~~~ on ne peux pas prendre tout les livre qui on ete commande ne une command exemple: jean a commande 16x le livre 'The sky' ici on ne le consider que 1 fois
SELECT SUM(sd.qty)
FROM  titles t, publishers p, salesdetail sd, stores s
WHERE t.title_id = sd.title_id
  AND t.pub_id = p.pub_id
  AND sd.stor_id = s.stor_id
  AND p.state = 'CA' AND s.state = 'CA'
  AND EXISTS(SELECT ta.title_id
             FROM titleauthor ta, authors au
             WHERE ta.au_id = au.au_id
               AND au.state = 'CA'
               AND t.title_id = ta.title_id);


/*2.e.iv */

--1
SELECT t.title_id, t.title
FROM titles t, publishers p
WHERE t.pub_id = p.pub_id
  AND p.pub_name = 'Algodata Infosystems'
  AND t.price = (SELECT MAX(t.price)
                 FROM titles
                 WHERE  p.pub_name = 'Algodata Infosystems');

--2
SELECT t.title_id , t.title
FROM titles t
WHERE EXISTS(SELECT sd.title_id
             FROM salesdetail sd
             WHERE t.title_id = sd.title_id --- pour faire le lien
             GROUP BY sd.title_id
            HAVING COUNT(DISTINCT sd.stor_id) > 1);
-- ou
SELECT t.title_id , t.title
FROM titles t
WHERE t.title_id IN(SELECT sd.title_id
             FROM salesdetail sd
             GROUP BY sd.title_id
            HAVING COUNT(DISTINCT sd.stor_id) > 1);

--3
-- qu'elle sont pour chaque type les livre qui sont plus cher que la moyen * 1.5

SELECT t1.title, type
FROM titles t1
WHERE t1.price >  (SELECT AVG(t2.price) * 1.5
                         FROM titles t2
                         WHERE t1.type = t2.type );
--4
SELECT DISTINCT a.au_id, a.au_fname, a.au_lname
FROM authors a, titleauthor ta, titles t, publishers p
WHERE a.au_id = ta.au_id
  AND ta.title_id = t.title_id
  AND p.pub_id = t.pub_id
  AND p.state = a.state;
--ou
SELECT DISTINCT a.au_id, a.au_fname, a.au_lname
FROM authors a, titleauthor ta
WHERE a.au_id = ta.au_id
  AND  a.state IN (SELECT p.state
                   FROM publishers p, titles t
                   WHERE t.title_id= ta.title_id --un editeur qui a publier le livre de l'auteur
                     AND t.pub_id = p.pub_id);


--5
SELECT p.pub_id, p.pub_name, p.city, p.state
FROM publishers p
WHERE p.pub_id NOT IN (SELECT t.pub_id FROM titles t);

--6 -- pas fini je comprend pas
SELECT p.pub_id, pub_name, city, state
FROM publishers p, titles t
WHERE p.pub_id = t.pub_id
GROUP BY p.pub_id, pub_name, city, state
HAVING COUNT(t.title_id) >= ALL(SELECT COUNT(t2.title_id)
/*ici on donne pour chaque publisher le nombre de livre et on compare avec notre table */
                                FROM titles t2
                               GROUP BY t2.pub_id);--- on fait une table pars editeur du nombre de livre que il on editer



--7
SELECT p.pub_id, p.pub_name, p.city, p.state
FROM publishers p
WHERE p.pub_id NOT IN (SELECT t.pub_id
                     FROM salesdetail sd, titles t
                     WHERE t.title_id = sd.title_id);


--8 livre auteur ca | pub CA | pas vendu en CA
SELECT t.title_id, t.title, t.type, t.pub_id, t.price, t.total_sales, t.pubdate
FROM titles t
WHERE t.pub_id IN (SELECT p.pub_id FROM publishers p WHERE p.state = 'CA')
  AND t.title_id IN (SELECT ta.title_id FROM authors a, titleauthor ta WHERE a.au_id = ta.au_id AND a.state = 'CA')
  AND t.title_id NOT IN (SELECT sd.title_id FROM salesdetail sd, stores s WHERE sd.stor_id = s.stor_id AND s.state = 'CA');
--ou
SELECT t.title_id, t.title, t.type, t.pub_id, t.price, t.total_sales, t.pubdate
FROM titles t, publishers p, authors a, titleauthor ta
WHERE a.au_id = ta.au_id
  AND ta.title_id = t.title_id
  AND p.pub_id = t.pub_id
  AND p.state = 'CA'
  AND a.state = 'CA'
  AND t.title_id NOT IN (SELECT sd.title_id
                         FROM salesdetail sd, stores s
                         WHERE sd.stor_id = s.stor_id
                           AND s.state = 'CA');


--9 -- pas fini je comprend pas comment le faire
SELECT DISTINCT t.title_id, t.title -- distinct car si un livre vendu le meme jour on ne le veux que 1 fois
FROM titles t, salesdetail sd, sales s
WHERE t.title_id = sd.title_id
  AND sd.stor_id = s.stor_id
  AND sd.ord_num = s.ord_num
AND s.date >= ALL(SELECT sa2.date FROM sales sa2);
-- sd.date doit etre plus grand parmis tout les date (vente)

-- on selection chaque livre pour chaque vente et on regarde si la date est de ma vente est > ou = a la tout les vente réaliser
--ou
SELECT DISTINCT t.title_id, t.title
FROM titles t, salesdetail sd, sales s
WHERE t.title_id = sd.title_id
  AND sd.stor_id = s.stor_id
  AND sd.ord_num = s.ord_num
AND s.date = (SELECT MAX(sa2.date) FROM sales sa2);


--10
SELECT DISTINCT st.stor_id, st.stor_name, st.stor_address, st.city, st.state, st.country
FROM stores st
WHERE st.stor_name != 'Bookbeat'
AND (SELECT COUNT(DISTINCT sd1.title_id)
     FROM salesdetail sd1, stores st1
     WHERE sd1.stor_id = st1.stor_id AND st1.stor_name = 'Bookbeat') --- nombre de livre vendu par bookbeat
= (SELECT COUNT(DISTINCT sd2.title_id)
   FROM salesdetail sd2, salesdetail sd3, stores st3
   WHERE  sd2.stor_id = st.stor_id AND sd2.title_id = sd3.title_id AND sd2.stor_id = sd3.stor_id AND st3.stor_name = 'Bookbeat');


--11
SELECT DISTINCT a.city
FROM authors a
WHERE a.state = 'CA' AND a.city NOT IN (SELECT s.city FROM stores s WHERE s.state = 'CA');
    -- list de tout les villes ou il y a des magasin en californie et on retire ca de la list d'auteur
--ou
SELECT DISTINCT a.city
FROM authors a
WHERE a.state = 'CA'
EXCEPT (SELECT s.city
        FROM stores s
        WHERE s.state = 'CA');


--12
SELECT p.pub_id, p.pub_name, p.city, p.state
FROM publishers p
WHERE p.city = (SELECT a.city
                 FROM authors a
                 GROUP BY a.city
                 HAVING COUNT(a.city) >= ALL(SELECT COUNT(a.city)
                                              FROM authors a
                                              GROUP BY a.city));

--13 -- tout les titres don le auteur sont ca
SELECT DISTINCT t.title_id, t.title
FROM titles t, titleauthor ta
WHERE  t.title_id = ta.title_id
  AND  t.title_id NOT IN (SELECT t2.title_id
                        FROM titleauthor ta2, authors a1, titles t2
                        WHERE ta2.au_id = a1.au_id
                          AND ta2.title_id = t2.title_id
                          AND a1.state != 'CA');



--14 Quels sont les livres qui n'ont été écrits par aucun auteur californien ?
SELECT DISTINCT t.title_id, t.title, t.type, t.pub_id, t.price, t.total_sales, t.pubdate
FROM titles t
WHERE t.title_id NOT IN (SELECT ta.title_id
                        FROM titleauthor ta, authors a
                        WHERE ta.au_id = a.au_id
                          AND a.state = 'CA');



--15 Quels sont les livres qui n'ont été écrits que par un seul auteur ?
SELECT t.title_id, t.title, t.type, t.pub_id, t.price, t.total_sales, t.pubdate
FROM titleauthor ta, titles t
WHERE  ta.title_id = t.title_id
GROUP BY t.title_id
HAVING COUNT(ta.au_id) = 1 ; --OU COUNT(t.au_id)


--16 Quels sont les livres qui n'ont qu'un auteur, et tels que cet auteur soit californien ?
-- !!! un livre qui a plusieur auteur (et que l'un entreux est CA ) ne doivent pas etre pris avec
SELECT t.title_id, t.title, t.type, t.pub_id, t.price, t.total_sales, t.pubdate
FROM titleauthor ta, authors a, titles t
WHERE ta.au_id = a.au_id
  AND ta.title_id = t.title_id
  AND a.state = 'CA'
AND  1 = (SELECT  count(ta1.au_id) FROM titleauthor ta1 WHERE ta1.title_id = t.title_id); -- on regarde que pour chaque livre il y a que 1 auteur car dans le select audessus on filtre 'CA' donc si un livre a un CA, BA il ne doit pas etre repris




/*2.g.ii*/
-- fair celui la aux tableau
--1 Donnez la liste des magasins, en ordre alphabétique, en mentionnant pour chacun le chiffre d'affaire total.
SELECT  s.stor_id, s.stor_name, COALESCE(SUM(sd.qty * t.price) , 0) AS chiffre_affaire
FROM stores s LEFT OUTER JOIN salesdetail sd ON s.stor_id = sd.stor_id LEFT OUTER JOIN titles t ON sd.title_id = t.title_id
WHERE  sd.title_id = t.title_id
GROUP BY s.stor_id, s.stor_name
ORDER BY 2;


--2 Donnez la liste des magasins, en mentionnant pour chacun le chiffre d'affaire total.
-- Classez cette liste par ordre décroissant de chiffre d'affaire.
SELECT  s.stor_id, s.stor_name, COALESCE(SUM(sd.qty * t.price) , 0)  AS chiffre_affaire
FROM stores s LEFT OUTER JOIN salesdetail sd ON s.stor_id = sd.stor_id LEFT OUTER JOIN titles t ON sd.title_id = t.title_id
WHERE  sd.title_id = t.title_id
GROUP BY s.stor_id, s.stor_name
ORDER BY 3;

--3 Donnez la liste des livres de plus de $20, classés par type, en donnant pour chacun son type,
-- son titre, le nom de son éditeur et son prix.
SELECT t.title_id, t.title, t.type, p.pub_name, t.price
FROM titles t INNER JOIN publishers p ON t.pub_id = p.pub_id
WHERE t.price > 20
ORDER BY  t.type;-- !! attention ici pas de pert de livre donc pas de OUTER JOIN !!!! on veux que les livre de 20+

--4 Donnez la liste des livres de plus de $20, classés par type, en donnant pour chacun son type,
-- son titre, les noms de ses auteurs et son prix.
SELECT  t.title_id, t.title, t.type
FROM  titles t LEFT OUTER JOIN  titleauthor ta ON t.title_id = ta.title_id
    LEFT OUTER JOIN authors a ON ta.au_id = a.au_id
WHERE t.price > 20
ORDER BY  t.type;-- OUTER JOIN car pert de livre si pas d'auteu qui la ecrit (bible, dico,....)



--5
(SELECT a.city FROM authors a WHERE a.state = 'CA' AND a.city IS NOT NULL -- !! la ville peux estr null
UNION -- garde les null attention
SELECT p.city FROM publishers p WHERE p.state = 'CA' AND p.city IS NOT NULL)-- !! la ville peux estr null
EXCEPT
SELECT s.city FROM stores s WHERE s.state = 'CA' AND s.city IS NOT NULL;-- !! la ville peux estr null

--6
-- Donnez la liste des auteurs en indiquant pour chacun, outre son nom et son prénom, le
-- nombre de livres de plus de 20 $ qu'il a écrits.
--
-- Classez cela par ordre décroissant de nombre de livres,
-- et, en cas d'ex aequo, par ordre alphabétique. N'oubliez pas les auteurs qui n'ont écrit aucun livre de
-- plus de 20 $ !

SELECT a.au_lname, a.au_fname, COUNT(t.title_id) AS nb_livre -- attention on doit mettre la derrnier table du OUTER JOIN car la jointure ce fait de gauche a droite 
FROM authors a LEFT OUTER  JOIN titleauthor ta ON a.au_id = ta.au_id LEFT OUTER JOIN titles t ON ta.title_id = t.title_id AND t.price > 20
GROUP BY a.au_lname, a.au_fname
ORDER BY  3 DESC, a.au_lname, a.au_fname;


--2.h.ii
SELECT a.au_lname, a.au_fname, a.city, a.state, a.address, a.country, t.title_id, COALESCE(t.title, 'aucun livre') AS title
FROM authors a LEFT OUTER JOIN titleauthor ta ON a.au_id = ta.au_id
    LEFT OUTER JOIN titles t ON ta.title_id = t.title_id
ORDER BY 6;





























