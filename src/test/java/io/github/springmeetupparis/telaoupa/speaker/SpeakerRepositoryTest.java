package io.github.springmeetupparis.telaoupa.speaker;


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
class SpeakerRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    SpeakerRepository repository;

    private Speaker persistedSpeaker;

    @BeforeEach
    void prepare() {
        persistedSpeaker = new Speaker("Stéphane", "Nicoll");
        entityManager.persist(persistedSpeaker);
    }

    @Test
    @DisplayName("Finds all stored speakers")
    void finds_speakers() {
        Iterable<Speaker> speakers = repository.findAll();

        assertThat(speakers)
                .containsExactly(persistedSpeaker);
    }

    @Test
    @DisplayName("Persists speakers")
    void persist_speakers() {
        Speaker speaker = repository.save(new Speaker("Josh", "Long"));

        Speaker result = entityManager.find(Speaker.class, speaker.getId());

        assertThat(result).isEqualToComparingFieldByField(speaker);
    }

    @Test
    @DisplayName("Blows up if persisting a speaker with already stored first and last name")
    void fails_persisting_duplicate_external_id() {
        Speaker duplicate = new Speaker(persistedSpeaker.getFirstName(), persistedSpeaker.getLastName());

        assertThatThrownBy(() -> repository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("SPEAKER_UNIQUE_NAME");
    }
}