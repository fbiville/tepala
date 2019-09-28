package io.github.springmeetupparis.telaoupa.session;

import io.github.springmeetupparis.telaoupa.meetup.Meetup;
import io.github.springmeetupparis.telaoupa.speaker.Speaker;
import org.assertj.core.util.Sets;
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

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
class SessionRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    SessionRepository repository;

    private Meetup persistedMeetup;
    private Session persistedSession;

    @BeforeEach
    void prepare() {
        persistedMeetup = new Meetup(1234L);
        entityManager.persist(persistedMeetup);
        persistedSession = new Session(1234L, persistedMeetup);
        entityManager.persist(persistedSession);
    }

    @Test
    @DisplayName("Finds all stored sessions")
    void finds_sessions() {
        Iterable<Session> sessions = repository.findAll();

        assertThat(sessions)
                .containsExactly(persistedSession);
    }

    @Test
    @DisplayName("Persists sessions")
    void persist_sessions() {
        Session session = repository.save(new Session(4321L, persistedMeetup));

        Session result = entityManager.find(Session.class, session.getId());

        assertThat(result).isEqualToComparingFieldByField(session);
    }

    @Test
    @DisplayName("Persist meetup sessions with speakers")
    @SuppressWarnings("unchecked")
    void persist_session_speakers() {
        Speaker speaker1 = new Speaker("Florent", "Biville");
        entityManager.persist(speaker1);
        Speaker speaker2 = new Speaker("Éric", "Bottard");
        entityManager.persist(speaker2);

        Session result = repository.save(new Session(42L, persistedMeetup, Sets.newLinkedHashSet(speaker1, speaker2)));

        assertThat((List) entityManager
                .createQuery("SELECT s.speakers FROM Session s WHERE s.id = :id", Set.class)
                .setParameter("id", result.getId())
                .getResultList())
                .containsOnlyOnce(speaker1, speaker2);
    }

    @Test
    @DisplayName("Blows up when persisting a session with already stored external ID")
    void fails_persisting_duplicate_external_id() {
        Session duplicate = new Session(persistedSession.getExternalId(), persistedSession.getMeetup());

        assertThatThrownBy(() -> repository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("SESSION_UNIQUE_EXTERNAL_ID");
    }

    @Test
    @DisplayName("Blows up when persisting a session with no associated meetup")
    void fails_persisting_null_meetup() {
        Session invalid = new Session(1221L, null);

        assertThatThrownBy(() -> repository.save(invalid))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("null");
    }
}