package io.github.springmeetupparis.telaoupa.meetup;

import io.github.springmeetupparis.telaoupa.organizer.Organizer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Meetup {
    private Long id;
    private long externalId;
    private Set<Organizer> organizers = new LinkedHashSet<>();

    public Meetup() {

    }

    public Meetup(long externalId) {
        this(externalId, new LinkedHashSet<>());
    }

    public Meetup(long externalId, Set<Organizer> organizers) {
        this.externalId = externalId;
        this.organizers = organizers;
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

    @ManyToMany
    public Set<Organizer> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(Set<Organizer> organizers) {
        this.organizers = organizers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meetup meetup = (Meetup) o;
        return Objects.equals(externalId, meetup.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId);
    }
}
