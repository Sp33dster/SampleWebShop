package pl.spjava.shop.web.konto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.AdministratorDTO;
import pl.spjava.shop.web.utils.ContextUtils;

@Named("utworzAdministratoraPageBean")
@RequestScoped
public class UtworzAdministratoraPageBean {

    public UtworzAdministratoraPageBean() {
    }

    @Inject
    private KontoController kontoController;

    private AdministratorDTO konto = new AdministratorDTO();

    public AdministratorDTO getKonto() {
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
            ContextUtils.emitInternationalizedMessage("utworzAdministratoraForm:passwordRepeat", "passwords.not.matching");
            return null;
        }

        return kontoController.utworzAdministratora(konto);
    }

}
