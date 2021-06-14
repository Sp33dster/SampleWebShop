package pl.spjava.shop.dto;

import java.math.BigDecimal;

public class PozycjaZamowieniaDTO {

    private Long id;

    private int ilosc;

    private BigDecimal cena;

    private ProduktDTO produkt;

    public PozycjaZamowieniaDTO(Long id, int ilosc, BigDecimal cena, ProduktDTO produkt) {
        this.id = id;
        this.ilosc = ilosc;
        this.cena = cena;
        this.produkt = produkt;
    }

    public PozycjaZamowieniaDTO() {
    }

    public Long getId() {
        return id;
    }

    public ProduktDTO getProdukt() {
        return produkt;
    }

    public void setProdukt(ProduktDTO produkt) {
        this.produkt = produkt;
    }

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

}
