package pl.spjava.shop.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class KlientDTO extends KontoDTO {

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$", message = "{constraint.string.incorrectNIP}")
    private String nip;

    public KlientDTO(String nip, Long id, String login, boolean potwierdzone, boolean aktywne, String typ, String imie, String nazwisko, String email, String telefon) {
        super(id, login, potwierdzone, aktywne, typ, imie, nazwisko, email, telefon);
        this.nip = nip;
    }

    public KlientDTO() {
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }
}
