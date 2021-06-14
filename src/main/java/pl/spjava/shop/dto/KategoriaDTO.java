package pl.spjava.shop.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class KategoriaDTO {

    private Long id;

    @NotNull
    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    private String nazwa;

    @NotNull
    @Size(max = 200, message = "{constraint.string.length.toolong}")
    private String opis;

    public KategoriaDTO(Long id, String nazwa, String opis) {
        this.id = id;
        this.nazwa = nazwa;
        this.opis = opis;
    }

    public KategoriaDTO() {
    }

    public Long getId() {
        return id;
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
