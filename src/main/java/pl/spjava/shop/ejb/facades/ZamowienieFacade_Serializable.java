package pl.spjava.shop.ejb.facades;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.exception.AppBaseException;
import pl.spjava.shop.exception.AppOptimisticLockException;
import pl.spjava.shop.exception.ZamowienieException;
import pl.spjava.shop.model.Zamowienie;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class ZamowienieFacade_Serializable extends AbstractFacade<Zamowienie> {

    @PersistenceContext(unitName = "ShopJavaDB_PU_Serializable")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZamowienieFacade_Serializable() {
        super(Zamowienie.class);
    }

    @Override
    public void edit(Zamowienie entity) throws AppBaseException {
        try {
            super.edit(entity);
            em.flush();
        } catch (OptimisticLockException oe) {
            throw AppOptimisticLockException.createWithOptimisticLockKey(oe);
        } catch (DatabaseException de) {
            throw ZamowienieException.createWithDbCheckConstraintKey(entity, de);
        }
    }
}
