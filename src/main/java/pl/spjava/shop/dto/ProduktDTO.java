package pl.spjava.shop.dto;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProduktDTO {

    private Long id;

    @NotNull
    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    private String nazwa;
    
    @NotNull
    @Size(max=200,message="{constraint.string.length.toolong}")
    private String opis;

    @Digits(integer=6, fraction=2)
    @DecimalMin(value="0.01")
    private BigDecimal cena;
    
    @Min(value=0)
    private int stan = 0;

    public ProduktDTO(Long id, String nazwa, String opis, BigDecimal cena, int stan) {
        this.id = id;
        this.nazwa = nazwa;
        this.opis = opis;
        this.cena = cena;
        this.stan = stan;
    }

    public ProduktDTO() {
    }
    
    /* Implementacja equals() w ProduktDTO jest niezbędna do obsługi akcji dodania produktu do zamówienia.
    Jeżeli produkt istnieje już na liście zamówienia, zwiększana jest ilość zamiast ponownego dodania do listy.
    */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProduktDTO other = (ProduktDTO) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

    public Long getId() {
        return id;
    }

    public int getStan() {
        return stan;
    }

    public void setStan(int stan) {
        this.stan = stan;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

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
