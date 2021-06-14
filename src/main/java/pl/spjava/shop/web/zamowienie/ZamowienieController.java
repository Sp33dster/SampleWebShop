package pl.spjava.shop.web.zamowienie;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.PozycjaZamowieniaDTO;
import pl.spjava.shop.dto.ProduktDTO;
import pl.spjava.shop.dto.ZamowienieDTO;
import pl.spjava.shop.ejb.endpoints.ZamowienieEndpoint;
import pl.spjava.shop.exception.AppBaseException;
import pl.spjava.shop.web.utils.ContextUtils;

@Named("zamowienieController")
@SessionScoped
public class ZamowienieController implements Serializable {

    private static final String LISTA_PRODUKTOW_KLIENT_MSG_ID = "listaProduktowKlientForm:listaProduktow";
    private static final Logger loger = Logger.getLogger(ZamowienieController.class.getSimpleName());

    @Inject
    private ZamowienieEndpoint zamowienieEndpoint;

    private ZamowienieDTO zamowienieZmiana;
    private ZamowienieDTO zamowienieSkladanie;

    public void resetujSkladaneZamowienie() {
        zamowienieSkladanie = new ZamowienieDTO();
    }

    public void dodajPozycjeDoSkladanegoZamowienia(ProduktDTO produkt) {
        // Zapewnienie unikatowości produktów na liście zamówienia
        // Gwarantuje to równiez baza danych, ale chcemy uniknąć tego problemu jak najwcześniej

        for (PozycjaZamowieniaDTO poz : zamowienieSkladanie.getPozycjeZamowienia()) {
            if (poz.getProdukt().equals(produkt)) { //dlatego ProduktDTO musi implementować equals()
                poz.setIlosc(poz.getIlosc() + 1);
                ContextUtils.emitInternationalizedMessage(LISTA_PRODUKTOW_KLIENT_MSG_ID, "product.increased.in.order");
                return;
            }
        }
        PozycjaZamowieniaDTO poz = new PozycjaZamowieniaDTO();
        poz.setProdukt(produkt);
        poz.setIlosc(1);
        poz.setCena(produkt.getCena());
        zamowienieSkladanie.getPozycjeZamowienia().add(poz);
        ContextUtils.emitInternationalizedMessage(LISTA_PRODUKTOW_KLIENT_MSG_ID, ("product.added.to.order"));
    }

    public boolean isZamowienieSkladanieNiepuste() {
        return (zamowienieSkladanie.getLiczbaPozycji() > 0);

    }

    public ZamowienieDTO getZamowienieSkladanie() {
        return zamowienieSkladanie;
    }

    public ZamowienieDTO getZamowienieZmiana() {
        return zamowienieZmiana;
    }

    public ZamowienieController() {
        zamowienieSkladanie = new ZamowienieDTO();
    }

    public String pobierzZamowienieDoZatwierdzenia(ZamowienieDTO zamowienie) {
        try {
            loger.log(Level.INFO, "Zgłoszenie akcji pobierzZamowienieDoZatwierdzenia (" + ContextUtils.getUserAddress() + ")");

            this.zamowienieZmiana = zamowienieEndpoint.pobierzZamowienie(zamowienie);
            return "confirmOrder";
        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ZamowienieController.class.getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji wyjatku: ", ex);

            ContextUtils.emitInternationalizedMessageOfException(ex);
            return null;
        }

    }

    public String pobierzZamowienieDoUsuniecia(ZamowienieDTO zamowienie) {
        try {
            loger.log(Level.INFO, "Zgłoszenie akcji pobierzZamowienieDoUsuniecia (" + ContextUtils.getUserAddress() + ")");

            this.zamowienieZmiana = zamowienieEndpoint.pobierzZamowienie(zamowienie);
            return "deleteOrder";

        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ZamowienieController.class.getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji wyjatku: ", ex);

            ContextUtils.emitInternationalizedMessageOfException(ex);
            return null;
        }
    }

    public String zatwierdzPobraneZamowienie() {
        try {
            loger.log(Level.INFO, "Zgłoszenie akcji zatwierdzPobraneZamowienie (" + ContextUtils.getUserAddress() + ")");

            zamowienieEndpoint.zatwierdzZamowienie();
            return "success";
        } catch (AppBaseException abe) {
            Logger.getLogger(ZamowienieController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji zatwierdzPobraneZamowienie wyjatku typu: ", abe.getClass());
            if (ContextUtils.isInternationalizationKeyExist(abe.getMessage())) {
                ContextUtils.emitInternationalizedMessage(null, abe.getMessage()); //wyjątki aplikacyjne powinny przenosić jedynie klucz do internacjonalizacji
            }
            return null;
        }
    }

    public String zlozZamowienie() {
        try {
            loger.log(Level.INFO, "Zgłoszenie akcji zlozZamowienie (" + ContextUtils.getUserAddress() + ")");

            zamowienieEndpoint.zlozZamowienie(zamowienieSkladanie);
            resetujSkladaneZamowienie();
            return "success";
        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ZamowienieController.class
                    .getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji wyjatku: ", ex);

            ContextUtils.emitInternationalizedMessageOfException(ex);
            return null;
        }
    }

    public String usunPobraneZamowienie() {
        try {
            loger.log(Level.INFO, "Zgłoszenie akcji usunPobraneZamowienie (" + ContextUtils.getUserAddress() + ")");

            zamowienieEndpoint.usunZamowienie(zamowienieZmiana);
            return "success";
        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ZamowienieController.class
                    .getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji wyjatku: ", ex);

            ContextUtils.emitInternationalizedMessageOfException(ex);
            return null;
        }
    }

    public List<ZamowienieDTO> pobierzWszystkieZamowienia() {
        return zamowienieEndpoint.pobierzWszystkieZamowienia();
    }

    public List<ZamowienieDTO> pobierzZamowieniaNieZatwierdzone() {
        return zamowienieEndpoint.pobierzZamowieniaNieZatwierdzone();
    }

    public List<ZamowienieDTO> pobierzMojeZamowienia() {
        return zamowienieEndpoint.pobierzMojeZamowienia();
    }

    public List<ZamowienieDTO> pobierzMojeZamowieniaNieZatwierdzone() {
        return zamowienieEndpoint.pobierzMojeZamowieniaNieZatwierdzone();
    }
}
