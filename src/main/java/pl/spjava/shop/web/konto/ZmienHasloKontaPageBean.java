package pl.spjava.shop.web.konto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.KontoDTO;
import pl.spjava.shop.web.utils.ContextUtils;

@Named("zmienHasloKontaPageBean")
@RequestScoped
public class ZmienHasloKontaPageBean {

    public ZmienHasloKontaPageBean() {
    }

    @Inject
    private KontoController kontoController;

    private KontoDTO konto;

    public KontoDTO getKonto() {
        return konto;
    }

    @PostConstruct
    private void init() {
        konto = kontoController.getKontoZmienHaslo();
        konto.setHaslo(new String());
    }

    private String hasloPowtorz = "";

    public String getHasloPowtorz() {
        return hasloPowtorz;
    }

    public void setHasloPowtorz(String hasloPowtorz) {
        this.hasloPowtorz = hasloPowtorz;
    }

    public String zmienHaslo() {
        if (!(hasloPowtorz.equals(konto.getHaslo()))) {
            ContextUtils.emitInternationalizedMessage("zmienHasloKontaForm:passwordRepeat", "passwords.not.matching");
            return null;
        }

        return kontoController.zmienHasloKonta(konto.getHaslo());
    }

}
