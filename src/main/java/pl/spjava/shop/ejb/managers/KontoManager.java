package pl.spjava.shop.ejb.managers;

import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.spjava.shop.ejb.facades.KontoFacade;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.exception.AppBaseException;
import pl.spjava.shop.model.Administrator;
import pl.spjava.shop.model.Klient;
import pl.spjava.shop.model.Pracownik;

@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class KontoManager extends AbstractManager
        implements SessionSynchronization {

    @Inject
    private KontoFacade kontoFacade;

    public void utworzKonto(Klient klient) throws AppBaseException {
        kontoFacade.create(klient);
    }

    public void utworzKonto(Pracownik pracownik) throws AppBaseException {
        kontoFacade.create(pracownik);
    }

    public void utworzKonto(Administrator administrator) throws AppBaseException {
        kontoFacade.create(administrator);
    }
}
