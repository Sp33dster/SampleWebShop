package pl.spjava.shop.web.zamowienie;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.ZamowienieDTO;

@Named("zatwierdzZamowieniePageBean")
@RequestScoped
public class ZatwierdzZamowieniePageBean {

    public ZatwierdzZamowieniePageBean() {
    }

    @Inject
    private ZamowienieController zamowienieController;

    public ZamowienieDTO getZamowienie() {
        return zamowienieController.getZamowienieZmiana();
    }

    public String zatwierdzZamowienie() {
        return zamowienieController.zatwierdzPobraneZamowienie();
    }

}
