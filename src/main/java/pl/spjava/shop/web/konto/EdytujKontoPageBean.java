package pl.spjava.shop.web.konto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.KontoDTO;
import pl.spjava.shop.exception.AppBaseException;
import pl.spjava.shop.web.utils.KontoUtils;

@Named("edytujKontoPageBean")
@RequestScoped
public class EdytujKontoPageBean {

    public EdytujKontoPageBean() {
    }

    @Inject
    private KontoController kontoController;

    @PostConstruct
    private void init() {
        konto = kontoController.getKontoEdytuj();
    }

    private KontoDTO konto = new KontoDTO();

    public KontoDTO getKonto() {
        return konto;
    }

    public boolean isKlient() {
        return KontoUtils.isKlient(konto);
    }

    public boolean isPracownik() {
        return KontoUtils.isPracownik(konto);
    }

    public boolean isAdministrator() {
        return KontoUtils.isAdministrator(konto);
    }

    public String zapiszKontoKlienta() throws AppBaseException {
        return kontoController.zapiszKontoKlientaPoEdycji(konto);
    }

    public String zapiszKontoPracownika() throws AppBaseException {
        return kontoController.zapiszKontoPracownikaPoEdycji(konto);
    }

    public String zapiszKontoAdministratora() throws AppBaseException {
        return kontoController.zapiszKontoAdministratoraPoEdycji(konto);
    }

}
