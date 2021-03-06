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
        // Je??eli konto klienta jest rejestrowane samodzielnie, to nie jest potwierdzone!
        klient.setPotwierdzone(false);
        klient.setNip(klientDTO.getNip());
        kontoFacade.create(klient);
    }

    // Poni??sze metody mo??na by zredukowa?? do jednej u??ywaj??c instanceOf().
    // Kwestia tego kt??re rozwi??zanie jest bardziej "clean code" pozostaje do dyskusji
    // Wariant metody przeci????onej jest wybierany na podstawie klasy argumentu
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void utworzKonto(KlientDTO klientDTO) throws AppBaseException {
        Klient klient = new Klient();
        // Edytowalne dane wsp??lne
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
                Logger.getGlobal().log(Level.SEVERE, "Pr??ba " + retryTXCounter
                        + " wykonania metody biznesowej zako??czona wyj??tkiem klasy:"
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
                Logger.getGlobal().log(Level.SEVERE, "Pr??ba " + retryTXCounter
                        + " wykonania metody biznesowej zako??czona wyj??tkiem klasy:"
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
                Logger.getGlobal().log(Level.SEVERE, "Pr??ba " + retryTXCounter
                        + " wykonania metody biznesowej zako??czona wyj??tkiem klasy:"
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
        return DTOConverter.tworzKontoDTOzEncji(kontoStan); //ta konwersja powinna odwzorowa?? hierarchi?? podklas Konta na analogiczn?? hierarchi?? DTO
    }

    // Mo??na by unika?? powtarzania tego kodu poprzez u??ycie instanceOF(). Jest kwesti?? dyskusyjn?? kt??re rozwi??zanie jest bardziej "clean code".
    public void zapiszKontoKlientaPoEdycji(KontoDTO klientDTO) throws AppBaseException {
        if (null == kontoStan) {
            throw new IllegalArgumentException("Brak wczytanego konta do modyfikacji");
        }
        // Przepisz te dane konta, kt??re podlegaj?? edycji (sa dostepne na formularzu)
        przepiszEdytowalneDaneKontaDTOdoEncji(klientDTO, kontoStan);
        // kontoStan oraz DTO musi by?? odpowiedniej podklasy Konto. Inaczej mamy b????d w konstrukcji aplikacji.
        ((Klient) kontoStan).setNip(((KlientDTO) klientDTO).getNip());
        //wykonej merge() na encji Konto, aby wesz??a ona w stan zarz??dzany przez obecny kontekst trwalo??ci
        kontoFacade.edit(kontoStan);
        //usu?? stan konta po zako??czonej operacji - unika b????dnego wielokrotnego wykonania
        kontoStan = null;
    }

    public void zapiszKontoPracownikaPoEdycji(KontoDTO pracownikDTO) throws AppBaseException {
        if (null == kontoStan) {
            throw new IllegalArgumentException("Brak wczytanego konta do modyfikacji");
        }
        // Przepisz te dane konta, kt??re podlegaj?? edycji (sa dostepne na formularzu)
        przepiszEdytowalneDaneKontaDTOdoEncji(pracownikDTO, kontoStan);
        // kontoStan oraz DTO musi by?? odpowiedniej podklasy Konto. Inaczej mamy b????d w konstrukcji aplikacji.
        ((Pracownik) kontoStan).setIntercom(((PracownikDTO) pracownikDTO).getIntercom());
        //wykonej merge() na encji Konto, aby wesz??a ona w stan zarz??dzany przez obecny kontekst trwalo??ci
        kontoFacade.edit(kontoStan);
        //usu?? stan konta po zako??czonej operacji - unika b????dnego wielokrotnego wykonania
        kontoStan = null;
    }

    public void zapiszKontoAdministratoraPoEdycji(KontoDTO administratorDTO) throws AppBaseException {
        if (null == kontoStan) {
            throw new IllegalArgumentException("Brak wczytanego konta do modyfikacji");
        }
        // Przepisz te dane konta, kt??re podlegaj?? edycji (sa dostepne na formularzu)
        przepiszEdytowalneDaneKontaDTOdoEncji(administratorDTO, kontoStan);
        // kontoStan oraz DTO musi by?? odpowiedniej podklasy Konto. Inaczej mamy b????d w konstrukcji aplikacji.
        ((Administrator) kontoStan).setAlarmNumber(((AdministratorDTO) administratorDTO).getAlarmNumber());
        //wykonej merge() na encji Konto, aby wesz??a ona w stan zarz??dzany przez obecny kontekst trwalo??ci
        kontoFacade.edit(kontoStan);
        //usu?? stan konta po zako??czonej operacji - unika b????dnego wielokrotnego wykonania
        kontoStan = null;
    }

    public void zmienMojeHaslo(String stare, String nowe) {
        Konto mojeKonto = pobierzMojeKonto();
        if (!mojeKonto.getHaslo().equals(KontoUtils.wyliczSkrotHasla(stare))) {
            throw new IllegalArgumentException("Podane dotychczasowe has??o nie zgadza si??");
        }
        mojeKonto.setHaslo(KontoUtils.wyliczSkrotHasla(nowe));
    }

    public void zmienHaslo(KontoDTO kontoDTO, String haslo) {
        Konto konto = kontoFacade.find(kontoDTO.getId());
        konto.setHaslo(KontoUtils.wyliczSkrotHasla(haslo));
    }

    //Metody narz??dziowe
    //Przepisuje do encji te dane konta, kt??re s?? na formularzu edycji
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

        // Je??eli konto jest tworzone przez administratora, jest od razu potwierdzone i aktywne
        konto.setAktywne(true);
        konto.setPotwierdzone(true);
        // Tworzone konto ma has??o jawne podane w formularzu, nale??y je przeliczy?? na skr??t
        konto.setHaslo(KontoUtils.wyliczSkrotHasla(kontoDTO.getHaslo()));
    }
}
