package pl.spjava.shop.ejb.facades;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.model.Produkt;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class ProduktFacade extends AbstractFacade<Produkt> {

    @PersistenceContext(unitName = "ShopJavaDB_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProduktFacade() {
        super(Produkt.class);
    }

    public List<Produkt> znajdzBedaceNaStanie() {
        TypedQuery<Produkt> tq = em.createNamedQuery("Produkt.znajdzNaStanie", Produkt.class);
        return tq.getResultList();
    }
}
