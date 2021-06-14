package pl.spjava.shop.web.utils;

import pl.spjava.shop.dto.AdministratorDTO;
import pl.spjava.shop.dto.KlientDTO;
import pl.spjava.shop.dto.KontoDTO;
import pl.spjava.shop.dto.PracownikDTO;

public class KontoUtils {

    public static boolean isAdministrator(KontoDTO konto) {
        return (konto instanceof AdministratorDTO);
    }

    public static boolean isPracownik(KontoDTO konto) {
        return (konto instanceof PracownikDTO);
    }

    public static boolean isKlient(KontoDTO konto) {
        return (konto instanceof KlientDTO);
    }

    public static String wyliczSkrotHasla(String hasloJawne) {
        //TODO: wstawić algorytm skrótu hasła
        return hasloJawne;
    }

}
