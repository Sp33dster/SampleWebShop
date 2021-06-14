package pl.spjava.shop.exception;

import pl.spjava.shop.model.Zamowienie;

public class ZamowienieException extends AppBaseException {

    static final public String KEY_DB_CONSTRAINT = "error.zamowienie.db.constraint";
    static final public String KEY_INSUFFICIENT_PROCDUCT_AMOUNT = "error.zamowienie.insufficient.product.amount";
    static final public String KEY_APROVE_OF_APROVED = "error.zamowienie.aprove.of.aproved";
    static final public String KEY_NO_STATE_IN_EJB = "error.zamowienie.no.state.in.ejb";
    static final public String KEY_NOT_FOUND = "error.zamowienie.not.found";
    static final public String KEY_PRODUCT_NOT_FOUND = "error.product.not.found";

    private ZamowienieException(String message) {
        super(message);
    }

    private ZamowienieException(String message, Throwable cause) {
        super(message, cause);
    }
    private Zamowienie zamowienie;

    public Zamowienie getZamowienie() {
        return zamowienie;
    }

    public void setZamowienie(Zamowienie zamowienie) {
        this.zamowienie = zamowienie;
    }

    static public ZamowienieException createWithTxRetryRollback() {
        ZamowienieException ze = new ZamowienieException(KEY_TX_RETRY_ROLLBACK);
        return ze;
    }

    static public ZamowienieException createWithDbCheckConstraintKey(Zamowienie zamowienie, Throwable cause) {
        ZamowienieException ze = new ZamowienieException(KEY_DB_CONSTRAINT, cause);
        ze.setZamowienie(zamowienie);
        return ze;
    }

    static public ZamowienieException createWithInsufficientProductAmount(Zamowienie zamowienie) {
        ZamowienieException ze = new ZamowienieException(KEY_INSUFFICIENT_PROCDUCT_AMOUNT);
        ze.setZamowienie(zamowienie);
        return ze;
    }

    static public ZamowienieException createWithAproveOfAproved(Zamowienie zamowienie) {
        ZamowienieException ze = new ZamowienieException(KEY_APROVE_OF_APROVED);
        ze.setZamowienie(zamowienie);
        return ze;
    }

    static public ZamowienieException createWithRemoveOfAproved() {
        ZamowienieException ze = new ZamowienieException(KEY_APROVE_OF_APROVED);
        return ze;
    }

    static public ZamowienieException createWithNotFound() {
        ZamowienieException ze = new ZamowienieException(KEY_NOT_FOUND);
        return ze;
    }

    static public ZamowienieException createNoStateInEJB() {
        ZamowienieException ze = new ZamowienieException(KEY_NO_STATE_IN_EJB);
        return ze;
    }

    static public ZamowienieException createWithProductNotFound() {
        ZamowienieException ze = new ZamowienieException(KEY_PRODUCT_NOT_FOUND);
        return ze;
    }

}
