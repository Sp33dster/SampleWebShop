package pl.spjava.shop.ejb.endpoints;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.spjava.shop.dto.ProduktDTO;
import pl.spjava.shop.ejb.facades.ProduktFacade;
import pl.spjava.shop.ejb.interceptor.LoggingInterceptor;
import pl.spjava.shop.ejb.interceptor.PerformanceInterceptor;
import pl.spjava.shop.utils.DTOConverter;

@Stateful
@LocalBean
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ProduktEndpoint {

    @Inject
    private ProduktFacade produktFacade;

    @Interceptors(PerformanceInterceptor.class)
    public List<ProduktDTO> znajdzBedaceNaStanie() {
        return DTOConverter.tworzListeProduktDTOzEncji(produktFacade.znajdzBedaceNaStanie());
    }
        
}
