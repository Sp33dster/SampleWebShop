package pl.spjava.shop.web.zamowienie;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.spjava.shop.dto.ZamowienieDTO;

@Named("usunZamowieniePageBean")
@RequestScoped
public class UsunZamowieniePageBean {

    public UsunZamowieniePageBean() {
    }

    @Inject
    private ZamowienieController zamowienieController;

    public ZamowienieDTO getZamowienie() {
        return zamowienieController.getZamowienieZmiana();
    }

    public String usunZamowienie() {
        return zamowienieController.usunPobraneZamowienie();
    }

}
