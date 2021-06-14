package pl.spjava.shop.exception;

import javax.persistence.OptimisticLockException;

public class AppOptimisticLockException extends AppBaseException {

    static final public String KEY_OPTIMISTIC_LOCK = "error.optimisticlock";

    private AppOptimisticLockException(String message) {
        super(message);
    }

    private AppOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    static public AppOptimisticLockException createWithOptimisticLockKey(OptimisticLockException cause) {
        AppOptimisticLockException ze = new AppOptimisticLockException(KEY_OPTIMISTIC_LOCK, cause);
        return ze;
    }

}
