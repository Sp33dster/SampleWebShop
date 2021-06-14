package pl.spjava.shop.ejb.endpoints;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.spjava.shop.dto.PozycjaZamowieniaDTO;
import pl.spjava.shop.dto.ZamowienieDTO;
import pl.spjava.shop.ejb.facades.ProduktFacade;
import pl.spjava.shop.ejb.facades.ZamowienieFacade;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.ejb.interceptor.PerformanceInterceptor;
import pl.spjava.shop.ejb.managers.ZamowienieManager;
import pl.spjava.shop.exception.AppBaseException;
import pl.spjava.shop.exception.ZamowienieException;
import pl.spjava.shop.model.PozycjaZamowienia;
import pl.spjava.shop.model.Produkt;
import pl.spjava.shop.model.Zamowienie;
import pl.spjava.shop.utils.DTOConverter;

@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.NEVER)
@Interceptors(LoggingInterceptor.class)
public class ZamowienieEndpoint {

    @Inject
    private KontoEndpoint kontoEndpoint;

    @Inject
    private ZamowienieManager zamowienieManager;
    @Inject
    private ZamowienieFacade zamowienieFacade;
    @Inject
    private ProduktFacade produktFacade;// niezbędna do wczytania produktów przy składaniu nowego zamówienia

    @Resource(name = "txRetryLimit")
    private int txRetryLimit;

    //Ta pozornie bezsensowna operacja powoduje odświeżenie zamówienia przed wyświetleniem jego szczegółów
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ZamowienieDTO pobierzZamowienie(ZamowienieDTO zamowienieDTO) throws AppBaseException {
        //Złożone już zamówienie nie podlega innym modyfikacjom niż usunięcie lub zatwierdzenie.
        //W obu tych przypadkach wczesniejszy stan encji nie ma znaczenia, stąd nie zachowujemy obiektu encji w stanie komponentu EJB
        Zamowienie zamowienie = zamowienieFacade.find(zamowienieDTO.getId());
        if (null == zamowienie) {
            throw ZamowienieException.createWithNotFound();
        } else {
            zamowienieManager.setZamowienieStan(zamowienie);
        }

        return DTOConverter.tworzZamowienieDTOzEncji(zamowienie);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ZamowienieDTO> pobierzWszystkieZamowienia() {
        return DTOConverter.tworzListeZamowienieDTOzEncji(zamowienieFacade.findAll());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ZamowienieDTO> pobierzZamowieniaNieZatwierdzone() {
        return DTOConverter.tworzListeZamowienieDTOzEncji(zamowienieFacade.znajdzZamowieniaNieZatwierdzone());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ZamowienieDTO> pobierzMojeZamowienia() {
        return DTOConverter.tworzListeZamowienieDTOzEncji(zamowienieFacade.znajdzZamowieniaDlaKlienta(kontoEndpoint.pobierzMojLogin())); // Wersja produkcyjna przy zastosowaniu poprawnego uwierzytelniania
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ZamowienieDTO> pobierzMojeZamowieniaNieZatwierdzone() {
        return DTOConverter.tworzListeZamowienieDTOzEncji(zamowienieFacade.znajdzZamowieniaNieZatwierdzoneDlaKlienta(kontoEndpoint.pobierzMojLogin())); // Wersja produkcyjna przy zastosowaniu poprawnego uwierzytelniania
    }

    @Interceptors(PerformanceInterceptor.class)
    public void zlozZamowienie(ZamowienieDTO zamowienieDTO) throws AppBaseException {

        //W przypadku tej operacji rola endpointa (konwersja DTO->encja) wymaga implementacji
        //logiki związanej z wyszukiwaniem obiektów Produkt
        Zamowienie zam = new Zamowienie();
        for (PozycjaZamowieniaDTO pozycjaZamowieniaDTO : zamowienieDTO.getPozycjeZamowienia()) {

            Produkt produkt = produktFacade.find(pozycjaZamowieniaDTO.getProdukt().getId());
            if (null == produkt) {
                throw ZamowienieException.createWithProductNotFound();
            }

            PozycjaZamowienia pozycjaZamowienia = new PozycjaZamowienia();
            pozycjaZamowienia.setProdukt(produkt);
            pozycjaZamowienia.setIlosc(pozycjaZamowieniaDTO.getIlosc());
            pozycjaZamowienia.setCena(pozycjaZamowieniaDTO.getCena());

            zam.getPozycjeZamowienia().add(pozycjaZamowienia);
        }

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                zamowienieManager.zlozZamowienie(zam);
                rollbackTX = zamowienieManager.isLastTransactionRollback();
            } catch (ZamowienieException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej zakończona wyjątkiem klasy: "
                        + ex.getClass().getName()
                        + " z komunikatem: " + ex.getMessage());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw ZamowienieException.createWithTxRetryRollback();
        }
    }

    public void zatwierdzZamowienie() throws AppBaseException {
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                zamowienieManager.zatwierdzZamowienie();
                rollbackTX = zamowienieManager.isLastTransactionRollback();
            } catch (ZamowienieException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej zakończona wyjątkiem klasy: "
                        + ex.getClass().getName()
                        + " z komunikatem: " + ex.getMessage());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw ZamowienieException.createWithTxRetryRollback();
        }
    }

    public void usunZamowienie(ZamowienieDTO zamowienieDTO) throws AppBaseException {
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                zamowienieManager.usunZamowienie(zamowienieDTO.getId());
                rollbackTX = zamowienieManager.isLastTransactionRollback();
            } catch (ZamowienieException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej zakończona wyjątkiem klasy: "
                        + ex.getClass().getName()
                        + " z komunikatem: " + ex.getMessage());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw ZamowienieException.createWithTxRetryRollback();
        }

    }

}
