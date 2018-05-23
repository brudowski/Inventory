package home.inventory.entities;

import javax.persistence.*;

/**
 *
 * @author BRudowski
 */
@MappedSuperclass
public abstract class SeqIdEntity {
    
    @Id
    @SequenceGenerator(name="seq", sequenceName="pks", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SeqIdEntity other = (SeqIdEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
