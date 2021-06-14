package pl.spjava.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

@TableGenerator(name = "PozZamIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "PozZam", initialValue = 100)
@Entity
@Table(name = "PozycjaZamowienia")
public class PozycjaZamowienia extends AbstractEntity implements Serializable {

    @Override
    protected Object getBusinessKey() {
        if (null != id) {
            return id; //do porównywania pozycji juz zapisanych w bazie
        } else {
            return produkt; //do porównywania pozycji nie zapisanych w bazie
        }
    }

    @Id
    @Column(name = "id", updatable = false) //NOT NULL i UNIQUE wynikają z ograniczenia kliucza głównego
    /*
     * Adnotacja wskazująca, że do generowania wartości tego pola zostanie
     * zastosowany generator tabelowy o wskazanej nazwie.
     */
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PozZamIdGen")
    private Long id;

    @Min(value = 1)
    @Column(name = "ilosc", nullable = false, updatable = false)
    private int ilosc;

    @Digits(integer = 6, fraction = 2)
    @DecimalMin(value = "0.01")
    @Column(name = "cena", precision = 8, scale = 2, nullable = false)
    private BigDecimal cena;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produkt_id", nullable = false, updatable = false)
    private Produkt produkt;

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    @Override
    public Long getId() {
        return id;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

}
