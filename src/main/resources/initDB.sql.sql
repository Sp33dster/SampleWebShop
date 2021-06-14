CREATE VIEW DaneLogowanie AS 
SELECT login,haslo,typ FROM KONTO WHERE potwierdzone=1 AND aktywne=1;

DELETE FROM SHOP.POZYCJAZAMOWIENIA;
DELETE FROM SHOP.ZAMOWIENIE;
DELETE FROM SHOP.PRODUKTYKATEGORIE;
DELETE FROM SHOP.PRODUKT;
DELETE FROM SHOP.KATEGORIA;

DELETE FROM SHOP.DANEPRACOWNIK;
DELETE FROM SHOP.DANEADMINISTRATOR;
DELETE FROM SHOP.DANEKLIENT;
DELETE FROM SHOP.DANEPERSONALNE;
DELETE FROM SHOP.KONTO;

INSERT INTO SHOP.KONTO (ID, TYP, AKTYWNE, HASLO, LOGIN, POTWIERDZONE, WERSJA) VALUES (1, 'KLIENT', 1, 'hasloklient', 'klient', 1, 1);
INSERT INTO SHOP.DANEPERSONALNE (ID, EMAIL, IMIE, NAZWISKO, TELEFON) VALUES (1, 'klient@nowhere.com', 'Jan', 'Kliencki', '000999888');
INSERT INTO SHOP.DANEKLIENT (ID, NIP) VALUES (1, '7277370107');

INSERT INTO SHOP.KONTO (ID, TYP, AKTYWNE, HASLO, LOGIN, POTWIERDZONE, WERSJA) VALUES (2, 'PRACOWNIK', 1, 'hasloprac', 'prac', 1, 1);
INSERT INTO SHOP.DANEPERSONALNE (ID, EMAIL, IMIE, NAZWISKO, TELEFON) VALUES (2, 'prac@here.com', 'Stefan', 'Pracowniczy', '111222333');
INSERT INTO SHOP.DANEPRACOWNIK (ID, INTERCOM) VALUES (2, '14');

INSERT INTO SHOP.KONTO (ID, TYP, AKTYWNE, HASLO, LOGIN, POTWIERDZONE, WERSJA) VALUES (3, 'ADMIN', 1, 'hasloadmin', 'admin', 1, 1);
INSERT INTO SHOP.DANEPERSONALNE (ID, EMAIL, IMIE, NAZWISKO, TELEFON) VALUES (3, 'admin@here.com', 'Zdzisław', 'Administratorski', '555666555');
INSERT INTO SHOP.DANEADMINISTRATOR (ID, ALARMNUMBER) VALUES (3, '13');



INSERT INTO SHOP.KATEGORIA (ID, NAZWA, OPIS, WERSJA) VALUES (1, 'Różności', 'Przyczłapka', 0);
INSERT INTO SHOP.KATEGORIA (ID, NAZWA, OPIS, WERSJA) VALUES (2, 'Edukacja', 'Produkty do użytku w szkołach', 0);
INSERT INTO SHOP.KATEGORIA (ID, NAZWA, OPIS, WERSJA) VALUES (3, 'RTV', 'Elektronika domowa', 0);
INSERT INTO SHOP.PRODUKT (ID, CENA, NAZWA, OPIS, STAN, WERSJA) VALUES (1, 10.00, 'Przyczłapka', 'Do bulbulatora', 7, 0);
INSERT INTO SHOP.PRODUKT (ID, CENA, NAZWA, OPIS, STAN, WERSJA) VALUES (2, 7.65, 'Bulbulator', 'Bez przyczłapek', 13, 0);
INSERT INTO SHOP.PRODUKT (ID, CENA, NAZWA, OPIS, STAN, WERSJA) VALUES (3, 2.99, 'Bateryjka', NULL, 118, 0);
INSERT INTO SHOP.PRODUKT (ID, CENA, NAZWA, OPIS, STAN, WERSJA) VALUES (4, 59.90, 'Globus', 'Z podświetleniem', 1, 0);
INSERT INTO SHOP.PRODUKT (ID, CENA, NAZWA, OPIS, STAN, WERSJA) VALUES (5, 12.00, 'Dzwonek ręczny', 'Drewno + mosiądz', 3, 0);
INSERT INTO SHOP.PRODUKT (ID, CENA, NAZWA, OPIS, STAN, WERSJA) VALUES (6, 265, 'Patefon', 'Typu korbotronik', 1, 0);
INSERT INTO SHOP.PRODUKT (ID, CENA, NAZWA, OPIS, STAN, WERSJA) VALUES (7, 25.55, 'Radio', 'Odbiera tylko słuszne stacje', 2, 0);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (1, 1);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (2, 1);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (3, 1);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (3, 3);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (4, 1);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (4, 2);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (5, 2);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (6, 3);
INSERT INTO SHOP.PRODUKTYKATEGORIE (PRODUKT_ID, KATEGORIA_ID) VALUES (7, 3);
