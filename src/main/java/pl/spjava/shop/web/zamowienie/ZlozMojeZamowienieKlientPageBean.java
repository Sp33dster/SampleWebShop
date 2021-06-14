package pl.spjava.shop.web.zamowienie;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.ZamowienieDTO;

@Named("zlozMojeZamowienieKlientPageBean")
@RequestScoped
public class ZlozMojeZamowienieKlientPageBean {

    public ZlozMojeZamowienieKlientPageBean() {
    }

    @Inject
    private ZamowienieController zamowienieController;

    public ZamowienieDTO getZamowienie() {
        return zamowienieController.getZamowienieSkladanie();
    }

    public String zlozZamowienie() {
        return zamowienieController.zlozZamowienie();
    }

}
