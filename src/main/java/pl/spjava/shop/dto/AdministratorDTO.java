package pl.spjava.shop.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AdministratorDTO extends KontoDTO implements Serializable {

    @NotNull
    @Size(max = 12, message = "{constraint.string.length.toolong}")
    private String alarmNumber;

    public AdministratorDTO() {
    }

    public AdministratorDTO(String alarmNumber, Long id, String login, boolean potwierdzone, boolean aktywne, String typ, String imie, String nazwisko, String email, String telefon) {
        super(id, login, potwierdzone, aktywne, typ, imie, nazwisko, email, telefon);
        this.alarmNumber = alarmNumber;
    }

    public String getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(String number) {
        this.alarmNumber = number;
    }
}
