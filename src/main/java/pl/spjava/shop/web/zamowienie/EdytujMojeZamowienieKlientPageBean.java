package pl.spjava.shop.web.zamowienie;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.PozycjaZamowieniaDTO;
import pl.spjava.shop.dto.ZamowienieDTO;

@Named("edytujMojeZamowienieKlientPageBean")
@RequestScoped
public class EdytujMojeZamowienieKlientPageBean {

    public EdytujMojeZamowienieKlientPageBean() {
    }

    @Inject
    private ZamowienieController zamowienieController;

    public ZamowienieDTO getZamowienie() {
        return zamowienieController.getZamowienieSkladanie();
    }

    public void przeliczZamowienie() {
        //Uzyskanie kopii listy pozycji zamowienia - ponieważ lista oryginalna może ulec zmianie
        for (PozycjaZamowieniaDTO poz : getZamowienie().getPozycjeZamowienia()) {
            if (0 >= poz.getIlosc()) {
                getZamowienie().getPozycjeZamowienia().remove(poz);
            }
        }
    }

    public void resetujZamowienie() {
        zamowienieController.resetujSkladaneZamowienie();
    }

    public String potwierdzSkladaneZamowienie() {
        return "createClientOrder";
    }

}
