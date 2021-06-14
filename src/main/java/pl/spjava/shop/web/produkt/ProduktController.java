package pl.spjava.shop.web.produkt;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import pl.spjava.shop.dto.ProduktDTO;
import pl.spjava.shop.ejb.endpoints.ProduktEndpoint;

@SessionScoped
public class ProduktController implements Serializable {

    @Inject
    private ProduktEndpoint produktEndpoint;

    public List<ProduktDTO> znajdzBedaceNaStanie() {
        return produktEndpoint.znajdzBedaceNaStanie();
    }

}
