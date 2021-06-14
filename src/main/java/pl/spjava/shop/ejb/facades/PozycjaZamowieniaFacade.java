package pl.spjava.shop.ejb.facades;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.model.PozycjaZamowienia;

@Stateless
@LocalBean
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PozycjaZamowieniaFacade extends AbstractFacade<PozycjaZamowienia> {

    @PersistenceContext(unitName = "ShopJavaDB_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PozycjaZamowieniaFacade() {
        super(PozycjaZamowienia.class);
    }

}
