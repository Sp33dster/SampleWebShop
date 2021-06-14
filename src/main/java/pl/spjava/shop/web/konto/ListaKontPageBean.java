package pl.spjava.shop.web.konto;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.KontoDTO;

@ViewScoped
@Named("listaKontPageBean")
public class ListaKontPageBean implements Serializable {

    static final String GENERAL_MSG_ID = "listaKontForm:listaKont";

    public ListaKontPageBean() {
    }

    @Inject
    private KontoController kontoController;

    @PostConstruct
    private void initModel() {
        konta = kontoController.dopasujKonta(szukanyLogin, szukaneImie, szukaneNazwisko, szukanyEmail);
    }

    private List<KontoDTO> konta;

    public List<KontoDTO> getKonta() {
        return konta;
    }

    private String szukanyLogin = "";
    private String szukaneImie = "";
    private String szukaneNazwisko = "";
    private String szukanyEmail = "";

    public String getSzukanyEmail() {
        return szukanyEmail;
    }

    public void setSzukanyEmail(String szukanyEmail) {
        this.szukanyEmail = szukanyEmail;
    }

    public String getSzukaneImie() {
        return szukaneImie;
    }

    public void setSzukaneImie(String szukaneImie) {
        this.szukaneImie = szukaneImie;
    }

    public String getSzukaneNazwisko() {
        return szukaneNazwisko;
    }

    public void setSzukaneNazwisko(String szukaneNazwisko) {
        this.szukaneNazwisko = szukaneNazwisko;
    }

    public String getSzukanyLogin() {
        return szukanyLogin;
    }

    public void setSzukanyLogin(String szukanyLogin) {
        this.szukanyLogin = szukanyLogin;
    }

    public void odswiez() {
        initModel();
    }

    public void wyczysc() {
        szukanyLogin = "";
        szukaneImie = "";
        szukaneNazwisko = "";
        szukanyEmail = "";
        initModel();
    }

    public void aktywujKonto(KontoDTO konto) {
        kontoController.aktywujKonto(konto);
        initModel();
    }

    public void deaktywujKonto(KontoDTO konto) {
        kontoController.deaktywujKonto(konto);
        initModel();
    }

    public void potwierdzKonto(KontoDTO konto) {
        kontoController.potwierdzKonto(konto);
        initModel();
    }

    public String edytujKonto(KontoDTO konto) {
        return kontoController.pobierzKontoDoEdycji(konto);
    }

    public String rozpocznijZmianeHasla(KontoDTO konto) {
        return kontoController.rozpocznijZmianeHasla(konto);
    }
}
