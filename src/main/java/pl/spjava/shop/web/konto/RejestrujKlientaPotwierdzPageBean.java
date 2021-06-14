package pl.spjava.shop.web.konto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.KlientDTO;

@Named("rejestrujKlientaPotwierdzPageBean")
@RequestScoped
public class RejestrujKlientaPotwierdzPageBean {

    public RejestrujKlientaPotwierdzPageBean() {
    }

    @Inject
    private KontoController kontoController;

    @PostConstruct
    private void initBean() {
        konto = kontoController.getKlientRejestracja();
    }

    private KlientDTO konto;

    public KlientDTO getKonto() {
        return konto;
    }

    public String rejestruj() {
        return kontoController.rejestrujKlienta();
    }

}
