package pl.spjava.shop.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@TableGenerator(name = "KategoriaIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "Kategoria", initialValue = 100)
@Entity
@Table(name = "Kategoria")
public class Kategoria extends AbstractEntity implements Serializable {

    @Override
    protected Object getBusinessKey() {
        return nazwa;
    }

    @Id
    @Column(name = "id", updatable = false) //NOT NULL i UNIQUE wynmikają z ograniczenia kliucza głównego
    /*
     * Adnotacja wskazująca, że do generowania wartości tego pola zostanie
     * zastosowany generator tabelowy o wskazanej nazwie.
     */
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "KategoriaIdGen")
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @NotNull
    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    @Column(name = "nazwa", length = 32, nullable = false, unique = true, updatable = false)
    private String nazwa;

    @NotNull
    @Size(max = 200, message = "{constraint.string.length.toolong}")
    @Column(name = "opis", length = 200)
    private String opis;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

}
