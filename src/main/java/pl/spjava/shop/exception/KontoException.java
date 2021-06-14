package pl.spjava.shop.exception;

import pl.spjava.shop.model.Konto;

public class KontoException extends AppBaseException {

    static final public String KEY_DB_CONSTRAINT = "error.konto.db.constraint.uniq.login";

    private KontoException(String message) {
        super(message);
    }

    private KontoException(String message, Throwable cause) {
        super(message, cause);
    }

    private Konto konto;

    public Konto getKonto() {
        return konto;
    }

    static public KontoException createTxRetryRollback() {
        KontoException ke = new KontoException(KEY_TX_RETRY_ROLLBACK);
        return ke;
    }

    static public KontoException createWithDbCheckConstraintKey(Konto konto, Throwable cause) {
        KontoException ke = new KontoException(KEY_DB_CONSTRAINT, cause);
        ke.konto = konto;
        return ke;
    }
}
