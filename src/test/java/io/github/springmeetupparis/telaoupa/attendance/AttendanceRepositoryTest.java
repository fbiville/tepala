package io.github.springmeetupparis.telaoupa.attendance;

import io.github.springmeetupparis.telaoupa.attendee.Attendee;
import io.github.springmeetupparis.telaoupa.meetup.Meetup;
import io.github.springmeetupparis.telaoupa.session.Session;
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
class AttendanceRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AttendanceRepository repository;

    private Attendance persistedAttendance;
    private Session persistedSession;
    private Rsvp persistedRsvp;
    private Status persistedStatus;
    private Attendee persistedAttendee;

    @BeforeEach
    void prepare() {
        Meetup persistedMeetup = new Meetup(123);
        entityManager.persist(persistedMeetup);
        persistedSession = new Session(124L, persistedMeetup);
        entityManager.persist(persistedSession);
        persistedAttendee = new Attendee(125L);
        entityManager.persist(persistedAttendee);
        persistedRsvp = entityManager.createQuery("FROM Rsvp r WHERE r.value = :value", Rsvp.class).setParameter("value", "yes").getSingleResult();
        entityManager.persist(persistedRsvp);
        persistedStatus = entityManager.createQuery("FROM Status s WHERE s.value = :value", Status.class).setParameter("value", "attended").getSingleResult();
        entityManager.persist(persistedStatus);
        persistedAttendance = new Attendance(persistedSession, persistedAttendee, persistedRsvp, persistedStatus);
        entityManager.persist(persistedAttendance);
    }

    @Test
    @DisplayName("Finds all stored attendances")
    public void finds_all_attendees() {
        Iterable<Attendance> meetups = repository.findAll();

        assertThat(meetups)
                .containsExactly(persistedAttendance);
    }

    @Test
    @DisplayName("Persists attendances")
    void persist_meetups() {
        Attendee otherAttendee = new Attendee(126L);
        entityManager.persist(otherAttendee);
        Attendance attendance = repository.save(new Attendance(persistedSession, otherAttendee, persistedRsvp, persistedStatus));

        Attendance result = entityManager.find(Attendance.class, attendance.getId());

        assertThat(result).isEqualToComparingFieldByField(attendance);
    }

    @Test
    @DisplayName("Blows up when session is null")
    void fails_persisting_attendances_without_session() {
        Attendee otherAttendee = new Attendee(128L);
        entityManager.persist(otherAttendee);
        Attendance invalid = new Attendance(null, otherAttendee, persistedRsvp, persistedStatus);
        assertThatThrownBy(() -> repository.save(invalid))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("null");
    }

    @Test
    @DisplayName("Blows up when attendee is null")
    void fails_persisting_attendances_without_attendee() {
        Attendance invalid = new Attendance(persistedSession, null, persistedRsvp, persistedStatus);
        assertThatThrownBy(() -> repository.save(invalid))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("null");
    }

    @Test
    @DisplayName("Blows up when RSVP is null")
    void fails_persisting_attendances_without_rsvp() {
        Attendee otherAttendee = new Attendee(130L);
        entityManager.persist(otherAttendee);
        Attendance invalid = new Attendance(persistedSession, otherAttendee, null, persistedStatus);
        assertThatThrownBy(() -> repository.save(invalid))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("null");
    }

    @Test
    @DisplayName("Blows up when same session and attendee are inserted again")
    void fails_persisting_attendances_with_same_session_and_attendee() {
        Attendance duplicate = new Attendance(persistedSession, persistedAttendee, persistedRsvp, persistedStatus);
        assertThatThrownBy(() -> repository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("ATTENDANCE_UNIQUE_EXTERNAL_SESSION_ATTENDEE");
    }
}