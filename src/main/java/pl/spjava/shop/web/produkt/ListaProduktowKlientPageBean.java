package pl.spjava.shop.web.produkt;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.ProduktDTO;
import pl.spjava.shop.web.zamowienie.ZamowienieController;

@Named("listaProduktowKlientPageBean")
@ViewScoped
public class ListaProduktowKlientPageBean implements Serializable {

    public ListaProduktowKlientPageBean() {
    }

    @PostConstruct
    private void initModel() {
        produkty = produktController.znajdzBedaceNaStanie();
    }
    @Inject
    private ProduktController produktController;

    @Inject
    private ZamowienieController zamowienieController;

    private List<ProduktDTO> produkty;

    public List<ProduktDTO> getProdukty() {
        return produkty;
    }

    public void dodajDoZamowienia(ProduktDTO produkt) {
        zamowienieController.dodajPozycjeDoSkladanegoZamowienia(produkt);
    }

}
