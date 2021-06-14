package pl.spjava.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@TableGenerator(name = "ProduktIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "Produkt", initialValue = 100)
@Entity
@Cacheable(false)
@Table(name = "Produkt")
@NamedQueries({
    @NamedQuery(name = "Produkt.znajdzNaStanie", query = "SELECT p FROM Produkt p WHERE p.stan > 0")
})
public class Produkt extends AbstractEntity implements Serializable {

    @Override
    protected Object getBusinessKey() {
        return nazwa;
    }
    @Id
    @Column(name = "id", updatable = false) //NOT NULL i UNIQUE wynikają z ograniczenia kliucza głównego
    /*
     * Adnotacja wskazująca, że do generowania wartości tego pola zostanie
     * zastosowany generator tabelowy o wskazanej nazwie.
     */
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ProduktIdGen")
    private Long id;

    @Override
    public Long getId() {
        return id;
    }
    @NotNull
    @Size(min = 3, max = 32)
    @Column(name = "nazwa", length = 32, nullable = false, unique = true, updatable = false)
    private String nazwa;

    @NotNull
    @Size(max = 200)
    @Column(name = "opis", length = 200)
    private String opis;

    @Digits(integer = 6, fraction = 2)
    @DecimalMin(value = "0.01")
    @Column(name = "cena", precision = 8, scale = 2, nullable = false)
    private BigDecimal cena;

    @Min(value = 0)
    @Column(name = "stan", nullable = false)
    private int stan = 0;
    @ManyToMany
    @JoinTable(name = "ProduktyKategorie", joinColumns = {
        @JoinColumn(name = "produkt_id")}, inverseJoinColumns = {
        @JoinColumn(name = "kategoria_id")}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"produkt_id", "kategoria_id"})})

    private List<Kategoria> kategorie = new ArrayList<Kategoria>();

    public List<Kategoria> getKategorie() {
        return kategorie;
    }

    public void setKategorie(List<Kategoria> kategorie) {
        this.kategorie = kategorie;
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
