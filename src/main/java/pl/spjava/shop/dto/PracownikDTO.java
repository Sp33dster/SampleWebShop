
package pl.spjava.shop.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PracownikDTO extends KontoDTO {

    @NotNull
    @Size(max=12,message="{constraint.string.length.toolong}")
    private String intercom;

    public PracownikDTO(String intercom, Long id, String login, boolean potwierdzone, boolean aktywne, String typ, String imie, String nazwisko, String email, String telefon) {
        super(id, login, potwierdzone, aktywne, typ, imie, nazwisko, email, telefon);
        this.intercom = intercom;
    }

    public PracownikDTO() {
    }

    public String getIntercom() {
        return intercom;
    }

    public void setIntercom(String intercom) {
        this.intercom = intercom;
    }
}
