package io.github.springmeetupparis.telaoupa.session;

import io.github.springmeetupparis.telaoupa.meetup.Meetup;
import io.github.springmeetupparis.telaoupa.speaker.Speaker;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Session {
    private Long id;
    private long externalId;
    private Meetup meetup;
    private Set<Speaker> speakers = new LinkedHashSet<>();

    public Session() {

    }

    public Session(long externalId, Meetup meetup) {
        this(externalId, meetup, new LinkedHashSet<>());
    }

    public Session(long externalId, Meetup meetup, Set<Speaker> speakers) {

        this.externalId = externalId;
        this.meetup = meetup;
        this.speakers = speakers;
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

    @ManyToOne
    public Meetup getMeetup() {
        return meetup;
    }

    public void setMeetup(Meetup meetup) {
        this.meetup = meetup;
    }

    @ManyToMany
    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Set<Speaker> speakers) {
        this.speakers = speakers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return externalId == session.externalId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId);
    }
}
