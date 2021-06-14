package pl.spjava.shop.ejb.endpoints;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.spjava.shop.dto.AdministratorDTO;
import pl.spjava.shop.dto.KlientDTO;
import pl.spjava.shop.dto.KontoDTO;
import pl.spjava.shop.dto.PracownikDTO;
import pl.spjava.shop.ejb.facades.AdministratorFacade;
import pl.spjava.shop.ejb.facades.KlientFacade;
import pl.spjava.shop.ejb.facades.KontoFacade;
import pl.spjava.shop.ejb.facades.PracownikFacade;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.ejb.managers.KontoManager;
import pl.spjava.shop.exception.AppBaseException;
import pl.spjava.shop.exception.KontoException;
import pl.spjava.shop.model.Administrator;
import pl.spjava.shop.model.Klient;
import pl.spjava.shop.model.Konto;
import pl.spjava.shop.model.Pracownik;
import pl.spjava.shop.utils.DTOConverter;
import pl.spjava.shop.web.utils.KontoUtils;

@Stateful
@LocalBean
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class KontoEndpoint {

    @Inject
    private KontoFacade kontoFacade;

    @Inject
    private KontoManager kontoManager;

    @Inject
    private KlientFacade klientFacade;

    @Inject
    private PracownikFacade pracownikFacade;

    @Inject
    private AdministratorFacade administratorFacade;

    @Resource
    protected SessionContext sctx;

    @Resource(name = "txRetryLimit")
    private int txRetryLimit;

    private Konto kontoStan;

    public KontoDTO pobierzMojeKontoDTO() {
        return DTOConverter.tworzKontoDTOzEncji(pobierzMojeKonto());
    }

    public Konto pobierzMojeKonto() {
        return kontoFacade.znajdzLogin(pobierzMojLogin());
    }

    public Klient pobierzMojeKontoKlienta() {
        return kontoFacade.znajdzLoginJakoKlienta(pobierzMojLogin());
    }

    public Pracownik pobierzMojeKontoPracownika() {
        return kontoFacade.znajdzLoginJakoPracownika(pobierzMojLogin());
    }

    public String pobierzMojLogin() throws IllegalStateException {
        return sctx.getCallerPrincipal().getName();
    }

    public void rejestrujKlienta(KlientDTO klientDTO) throws AppBaseException {
        Klient klient = new Klient();
        przepiszDaneDoNowegoKonta(klientDTO, klient);
        // Jeżeli konto klienta jest rejestrowane samodzielnie, to nie jest potwierdzone!
        klient.setPotwierdzone(false);
        klient.setNip(klientDTO.getNip());
        kontoFacade.create(klient);
    }

    // Poniższe metody można by zredukować do jednej używając instanceOf().
    // Kwestia tego które rozwiązanie jest bardziej "clean code" pozostaje do dyskusji
    // Wariant metody przeciążonej jest wybierany na podstawie klasy argumentu
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void utworzKonto(KlientDTO klientDTO) throws AppBaseException {
        Klient klient = new Klient();
        // Edytowalne dane wspólne
        przepiszDaneDoNowegoKonta(klientDTO, klient);
        // Dane klienta
        klient.setNip(klientDTO.getNip());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                kontoManager.utworzKonto(klient);
                rollbackTX = kontoManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej zakończona wyjątkiem klasy:"
                        + ex.getClass().getName()
                        + " z komunikatem:" + ex.getMessage());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw KontoException.createTxRetryRollback();
        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void utworzKonto(PracownikDTO pracownikDTO) throws AppBaseException {
        Pracownik pracownik = new Pracownik();
        przepiszDaneDoNowegoKonta(pracownikDTO, pracownik);
        // Dane pracownika
        pracownik.setIntercom(pracownikDTO.getIntercom());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                kontoManager.utworzKonto(pracownik);
                rollbackTX = kontoManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej zakończona wyjątkiem klasy:"
                        + ex.getClass().getName()
                        + " z komunikatem:" + ex.getMessage());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw KontoException.createTxRetryRollback();
        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void utworzKonto(AdministratorDTO administratorDTO) throws AppBaseException {
        Administrator administrator = new Administrator();
        przepiszDaneDoNowegoKonta(administratorDTO, administrator);
        // Dane administratora
        administrator.setAlarmNumber(administratorDTO.getAlarmNumber());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                kontoManager.utworzKonto(administrator);
                rollbackTX = kontoManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej zakończona wyjątkiem klasy:"
                        + ex.getClass().getName()
                        + " z komunikatem:" + ex.getMessage());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw KontoException.createTxRetryRollback();
        }
    }

    public void aktywujKonto(KontoDTO kontoDTO) {
        Konto konto = kontoFacade.find(kontoDTO.getId());
        konto.setAktywne(true);
    }

    public void deaktywujKonto(KontoDTO kontoDTO) {
        Konto konto = kontoFacade.find(kontoDTO.getId());
        konto.setAktywne(false);
    }

    public void potwierdzKonto(KontoDTO kontoDTO) {
        Konto konto = kontoFacade.find(kontoDTO.getId());
        konto.setPotwierdzone(true);
    }

    public List<KontoDTO> pobierzWszystkieKonta() {
        return DTOConverter.tworzListeKontoDTOzEncji(kontoFacade.findAll());
    }

    public List<KontoDTO> dopasujKonta(String loginWzor, String imieWzor, String nazwiskoWzor, String emailWzor) {
        return DTOConverter.tworzListeKontoDTOzEncji(kontoFacade.dopasujKonta(loginWzor, imieWzor, nazwiskoWzor, emailWzor));
    }

    public KontoDTO pobierzKontoDoEdycji(KontoDTO konto) {
        kontoStan = kontoFacade.znajdzLogin(konto.getLogin());
        return DTOConverter.tworzKontoDTOzEncji(kontoStan); //ta konwersja powinna odwzorować hierarchię podklas Konta na analogiczną hierarchię DTO
    }

    // Można by unikać powtarzania tego kodu poprzez użycie instanceOF(). Jest kwestią dyskusyjną które rozwiązanie jest bardziej "clean code".
    public void zapiszKontoKlientaPoEdycji(KontoDTO klientDTO) throws AppBaseException {
        if (null == kontoStan) {
            throw new IllegalArgumentException("Brak wczytanego konta do modyfikacji");
        }
        // Przepisz te dane konta, które podlegają edycji (sa dostepne na formularzu)
        przepiszEdytowalneDaneKontaDTOdoEncji(klientDTO, kontoStan);
        // kontoStan oraz DTO musi być odpowiedniej podklasy Konto. Inaczej mamy błąd w konstrukcji aplikacji.
        ((Klient) kontoStan).setNip(((KlientDTO) klientDTO).getNip());
        //wykonej merge() na encji Konto, aby weszła ona w stan zarządzany przez obecny kontekst trwalości
        kontoFacade.edit(kontoStan);
        //usuń stan konta po zakończonej operacji - unika błędnego wielokrotnego wykonania
        kontoStan = null;
    }

    public void zapiszKontoPracownikaPoEdycji(KontoDTO pracownikDTO) throws AppBaseException {
        if (null == kontoStan) {
            throw new IllegalArgumentException("Brak wczytanego konta do modyfikacji");
        }
        // Przepisz te dane konta, które podlegają edycji (sa dostepne na formularzu)
        przepiszEdytowalneDaneKontaDTOdoEncji(pracownikDTO, kontoStan);
        // kontoStan oraz DTO musi być odpowiedniej podklasy Konto. Inaczej mamy błąd w konstrukcji aplikacji.
        ((Pracownik) kontoStan).setIntercom(((PracownikDTO) pracownikDTO).getIntercom());
        //wykonej merge() na encji Konto, aby weszła ona w stan zarządzany przez obecny kontekst trwalości
        kontoFacade.edit(kontoStan);
        //usuń stan konta po zakończonej operacji - unika błędnego wielokrotnego wykonania
        kontoStan = null;
    }

    public void zapiszKontoAdministratoraPoEdycji(KontoDTO administratorDTO) throws AppBaseException {
        if (null == kontoStan) {
            throw new IllegalArgumentException("Brak wczytanego konta do modyfikacji");
        }
        // Przepisz te dane konta, które podlegają edycji (sa dostepne na formularzu)
        przepiszEdytowalneDaneKontaDTOdoEncji(administratorDTO, kontoStan);
        // kontoStan oraz DTO musi być odpowiedniej podklasy Konto. Inaczej mamy błąd w konstrukcji aplikacji.
        ((Administrator) kontoStan).setAlarmNumber(((AdministratorDTO) administratorDTO).getAlarmNumber());
        //wykonej merge() na encji Konto, aby weszła ona w stan zarządzany przez obecny kontekst trwalości
        kontoFacade.edit(kontoStan);
        //usuń stan konta po zakończonej operacji - unika błędnego wielokrotnego wykonania
        kontoStan = null;
    }

    public void zmienMojeHaslo(String stare, String nowe) {
        Konto mojeKonto = pobierzMojeKonto();
        if (!mojeKonto.getHaslo().equals(KontoUtils.wyliczSkrotHasla(stare))) {
            throw new IllegalArgumentException("Podane dotychczasowe hasło nie zgadza się");
        }
        mojeKonto.setHaslo(KontoUtils.wyliczSkrotHasla(nowe));
    }

    public void zmienHaslo(KontoDTO kontoDTO, String haslo) {
        Konto konto = kontoFacade.find(kontoDTO.getId());
        konto.setHaslo(KontoUtils.wyliczSkrotHasla(haslo));
    }

    //Metody narzędziowe
    //Przepisuje do encji te dane konta, które są na formularzu edycji
    private static void przepiszEdytowalneDaneKontaDTOdoEncji(KontoDTO kontoDTO, Konto konto) {
        konto.setImie(kontoDTO.getImie());
        konto.setNazwisko(kontoDTO.getNazwisko());
        konto.setTelefon(kontoDTO.getTelefon());
        konto.setEmail(kontoDTO.getEmail());
    }

    private static void przepiszDaneDoNowegoKonta(KontoDTO kontoDTO, Konto konto) {
        //login jest ustawiany tylko przy tworzeniu konta!
        konto.setLogin(kontoDTO.getLogin());

        przepiszEdytowalneDaneKontaDTOdoEncji(kontoDTO, konto);

        // Jeżeli konto jest tworzone przez administratora, jest od razu potwierdzone i aktywne
        konto.setAktywne(true);
        konto.setPotwierdzone(true);
        // Tworzone konto ma hasło jawne podane w formularzu, należy je przeliczyć na skrót
        konto.setHaslo(KontoUtils.wyliczSkrotHasla(kontoDTO.getHaslo()));
    }
}
