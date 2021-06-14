package pl.spjava.shop.web.konto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.KontoDTO;
import pl.spjava.shop.web.utils.KontoUtils;

@Named("szczegolyMojegoKontaPageBean")
@RequestScoped
public class SzczegolyMojegoKontaPageBean {

    public SzczegolyMojegoKontaPageBean() {
    }

    @Inject
    private KontoController kontoController;

    @PostConstruct
    private void init() {
        konto = kontoController.pobierzMojeKonto();
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

}
