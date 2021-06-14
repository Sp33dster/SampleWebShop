package pl.spjava.shop.web.zamowienie;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.ZamowienieDTO;

@Named("listaZamowienKlientPageBean")
@ViewScoped
public class ListaZamowienKlientPageBean implements Serializable {

    public ListaZamowienKlientPageBean() {
    }
    private boolean tylkoNieZatwiedzone = false;
    private List<ZamowienieDTO> zamowienia;

    @PostConstruct
    private void initModel() {
        if (tylkoNieZatwiedzone) {
            zamowienia = zamowienieController.pobierzMojeZamowieniaNieZatwierdzone();
        } else {
            zamowienia = zamowienieController.pobierzMojeZamowienia();
        }
    }
    @Inject
    private ZamowienieController zamowienieController;

    public List<ZamowienieDTO> getZamowienia() {
        return zamowienia;
    }

    public boolean isTylkoNieZatwiedzone() {
        return tylkoNieZatwiedzone;
    }

    public void setTylkoNieZatwiedzone(boolean tylkoNieZatwiedzone) {
        this.tylkoNieZatwiedzone = tylkoNieZatwiedzone;
    }

    public String usunWybraneZamowienie(ZamowienieDTO zamowienie) {
        return zamowienieController.pobierzZamowienieDoUsuniecia(zamowienie);
    }

    public void odswiez() {
        initModel();
    }
}
