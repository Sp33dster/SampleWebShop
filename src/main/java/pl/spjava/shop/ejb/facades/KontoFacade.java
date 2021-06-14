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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.model.Klient;
import pl.spjava.shop.model.Konto;
import pl.spjava.shop.model.Pracownik;
import pl.spjava.shop.model.ShopJavaDB_PU.Konto_;

@Stateless
@LocalBean
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class KontoFacade extends AbstractFacade<Konto> {

    @PersistenceContext(unitName = "ShopJavaDB_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KontoFacade() {
        super(Konto.class);
    }

    public Konto znajdzLogin(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Konto> query = cb.createQuery(Konto.class);
        Root<Konto> from = query.from(Konto.class);
        query = query.select(from);
        query = query.where(cb.equal(from.get(Konto_.login), login)); //Przykład wskazania atrybutu encji poprzez klasę metamodelu
        TypedQuery<Konto> tq = em.createQuery(query);

        return tq.getSingleResult();
    }

    // Wyszukiwanie konta o zadanym loginie, ale tylko jako podklasa Klient
    public Klient znajdzLoginJakoKlienta(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Klient> query = cb.createQuery(Klient.class);
        Root<Klient> from = query.from(Klient.class);
        query = query.select(from);
        query = query.where(cb.equal(from.get("login"), login)); //Przykład wskazania atrybutu encji poprzez nazwę
        TypedQuery<Klient> tq = em.createQuery(query);

        return tq.getSingleResult();
    }

    // Wyszukiwanie konta o zadanym loginie, ale tylko jako podklasa Pracownik
    public Pracownik znajdzLoginJakoPracownika(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pracownik> query = cb.createQuery(Pracownik.class);
        Root<Pracownik> from = query.from(Pracownik.class);
        query = query.select(from);
        query = query.where(cb.equal(from.get("login"), login)); //Przykład wskazania atrybutu encji poprzez nazwę
        TypedQuery<Pracownik> tq = em.createQuery(query);

        return tq.getSingleResult();
    }


    public List<Konto> dopasujKonta(String loginWzor, String imieWzor, String nazwiskoWzor, String emailWzor) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Konto> query = cb.createQuery(Konto.class);
        Root<Konto> from = query.from(Konto.class);
        query = query.select(from);
        Predicate criteria = cb.conjunction();
        if (null != loginWzor && !(loginWzor.isEmpty())) {
            criteria = cb.and(criteria, cb.like(from.get(Konto_.login), '%' + loginWzor + '%'));
        }
        if (null != imieWzor && !(imieWzor.isEmpty())) {
            criteria = cb.and(criteria, cb.like(from.get(Konto_.imie), '%' + imieWzor + '%'));
        }
        if (null != nazwiskoWzor && !(nazwiskoWzor.isEmpty())) {
            criteria = cb.and(criteria, cb.like(from.get(Konto_.nazwisko), '%' + nazwiskoWzor + '%'));
        }
        if (null != emailWzor && !(emailWzor.isEmpty())) {
            criteria = cb.and(criteria, cb.like(from.get(Konto_.email), '%' + emailWzor + '%'));
        }

        query = query.where(criteria);
        TypedQuery<Konto> tq = em.createQuery(query);
        return tq.getResultList();
    }
}
