package pl.spjava.shop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@TableGenerator(name = "ZamowienieIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "Zamowienie", initialValue = 100)
@Entity
@Cacheable(false)
@Table(name = "Zamowienie")
@NamedQueries({
    @NamedQuery(name = "Zamowienie.znajdzNieZatwierdzone", query = "SELECT z FROM Zamowienie z WHERE z.zatwierdzone=false"),
    @NamedQuery(name = "Zamowienie.znajdzDlaKlienta", query = "SELECT z FROM Zamowienie z WHERE z.ktoZlozyl.login=:login"),
    @NamedQuery(name = "Zamowienie.znajdzNieZatwierdzoneDlaKlienta", query = "SELECT z FROM Zamowienie z WHERE z.zatwierdzone=false AND z.ktoZlozyl.login=:login")
})
public class Zamowienie extends AbstractEntity implements Serializable {

    @Override
    protected Object getBusinessKey() {
        return id;
    }

    @Id
    @Column(name = "id", updatable = false) //NOT NULL i UNIQUE wynmikają z ograniczenia kliucza głównego
    /*
     * Adnotacja wskazująca, że do generowania wartości tego pola zostanie
     * zastosowany generator tabelowy o wskazanej nazwie.
     */
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ZamowienieIdGen")
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "zamowienie_id", nullable = false, updatable = false)
    private List<PozycjaZamowienia> pozycjeZamowienia = new ArrayList<PozycjaZamowienia>();

    @Column(name = "zatw", nullable = false)
    private boolean zatwierdzone = false;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Klient ktoZlozyl;

    @JoinColumn(nullable = true)
    @ManyToOne
    private Pracownik ktoZatwierdzil;

    public Pracownik getKtoZatwierdzil() {
        return ktoZatwierdzil;
    }

    public void setKtoZatwierdzil(Pracownik ktoZatwierdzil) {
        this.ktoZatwierdzil = ktoZatwierdzil;
    }

    public Klient getKtoZlozyl() {
        return ktoZlozyl;
    }

    public void setKtoZlozyl(Klient ktoZlozyl) {
        this.ktoZlozyl = ktoZlozyl;
    }

    public boolean isZatwierdzone() {
        return zatwierdzone;
    }

    public void setZatwierdzone(boolean zatwierdzone) {
        this.zatwierdzone = zatwierdzone;
    }

    public List<PozycjaZamowienia> getPozycjeZamowienia() {
        return pozycjeZamowienia;
    }

    @Override
    public Long getId() {
        return id;
    }

}
