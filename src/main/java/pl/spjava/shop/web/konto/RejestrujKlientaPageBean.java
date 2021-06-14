package pl.spjava.shop.web.konto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.KlientDTO;
import pl.spjava.shop.web.utils.ContextUtils;

@Named("rejestrujKlientaPageBean")
@RequestScoped
public class RejestrujKlientaPageBean {

    public RejestrujKlientaPageBean() {
    }

    @Inject
    private KontoController kontoController;

    private KlientDTO konto = new KlientDTO();

    public KlientDTO getKonto() {
        return konto;
    }

    private String hasloPowtorz = "";

    public String getHasloPowtorz() {
        return hasloPowtorz;
    }

    public void setHasloPowtorz(String hasloPowtorz) {
        this.hasloPowtorz = hasloPowtorz;
    }

    public String potwierdzRejestracje() {
        if (!(hasloPowtorz.equals(konto.getHaslo()))) {
            ContextUtils.emitInternationalizedMessage("rejestrujKlientaForm:passwordRepeat", "passwords.not.matching");
            return null;
        }

        return kontoController.potwierdzRejestracjeKlienta(konto);
    }

}
