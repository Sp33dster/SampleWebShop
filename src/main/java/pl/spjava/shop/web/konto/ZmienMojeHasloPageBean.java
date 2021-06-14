package pl.spjava.shop.web.konto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.KontoDTO;
import pl.spjava.shop.web.utils.ContextUtils;

@Named("zmienMojeHasloPageBean")
@RequestScoped
public class ZmienMojeHasloPageBean {

    public ZmienMojeHasloPageBean() {
    }

    @Inject
    private KontoController kontoController;

    private KontoDTO konto = new KontoDTO();

    public KontoDTO getKonto() {
        return konto;
    }

    private String hasloPowtorz = "";

    public String getHasloPowtorz() {
        return hasloPowtorz;
    }

    public void setHasloPowtorz(String hasloPowtorz) {
        this.hasloPowtorz = hasloPowtorz;
    }

    private String stareHaslo = "";

    public String getStareHaslo() {
        return stareHaslo;
    }

    public void setStareHaslo(String stareHaslo) {
        this.stareHaslo = stareHaslo;
    }

    public String zmienHaslo() {
        if (!(hasloPowtorz.equals(konto.getHaslo()))) {
            ContextUtils.emitInternationalizedMessage("zmienMojeHasloForm:passwordRepeat", "passwords.not.matching");
            return null;
        }

        return kontoController.zmienMojeHaslo(stareHaslo, konto.getHaslo());
    }

}
