package io.github.springmeetupparis.telaoupa.attendance;

import io.github.springmeetupparis.telaoupa.attendee.Attendee;
import io.github.springmeetupparis.telaoupa.session.Session;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

/*
 * Note that the external ID (typically from Meetup API) could have been stored here, like other entities do.
 * However, contrary to the other entities', this entity's data has higher precedence over external data.
 */
@Entity
public class Attendance {

    private Long id;
    private Session session;
    private Attendee attendee;
    private Rsvp rsvp;
    private Status status;

    public Attendance() {
    }

    public Attendance(Session session, Attendee attendee, Rsvp rsvp, Status status) {
        this.session = session;
        this.attendee = attendee;
        this.rsvp = rsvp;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @ManyToOne
    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    @ManyToOne
    public Rsvp getRsvp() {
        return rsvp;
    }

    public void setRsvp(Rsvp rsvp) {
        this.rsvp = rsvp;
    }

    @ManyToOne
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(session, that.session) &&
                Objects.equals(attendee, that.attendee) &&
                Objects.equals(rsvp, that.rsvp) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, attendee, rsvp, status);
    }
}
