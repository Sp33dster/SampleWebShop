package pl.spjava.shop.ejb.managers;

import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.spjava.shop.ejb.endpoints.KontoEndpoint;
import pl.spjava.shop.ejb.facades.ZamowienieFacade;
import pl.spjava.shop.ejb.facades.ZamowienieFacade_Serializable;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.exception.AppBaseException;
import pl.spjava.shop.exception.ZamowienieException;
import pl.spjava.shop.model.Klient;
import pl.spjava.shop.model.PozycjaZamowienia;
import pl.spjava.shop.model.Pracownik;
import pl.spjava.shop.model.Zamowienie;


@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class ZamowienieManager extends AbstractManager
        implements SessionSynchronization {

    @Inject
    private KontoEndpoint kontoEndpoint;
    @Inject
    private ZamowienieFacade zamowienieFacade;
    @Inject
    private ZamowienieFacade_Serializable zamowienieFacade_Serializable;

    private Zamowienie zamowienieStan;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void setZamowienieStan(Zamowienie zamowienieStan) {
        this.zamowienieStan = zamowienieStan;
    }

    public void zlozZamowienie(Zamowienie zam) throws AppBaseException {

        //Zamówienie jest wstępnie przygotowane przez endpoint - są skonwertowane na encje pozycje zamówienia (mają stan nowych obiektów)
        Klient mojeKontoKlienta = kontoEndpoint.pobierzMojeKontoKlienta(); // Wersja produkcyjna przy zastosowaniu poprawnego uwierzytelniania
        // gdyby metoda była wykonywana na rzecz Konta nie będącego Klientem, działanie zostanie przerwane przez NoResultException

        //Związek między Klientem a Zamówieniami jest dwukierunkowy!
        zam.setKtoZlozyl(mojeKontoKlienta);
        mojeKontoKlienta.getZamowienia().add(zam);

        zamowienieFacade.create(zam);//dzięki kaskadzie PERSIST powinny być utworzone także pozycje zamówienia
    }

    // Ta metoda posluguje sie fasada, ktora uzywa jednostki skladowania opartej na puli polaczen z poziomem izolacji transakcji 'serializable'
    public void zatwierdzZamowienie() throws AppBaseException {
        if (null == zamowienieStan) { //encja zamowienia pobierana ze stanu co jest niezbędne dla mechnizmu blokad optymistycznych
            throw ZamowienieException.createNoStateInEJB();
        }

        //Czy aktualny stan produktów jest wystarczający dla zamówienia?
        //Wykorzystano poziom izolacji Serializable ze względu na możliwy tutaj wyścig
        for (PozycjaZamowienia poz : zamowienieStan.getPozycjeZamowienia()) {
            if (poz.getIlosc() <= poz.getProdukt().getStan()) {
                poz.getProdukt().setStan(poz.getProdukt().getStan() - poz.getIlosc());
            } else {
                throw ZamowienieException.createWithInsufficientProductAmount(zamowienieStan);
            }
        }

        Pracownik mojeKontoPracownika = kontoEndpoint.pobierzMojeKontoPracownika(); // Wersja produkcyjna przy zastosowaniu poprawnego uwierzytelniania
        // gdyby metoda była wykonywana na rzecz Konta nie będącego Pracownikiem, działanie zostanie przerwane przez NoResultException

        //Związek miedzy zamowieniem a zatwierdzajacym je pracownikiem jest jednokierunkowy
        zamowienieStan.setKtoZatwierdzil(mojeKontoPracownika);
        zamowienieStan.setZatwierdzone(true);

        zamowienieFacade_Serializable.edit(zamowienieStan); //utworzenie kopii encji ze stanem zarzadzalnym, dlatego metoda edit jest ostatnia w bloku
        zamowienieStan = null;
    }

    public void usunZamowienie(Long zamowienieId) throws AppBaseException {
        Zamowienie zam = zamowienieFacade_Serializable.find(zamowienieId);
        if (null != zam) {
            //Czy zamówienie nie zostało zatwierdzone?
            //Wykorzystano poziom izolacji Serializable ze względu na możliwy wyścig tutaj
            if (zam.isZatwierdzone()) {
                throw ZamowienieException.createWithAproveOfAproved(zam);
            } else {
                zamowienieFacade_Serializable.remove(zam);
            }
        }
    }
}
