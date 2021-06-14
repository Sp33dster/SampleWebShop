package pl.spjava.shop.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "DanePracownik")
/* Adnotacja @DiscriminatorValue określa, jaka wartość znajdzie się w kolumnie oznaczonej @DiscriminatorColumn
 * w tabeli reprezentującej nadrzędną klasę encyjną.
 */
@DiscriminatorValue("PRACOWNIK")
@NamedQueries({
    @NamedQuery(name = "Pracownik.findAll", query = "SELECT d FROM Pracownik d"),
    @NamedQuery(name = "Pracownik.findByIntercom", query = "SELECT d FROM Pracownik d WHERE d.intercom = :intercom")
})
public class Pracownik extends Konto implements Serializable {

    @NotNull
    @Size(max = 12, message = "{constraint.string.length.toolong}")
    @Column(name = "intercom", unique = true, nullable = false, length = 12)
    private String intercom;

    public Pracownik() {
    }

    public String getIntercom() {
        return intercom;
    }

    public void setIntercom(String intercom) {
        this.intercom = intercom;
    }
}
