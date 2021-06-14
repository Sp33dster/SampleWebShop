package pl.spjava.shop.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZamowienieDTO {

    private Date lastModification;

    private Long id;

    private List<PozycjaZamowieniaDTO> pozycjeZamowienia = new ArrayList<>();

    private boolean zatwierdzone = false;

    private KlientDTO ktoZlozyl;

    private PracownikDTO ktoZatwierdzil;

    public ZamowienieDTO(Long id, Date lastModification, boolean zatwierdzone, KlientDTO ktoZlozyl, PracownikDTO ktoZatwierdzil, List<PozycjaZamowieniaDTO> pozycjeZamowienia) {
        this.id = id;
        this.lastModification = lastModification;
        this.zatwierdzone = zatwierdzone;
        this.ktoZlozyl = ktoZlozyl;
        this.ktoZatwierdzil = ktoZatwierdzil;
        this.pozycjeZamowienia = pozycjeZamowienia;
    }

    public ZamowienieDTO() {
    }
    
    public String getLoginSkladajacego() {
        // NPE-SAFE w przypadku zamówienia jeszcze nie zapisanego
        if (null == ktoZlozyl) {
            return "";
        } else {
            return ktoZlozyl.getLogin();
        }
    }

    public String getLoginZatwierdzajacego() {
        // NPE-SAFE w przypadku zamówienia nie zatwierdzonego
        if (null == ktoZatwierdzil) {
            return "";
        } else {
            return ktoZatwierdzil.getLogin();
        }
    }

    public BigDecimal getSumaZamowienia() {
        BigDecimal suma = new BigDecimal(BigInteger.ZERO, 2);
        for (PozycjaZamowieniaDTO poz : this.pozycjeZamowienia) {
            suma = suma.add(poz.getCena().multiply(new BigDecimal(poz.getIlosc())));
        }
        return suma;
    }

    public int getLiczbaPozycji() {
        return this.pozycjeZamowienia.size();
    }

    public PracownikDTO getKtoZatwierdzil() {
        return ktoZatwierdzil;
    }

    public void setKtoZatwierdzil(PracownikDTO ktoZatwierdzil) {
        this.ktoZatwierdzil = ktoZatwierdzil;
    }

    public KlientDTO getKtoZlozyl() {
        return ktoZlozyl;
    }

    public void setKtoZlozyl(KlientDTO ktoZlozyl) {
        this.ktoZlozyl = ktoZlozyl;
    }

    public boolean isZatwierdzone() {
        return zatwierdzone;
    }

    public void setZatwierdzone(boolean zatwierdzone) {
        this.zatwierdzone = zatwierdzone;
    }

    public List<PozycjaZamowieniaDTO> getPozycjeZamowienia() {
        return pozycjeZamowienia;
    }

    public void setPozycjeZamowienia(List<PozycjaZamowieniaDTO> pozycjeZamowienia) {
        this.pozycjeZamowienia = pozycjeZamowienia;
    }

    public Long getId() {
        return id;
    }

    public Date getLastModification() {
        return lastModification;
    }
}

