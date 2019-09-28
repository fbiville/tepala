package io.github.springmeetupparis.telaoupa.organizer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Organizer {

    private Long id;
    private long externalId;

    public Organizer() {
    }

    public Organizer(long externalId) {
        this.externalId = externalId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organizer organizer = (Organizer) o;
        return externalId == organizer.externalId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId);
    }
}
