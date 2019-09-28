package io.github.springmeetupparis.telaoupa.attendee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
class AttendeeRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AttendeeRepository repository;

    private Attendee persistedAttendee;

    @BeforeEach
    void prepare() {
        persistedAttendee = new Attendee(1234L);
        entityManager.persist(persistedAttendee);
    }

    @Test
    @DisplayName("Finds all stored attendees")
    public void finds_all_attendees() {
        Iterable<Attendee> meetups = repository.findAll();

        assertThat(meetups)
                .containsExactly(persistedAttendee);
    }

    @Test
    @DisplayName("Persists attendees")
    void persist_meetups() {
        Attendee attendee = repository.save(new Attendee(4321L));

        Attendee result = entityManager.find(Attendee.class, attendee.getId());

        assertThat(result).isEqualToComparingFieldByField(attendee);
    }

    @Test
    @DisplayName("Blows up when persisting an attendee with already stored external ID")
    void fails_persisting_duplicate_external_id() {
        Attendee duplicate = new Attendee(persistedAttendee.getExternalId());

        assertThatThrownBy(() -> repository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("ATTENDEE_UNIQUE_EXTERNAL_ID");
    }

}