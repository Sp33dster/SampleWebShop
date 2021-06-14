package pl.spjava.shop.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Konto")
/*
 * Adnotacja deklarująca łączenie tabel, tzn. reprezentowanie przez tę klasę
 * encyjną krotek z innej tabeli. Aby łączenie tabel mogło funkcjonować, między
 * tabelami musi zachodzić relacja 1:1 - równość kluczy głównych.
 */
@SecondaryTable(name = "DanePersonalne")

/*
 * Encja Konto nie definiuje kwerend nazywanych (NamedQueries), ponieważ do wyszukiwania kont jest używane Criteria API.
 * Zobacz metody komponentu KontoFacade.
 */

/*
 * Adnotacja deklarująca generator tabelowy używany do generowania wartości
 * kluczy głównych. Generator tabelowy jako jedyny działa wyłącznie w oparciu o
 * standardowe kwerendy SQL. Atrybuty: name - nazwa generatora table - tabela, w
 * której generator przechowuje swoje dane robocze (tabela generatora)
 * pkColumnName - nazwa kolumny w tabeli generatora, której wartości
 * identyfikują rezerwy numeracji dla poszczególnych klas valueColumnName -
 * nazwa kolumny w tabeli generatora, której wartości stanowią rezerwy numeracji
 * dla poszczególnych klas pkColumnValue - wartość w kolumnie pkColumnName,
 * identyfikująca rezerwę numeracji dla tej klasy. Wartości te powinny być
 * unikalne dla poszczególnych klas (np. nazwy klas).
 */
@TableGenerator(name = "KontoIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "Konto", initialValue=100)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "typ")
@DiscriminatorValue("KONTO")
public abstract class Konto extends AbstractEntity implements Serializable {

    public Konto() {
    }

    /**
     * Metoda zwraca identyfikator biznesowy encji, używany w klasie
     * AbstractEntity do wyliczenia hashcode.
     *
     * @return identyfikator biznesowy - tu: login
     */
    @Override
    protected Object getBusinessKey() {
        return login;
    }
    @Id
    @Column(name = "id", updatable = false) //NOT NULL i UNIQUE wynikają z ograniczenia klucza głównego
    /*
     * Adnotacja wskazująca, że do generowania wartości tego pola zostanie
     * zastosowany generator tabelowy o wskazanej nazwie.
     */
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "KontoIdGen")
    private Long id;
    
    @NotNull(message="{constraint.notnull}")
    @Size(min=3,max=32,message="{constraint.string.length.notinrange}")
    @Pattern(regexp="^[_a-zA-Z0-9-]*$",message="{constraint.string.incorrectchar}")
    @Column(name = "login", length = 32, nullable = false, unique = true, updatable = false)
    private String login;

    @NotNull(message="{constraint.notnull}")
    @Size(min=6,message="{constraint.string.length.tooshort}")
    @Column(name = "haslo", length = 256, nullable = false)
    private String haslo;

    @Column(name = "potwierdzone", nullable = false)
    private boolean potwierdzone;
    
    @Column(name = "aktywne", nullable = false)
    private boolean aktywne;
    /*
     * Zapewnia dostęp do wartości dyskryminatora klas podrzędnych. Oczywiście
     * nie można modyfikowac wartości w tej kolumnie!
     *
     */
    @Column(name="typ",updatable=false)
    private String typ;

    /*
     * Dzięki zastosowaniu adnotacji @SecondaryTable możliwe jest mapowanie w
     * tejj klasie danych z innej tabeli. Nazwa "drugiej" tabeli musi być podana
     * jako wartość atrybutu table adnotacji @Column
     */
    @NotNull(message="{constraint.notnull}")
    @Size(min=3,max=32,message="{constraint.string.length.notinrange}")
    @Column(name = "imie", table = "DanePersonalne", length = 32, nullable = false)
    private String imie;

    @NotNull(message="{constraint.notnull}")
    @Size(min=3,max=32,message="{constraint.string.length.notinrange}")
    @Column(name = "nazwisko", table = "DanePersonalne", length = 32, nullable = false)
    private String nazwisko;

    @NotNull(message="{constraint.notnull}")
    @Size(min=6,max=64,message="{constraint.string.length.notinrange}")
    @Pattern(regexp="^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$",message="{constraint.string.incorrectemail}")
    @Column(name = "email", table = "DanePersonalne", length = 64, unique = true, nullable = false)
    private String email;
        
    @Size(max=12,message="{constraint.string.length.toolong}")
    @Column(name = "telefon", table = "DanePersonalne", length = 12, unique = true, nullable = true)
    private String telefon;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public boolean isPotwierdzone() {
        return potwierdzone;
    }

    public void setPotwierdzone(boolean potwierdzone) {
        this.potwierdzone = potwierdzone;
    }

    public boolean isAktywne() {
        return aktywne;
    }

    public void setAktywne(boolean aktywne) {
        this.aktywne = aktywne;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getTyp() {
        return typ;
    }
}
