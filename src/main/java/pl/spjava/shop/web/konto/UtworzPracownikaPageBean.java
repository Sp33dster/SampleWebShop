package pl.spjava.shop.web.konto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.PracownikDTO;
import pl.spjava.shop.web.utils.ContextUtils;

@Named("utworzPracownikaPageBean")
@RequestScoped
public class UtworzPracownikaPageBean {

    public UtworzPracownikaPageBean() {
    }

    @Inject
    private KontoController kontoController;

    private PracownikDTO konto = new PracownikDTO();

    public PracownikDTO getKonto() {
        return konto;
    }

    private String hasloPowtorz = "";

    public String getHasloPowtorz() {
        return hasloPowtorz;
    }

    public void setHasloPowtorz(String hasloPowtorz) {
        this.hasloPowtorz = hasloPowtorz;
    }

    public String utworz() {
        if (!(hasloPowtorz.equals(konto.getHaslo()))) {
            ContextUtils.emitInternationalizedMessage("utworzPracownikaForm:passwordRepeat", "passwords.not.matching");
            return null;
        }

        return kontoController.utworzPracownika(konto);
    }

}
