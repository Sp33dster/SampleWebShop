package pl.spjava.shop.web.konto;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.AdministratorDTO;
import pl.spjava.shop.dto.KlientDTO;
import pl.spjava.shop.dto.KontoDTO;
import pl.spjava.shop.dto.PracownikDTO;
import pl.spjava.shop.ejb.endpoints.KontoEndpoint;
import pl.spjava.shop.exception.AppBaseException;
import pl.spjava.shop.exception.KontoException;
import pl.spjava.shop.web.utils.ContextUtils;

@Named("kontoSession")
@SessionScoped
public class KontoController implements Serializable {

    private static final Logger LOG = Logger.getLogger(KontoController.class.getName());

    @Inject
    private KontoEndpoint kontoEndpoint;

    public String resetujSesje() {
        ContextUtils.invalidateSession();
        /* Poprawne zakończenie sesji wymaga wymuszenia nowego żądania na przeglądarce, stąd metoda ta
         * prowadzi do przypadku nawigacji z elementem <redirect />.
         * UWAGA: integracja logowania typu BASIC z przeglądarką powoduje, że czasem mimo to "wylogowanie" jest nieskuteczne - 
         * powstaje nowa sesja już zalogowanego użytkownika. Dlatego bezpieczniej jest stosować uwierzytelnianie przez formularz (FORM).
         */
        return "cancelAction";
    }

    public String getMojLogin() {
        return ContextUtils.getUserName();
    }

    private KlientDTO klientRejestracja;

    private KlientDTO klientUtworz;

    private PracownikDTO pracownikUtworz;

    private AdministratorDTO administratorUtworz;

    private KontoDTO kontoEdytuj;

    private KontoDTO kontoZmienHaslo;

    public KontoDTO getKontoZmienHaslo() {
        return kontoZmienHaslo;
    }

    public KontoDTO getKontoEdytuj() {
        return kontoEdytuj;
    }

    public KlientDTO getKlientRejestracja() {
        return klientRejestracja;
    }

    public KontoController() {
    }

    public String utworzKlienta(KlientDTO klient) {
        try {
            LOG.log(Level.INFO, "Zgłoszenie akcji utworzKlienta (" + ContextUtils.getUserAddress() + ")");

            klientUtworz = klient;
            kontoEndpoint.utworzKonto(klientUtworz);
            klientUtworz = null;
            return "success";
        } catch (KontoException ke) {
            if (KontoException.KEY_DB_CONSTRAINT.equals(ke.getMessage())) {
                ContextUtils.emitInternationalizedMessage("login", KontoException.KEY_DB_CONSTRAINT); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            } else {
                Logger.getLogger(KontoController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji utworzKlienta wyjatku: ", ke);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(KontoController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji utworzKlienta wyjatku typu: ", abe.getClass());
            if (ContextUtils.isInternationalizationKeyExist(abe.getMessage())) {
                ContextUtils.emitInternationalizedMessage(null, abe.getMessage()); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            }
            return null;
        }
    }

    public String utworzPracownika(PracownikDTO pracownik) {
        try {
            LOG.log(Level.INFO, "Zgłoszenie akcji utworzPracownika (" + ContextUtils.getUserAddress() + ")");

            pracownikUtworz = pracownik;
            kontoEndpoint.utworzKonto(pracownikUtworz);
            pracownikUtworz = null;
            return "success";
        } catch (KontoException ke) {
            if (KontoException.KEY_DB_CONSTRAINT.equals(ke.getMessage())) {
                ContextUtils.emitInternationalizedMessage(null, KontoException.KEY_DB_CONSTRAINT); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            } else {
                Logger.getLogger(KontoController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji utworzPracownika wyjatku: ", ke);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(KontoController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji utworzPracownika wyjatku typu: ", abe.getClass());
            if (ContextUtils.isInternationalizationKeyExist(abe.getMessage())) {
                ContextUtils.emitInternationalizedMessage(null, abe.getMessage()); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            }
            return null;
        }
    }

    public String utworzAdministratora(AdministratorDTO admin) {
        try {
            LOG.log(Level.INFO, "Zgłoszenie akcji utworzAdministratora (" + ContextUtils.getUserAddress() + ")");

            administratorUtworz = admin;
            kontoEndpoint.utworzKonto(administratorUtworz);
            administratorUtworz = null;
            return "success";
        } catch (KontoException ke) {
            if (KontoException.KEY_DB_CONSTRAINT.equals(ke.getMessage())) {
                ContextUtils.emitInternationalizedMessage("login", KontoException.KEY_DB_CONSTRAINT); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            } else {
                Logger.getLogger(KontoController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji utworzAdministratora wyjatku: ", ke);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(KontoController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji utworzAdministratora wyjatku typu: ", abe.getClass());
            if (ContextUtils.isInternationalizationKeyExist(abe.getMessage())) {
                ContextUtils.emitInternationalizedMessage(null, abe.getMessage()); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            }
            return null;
        }
    }

    public String potwierdzRejestracjeKlienta(KlientDTO klient) {
        this.klientRejestracja = klient;
        return "confirmRegister";
    }

    public String rozpocznijZmianeHasla(KontoDTO konto) {
        this.kontoZmienHaslo = konto;
        return "changePassword";
    }

    public String rejestrujKlienta() {
        try {
            LOG.log(Level.INFO, "Zgłoszenie akcji rejestrujKlienta (" + ContextUtils.getUserAddress() + ")");
            kontoEndpoint.rejestrujKlienta(klientRejestracja);
            klientRejestracja = null;
            return "success";
        } catch (KontoException ke) {
            if (KontoException.KEY_DB_CONSTRAINT.equals(ke.getMessage())) {
                ContextUtils.emitInternationalizedMessage("login", KontoException.KEY_DB_CONSTRAINT); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            } else {
                Logger.getLogger(KontoController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji rejestrujKlienta wyjatku: ", ke);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(KontoController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji rejestrujKlienta wyjatku typu: ", abe.getClass());
            if (ContextUtils.isInternationalizationKeyExist(abe.getMessage())) {
                ContextUtils.emitInternationalizedMessage(null, abe.getMessage()); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            }
            return null;
        }
    }

    public void aktywujKonto(KontoDTO konto) {
        kontoEndpoint.aktywujKonto(konto);
        ContextUtils.emitSuccessMessage(ListaKontPageBean.GENERAL_MSG_ID);
    }

    public void deaktywujKonto(KontoDTO konto) {
        kontoEndpoint.deaktywujKonto(konto);
        ContextUtils.emitSuccessMessage(ListaKontPageBean.GENERAL_MSG_ID);
    }

    public void potwierdzKonto(KontoDTO konto) {
        kontoEndpoint.potwierdzKonto(konto);
        ContextUtils.emitSuccessMessage(ListaKontPageBean.GENERAL_MSG_ID);
    }

    public String pobierzKontoDoEdycji(KontoDTO konto) {
        kontoEdytuj = kontoEndpoint.pobierzKontoDoEdycji(konto);
        return "editAccount";
    }

    public String zapiszKontoKlientaPoEdycji(KontoDTO konto) throws AppBaseException {
        kontoEndpoint.zapiszKontoKlientaPoEdycji(konto);
        return "success";
    }

    public String zapiszKontoPracownikaPoEdycji(KontoDTO konto) throws AppBaseException {
        kontoEndpoint.zapiszKontoPracownikaPoEdycji(konto);
        return "success";
    }

    public String zapiszKontoAdministratoraPoEdycji(KontoDTO konto) throws AppBaseException {
        kontoEndpoint.zapiszKontoAdministratoraPoEdycji(konto);
        return "success";
    }

    public String zmienHasloKonta(String haslo) {
        kontoEndpoint.zmienHaslo(kontoZmienHaslo, haslo);
        return "success";
    }

    public String zmienMojeHaslo(String stare, String nowe) {
        kontoEndpoint.zmienMojeHaslo(stare, nowe);
        return "success";
    }

    public List<KontoDTO> pobierzWszystkieKonta() {
        return kontoEndpoint.pobierzWszystkieKonta();
    }

    public List<KontoDTO> dopasujKonta(String loginWzor, String imieWzor, String nazwiskoWzor, String emailWzor) {
        return kontoEndpoint.dopasujKonta(loginWzor, imieWzor, nazwiskoWzor, emailWzor);
    }

    public KontoDTO pobierzMojeKonto() {
        return kontoEndpoint.pobierzMojeKontoDTO();
    }

    @PostConstruct
    private void init() {
        LOG.severe("Session started: " + ContextUtils.getSessionID());
    }
}
