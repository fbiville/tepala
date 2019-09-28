package io.github.springmeetupparis.telaoupa.attendance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Represents whether an Attendee came to a Session or not
 */
@Entity
public class Status {

    private Long id;
    private String value;

    public Status() {
    }

    public Status(String value) {
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status rsvp = (Status) o;
        return Objects.equals(value, rsvp.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
