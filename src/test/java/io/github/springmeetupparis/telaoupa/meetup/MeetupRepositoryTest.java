package io.github.springmeetupparis.telaoupa.meetup;

import io.github.springmeetupparis.telaoupa.organizer.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Sets.newLinkedHashSet;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
class MeetupRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MeetupRepository repository;

    private Meetup persistedMeetup;

    @BeforeEach
    void prepare() {
        persistedMeetup = new Meetup(1234L);
        entityManager.persist(persistedMeetup);
    }

    @Test
    @DisplayName("Finds all stored meetups")
    void finds_meetups() {
        Iterable<Meetup> meetups = repository.findAll();

        assertThat(meetups)
                .containsExactly(persistedMeetup);
    }

    @Test
    @DisplayName("Persists meetups")
    void persist_meetups() {
        Meetup meetup = repository.save(new Meetup(4321L));

        Meetup result = entityManager.find(Meetup.class, meetup.getId());

        assertThat(result).isEqualToComparingFieldByField(meetup);
    }

    @Test
    @DisplayName("Persist meetups with organizers")
    @SuppressWarnings("unchecked")
    void persist_session_speakers() {
        Organizer organizer1 = new Organizer(42L);
        entityManager.persist(organizer1);
        Organizer organizer2 = new Organizer(43L);
        entityManager.persist(organizer2);

        Meetup result = repository.save(new Meetup(2222L, newLinkedHashSet(organizer1, organizer2)));

        assertThat((List) entityManager
                .createQuery("SELECT m.organizers FROM Meetup m WHERE m.id = :id", Set.class)
                .setParameter("id", result.getId())
                .getResultList())
                .containsOnlyOnce(organizer1, organizer2);
    }

    @Test
    @DisplayName("Blows up when persisting a meetup with already stored external ID")
    void fails_persisting_duplicate_external_id() {
        Meetup duplicate = new Meetup(persistedMeetup.getExternalId());

        assertThatThrownBy(() -> repository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("MEETUP_UNIQUE_EXTERNAL_ID");
    }
}