package pl.spjava.shop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "DaneKlient")

/* Adnotacja @DiscriminatorValue określa, jaka wartość znajdzie się w kolumnie oznaczonej @DiscriminatorColumn
 * w tabeli reprezentującej nadrzędną klasę encyjną.
 */
@DiscriminatorValue("KLIENT")
@NamedQueries({
    @NamedQuery(name = "Klient.findAll", query = "SELECT d FROM Klient d"),
    @NamedQuery(name = "Klient.findByLogin", query = "SELECT d FROM Klient d WHERE d.login = :login")
})
public class Klient extends Konto implements Serializable {

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$", message = "{constraint.string.incorrectNIP}")
    @Column(name = "nip", unique = true, nullable = false, length = 10)
    private String nip;

    @OneToMany(mappedBy = "ktoZlozyl")
    private List<Zamowienie> zamowienia = new ArrayList<Zamowienie>();

    public List<Zamowienie> getZamowienia() {
        return zamowienia;
    }

    public Klient() {
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }
}
