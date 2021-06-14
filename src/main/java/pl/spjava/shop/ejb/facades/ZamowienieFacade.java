package pl.spjava.shop.ejb.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.model.PozycjaZamowienia;
import pl.spjava.shop.model.Produkt;
import pl.spjava.shop.model.Zamowienie;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class ZamowienieFacade extends AbstractFacade<Zamowienie> {

    @PersistenceContext(unitName = "ShopJavaDB_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZamowienieFacade() {
        super(Zamowienie.class);
    }

    public List<Zamowienie> znajdzZamowieniaNieZatwierdzone() {
        TypedQuery<Zamowienie> tq = em.createNamedQuery("Zamowienie.znajdzNieZatwierdzone", Zamowienie.class);
        return tq.getResultList();
    }

    public List<Zamowienie> znajdzZamowieniaDlaKlienta(String login) {
        TypedQuery<Zamowienie> tq = em.createNamedQuery("Zamowienie.znajdzDlaKlienta", Zamowienie.class);
        tq.setParameter("login", login);
        return tq.getResultList();
    }

    public List<Zamowienie> znajdzZamowieniaNieZatwierdzoneDlaKlienta(String login) {
        TypedQuery<Zamowienie> tq = em.createNamedQuery("Zamowienie.znajdzNieZatwierdzoneDlaKlienta", Zamowienie.class);
        tq.setParameter("login", login);
        return tq.getResultList();
    }

    public void odswiezCenyProduktow(Zamowienie zam) {
        for (PozycjaZamowienia poz : zam.getPozycjeZamowienia()) {
            poz.setProdukt(em.find(Produkt.class, poz.getProdukt().getId()));
            em.refresh(poz.getProdukt()); // Musimy byc pewni ze stan produktow jest aktualny, pobrany w ramach tej samej transakcji. UWAGA teoretycznie moze rzucic EntityNotFound exception (o ile dopuszczamy usuwanie produktow)
            poz.setCena(poz.getProdukt().getCena());
        }
    }

}
