/*2.b.iv*/


--1
SELECT t.title_id, t.title, t.price, p.pub_name
FROM titles t INNER JOIN publishers p ON t.pub_id = p.pub_id;

--2
SELECT t.title_id, t.title, t.price, p.pub_name
FROM titles t INNER JOIN publishers p ON t.pub_id = p.pub_id
WHERE t.type = 'psychology';

--3
SELECT DISTINCT a.au_id, a.au_lname, a.au_fname, a.city, a.state, a.address, a.country, a.phone
FROM authors a INNER JOIN titleauthor ta ON a.au_id = ta.au_id;

--4
SELECT DISTINCT a.state
FROM  authors a INNER JOIN titleauthor ta ON a.au_id = ta.au_id
WHERE a.state IS NOT NULL;

--5
SELECT DISTINCT st.stor_id, st.stor_name, st.stor_address
FROM sales s INNER JOIN stores st ON s.stor_id = st.stor_id
WHERE date_part('year', s.date) = 1991 AND date_part('month', s.date) = 11;

--6
SELECT t.title_id, t.title, t.type, t.pub_id, t.price, t.total_sales, t.pubdate
FROM titles t INNER JOIN publishers p ON t.pub_id = p.pub_id
WHERE t.type = 'psychology'
  AND t.price < 20
  AND p.pub_name NOT LIKE 'Algo%';

--7
SELECT DISTINCT t.title_id, t.title
FROM authors a INNER JOIN titleauthor ta ON a.au_id = ta.au_id
    INNER JOIN titles t ON ta.title_id = t.title_id
WHERE  a.state = 'CA';

--8
SELECT DISTINCT a.au_id, a.au_fname, a.au_lname
FROM authors a INNER JOIN titleauthor ta ON a.au_id = ta.au_id
    INNER JOIN titles t ON ta.title_id = t.title_id
    INNER JOIN publishers p ON  t.pub_id = p.pub_id
WHERE p.state = 'CA';

--9
SELECT DISTINCT a.au_id, a.au_lname, a.au_fname
FROM authors a INNER JOIN titleauthor ta ON a.au_id = ta.au_id
    INNER JOIN titles t ON ta.title_id = t.title_id
    INNER JOIN publishers p ON t.pub_id = p.pub_id
WHERE a.state = p.state;

--10
SELECT DISTINCT p.pub_id, p.pub_name
FROM publishers p INNER JOIN titles t ON p.pub_id = t.pub_id
    INNER JOIN salesdetail sd ON t.title_id = sd.title_id
    INNER JOIN sales s ON sd.stor_id = s.stor_id AND sd.ord_num = s.ord_num
WHERE  s.date > '1/11/1990' OR s.date < '1/3/1991';


--11
SELECT DISTINCT s.stor_id, s.stor_name, s.stor_address, s.city, s.country, s.state
FROM stores s INNER JOIN salesdetail sd ON s.stor_id = sd.stor_id
    INNER JOIN titles t ON t.title_id = sd.title_id
WHERE t.title SIMILAR TO '%[cC]ook%';
