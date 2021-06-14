package pl.spjava.shop.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * Klasa nadrzędna dla wszystkich klas encyjnych. Ustala wspólne metody:
 * toString() equals() hashCode()
 */
@MappedSuperclass
public abstract class AbstractEntity {

    protected static final long serialVersionUID = 1L;

    // Samego klucza głównego nie można dziedziczyć ze względu na stosowanie różnych generatorów tabelowych w różnych encjach.
    protected abstract Object getId();

    // Z kolei nie wiadomo co będzie kluczem biznesowym w poszczególnych encjach
    protected abstract Object getBusinessKey();

    @Version
    @Column(name = "wersja", nullable = false)
    private int wersja;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_timestamp")
    private Date creationTimestamp;

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public Date getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_timestamp")
    private Date modificationTimestamp;

    @PreUpdate
    private void updateTimestamp() {
        modificationTimestamp = new Date();
    }

    @PrePersist
    private void creationTimestamp() {
        creationTimestamp = new Date();
    }

    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", key=" + getBusinessKey() + ", version=" + wersja + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this.getClass().isAssignableFrom(obj.getClass())) {
            return this.getBusinessKey().equals(((AbstractEntity) obj).getBusinessKey());
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return getBusinessKey().hashCode();
    }
}
