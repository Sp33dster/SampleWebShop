package pl.spjava.shop.ejb.interceptor;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class PerformanceInterceptor {

    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object calculateDuration(InvocationContext invocation) throws Exception {
        StringBuilder sb = new StringBuilder("Przetwarzanie metody biznesowej ")
                .append(invocation.getTarget().getClass().getName()).append('.')
                .append(invocation.getMethod().getName());
        sb.append(" z tożsamością: ").append(sessionContext.getCallerPrincipal().getName());
        final long startTime = System.currentTimeMillis();
        try {
            Object result = invocation.proceed();
            return result;
        } finally {
            sb.append(" trwało ").append(System.currentTimeMillis() - startTime).append(" ms");
            Logger.getGlobal().log(Level.SEVERE, sb.toString());
        }
    }
}
