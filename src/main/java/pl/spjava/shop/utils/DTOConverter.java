package pl.spjava.shop.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import pl.spjava.shop.dto.AdministratorDTO;
import pl.spjava.shop.dto.KategoriaDTO;
import pl.spjava.shop.dto.KlientDTO;
import pl.spjava.shop.dto.KontoDTO;
import pl.spjava.shop.dto.PozycjaZamowieniaDTO;
import pl.spjava.shop.dto.PracownikDTO;
import pl.spjava.shop.dto.ProduktDTO;
import pl.spjava.shop.dto.ZamowienieDTO;
import pl.spjava.shop.model.Administrator;
import pl.spjava.shop.model.Kategoria;
import pl.spjava.shop.model.Klient;
import pl.spjava.shop.model.Konto;
import pl.spjava.shop.model.PozycjaZamowienia;
import pl.spjava.shop.model.Pracownik;
import pl.spjava.shop.model.Produkt;
import pl.spjava.shop.model.Zamowienie;

public class DTOConverter {

    // Konwersje z DTO na encje znajdują się w kodzie endpointów, ponieważ są zależne od logiki poszczególnych przypadków użycia
    // Rozwiązanie z instanceOF jest mało eleganckie, ale umożliwia konwersję listy encji na listę DTO z odwzorowaniem klas potomnych
    // dirty tradeoff
    public static KontoDTO tworzKontoDTOzEncji(Konto konto) {
        // Konto jest abstrakcyjna, obiekt musi być klasy potomnej

        if (konto instanceof Klient) {
            return tworzKlientDTOzEncji((Klient) konto);
        }

        if (konto instanceof Pracownik) {
            return tworzPracownikDTOzEncji((Pracownik) konto);
        }

        if (konto instanceof Administrator) {
            return tworzAdministratorDTOzEncji((Administrator) konto);
        }

        return null;
    }

    private static KlientDTO tworzKlientDTOzEncji(Klient klient) {
        return null == klient ? null : new KlientDTO(klient.getNip(), klient.getId(), klient.getLogin(), klient.isPotwierdzone(), klient.isAktywne(), klient.getTyp(), klient.getImie(), klient.getNazwisko(), klient.getEmail(), klient.getTelefon());
    }

    private static PracownikDTO tworzPracownikDTOzEncji(Pracownik pracownik) {
        return null == pracownik ? null : new PracownikDTO(pracownik.getIntercom(), pracownik.getId(), pracownik.getLogin(), pracownik.isPotwierdzone(), pracownik.isAktywne(), pracownik.getTyp(), pracownik.getImie(), pracownik.getNazwisko(), pracownik.getEmail(), pracownik.getTelefon());
    }

    private static AdministratorDTO tworzAdministratorDTOzEncji(Administrator administrator) {
        return null == administrator ? null : new AdministratorDTO(administrator.getAlarmNumber(), administrator.getId(), administrator.getLogin(), administrator.isPotwierdzone(), administrator.isAktywne(), administrator.getTyp(), administrator.getImie(), administrator.getNazwisko(), administrator.getEmail(), administrator.getTelefon());
    }

    public static List<KontoDTO> tworzListeKontoDTOzEncji(List<Konto> konta) {
        return null == konta ? null : konta.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.tworzKontoDTOzEncji(elt)) //metoda przeciążona ze względu na podtyp konta
                .collect(Collectors.toList());
    }

    public static ProduktDTO tworzProduktDTOzEncji(Produkt produkt) {
        return null == produkt ? null : new ProduktDTO(produkt.getId(), produkt.getNazwa(), produkt.getOpis(), produkt.getCena(), produkt.getStan());
    }

    public static List<ProduktDTO> tworzListeProduktDTOzEncji(List<Produkt> produkty) {
        return null == produkty ? null : produkty.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.tworzProduktDTOzEncji(elt))
                .collect(Collectors.toList());
    }

    public static ZamowienieDTO tworzZamowienieDTOzEncji(Zamowienie zamowienie) {
        return null == zamowienie ? null : new ZamowienieDTO(zamowienie.getId(), zamowienie.getModificationTimestamp(), zamowienie.isZatwierdzone(), tworzKlientDTOzEncji(zamowienie.getKtoZlozyl()), tworzPracownikDTOzEncji(zamowienie.getKtoZatwierdzil()), tworzListePozycjaZamowieniaDTOzEncji(zamowienie.getPozycjeZamowienia()));
    }

    public static List<ZamowienieDTO> tworzListeZamowienieDTOzEncji(List<Zamowienie> zamowienia) {
        return null == zamowienia ? null : zamowienia.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.tworzZamowienieDTOzEncji(elt))
                .collect(Collectors.toList());
    }

    public static KategoriaDTO tworzKategoriaDTOzEncji(Kategoria kategoria) {
        return null == kategoria ? null : new KategoriaDTO(kategoria.getId(), kategoria.getNazwa(), kategoria.getOpis());
    }

    public static List<KategoriaDTO> tworzListeKategoriaDTOzEncji(List<Kategoria> kategorie) {
        return null == kategorie ? null : kategorie.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.tworzKategoriaDTOzEncji(elt))
                .collect(Collectors.toList());
    }

    public static PozycjaZamowieniaDTO tworzPozycjaZamowieniaDTOzEncji(PozycjaZamowienia pozycja) {
        return null == pozycja ? null : new PozycjaZamowieniaDTO(pozycja.getId(), pozycja.getIlosc(), pozycja.getCena(), tworzProduktDTOzEncji(pozycja.getProdukt()));
    }

    public static List<PozycjaZamowieniaDTO> tworzListePozycjaZamowieniaDTOzEncji(List<PozycjaZamowienia> pozycjeZamowienia) {
        return null == pozycjeZamowienia ? null : pozycjeZamowienia.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.tworzPozycjaZamowieniaDTOzEncji(elt))
                .collect(Collectors.toList());
    }

}
