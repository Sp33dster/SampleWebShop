package pl.spjava.shop.dto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class KontoDTO {

    private Long id;
    
    @NotNull(message="{constraint.notnull}")
    @Size(min=3,max=32,message="{constraint.string.length.notinrange}")
    @Pattern(regexp="^[_a-zA-Z0-9-]*$",message="{constraint.string.incorrectchar}")
    private String login;

    @NotNull(message="{constraint.notnull}")
    @Size(min=8,message="{constraint.string.length.tooshort}")
    @Column(name = "haslo", length = 256, nullable = false)
    private String haslo;

    private boolean potwierdzone;
    
    private boolean aktywne;

    private String typ;

    @NotNull(message="{constraint.notnull}")
    @Size(min=3,max=32,message="{constraint.string.length.notinrange}")
    private String imie;

    @NotNull(message="{constraint.notnull}")
    @Size(min=3,max=32,message="{constraint.string.length.notinrange}")
    private String nazwisko;

    @NotNull(message="{constraint.notnull}")
    @Size(min=6,max=64,message="{constraint.string.length.notinrange}")
    @Pattern(regexp="^[_a-zA-Z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$",message="{constraint.string.incorrectemail}")
    private String email;
        
    @Size(max=12,message="{constraint.string.length.toolong}")
    private String telefon;

    public KontoDTO() {
    }

    //Konstruktor nie inicjuje hasła. Nigdy nie będzie potrzeby eksponowania go w widoku.
    public KontoDTO(Long id, String login, boolean potwierdzone, boolean aktywne, String typ, String imie, String nazwisko, String email, String telefon) {
        this.id = id;
        this.login = login;
        this.potwierdzone = potwierdzone;
        this.aktywne = aktywne;
        this.typ = typ;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.telefon = telefon;
    }

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
