package io.github.springmeetupparis.telaoupa.organizer;

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
class OrganizerRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrganizerRepository repository;

    private Organizer persistedOrganizer;

    @BeforeEach
    void prepare() {
        persistedOrganizer = new Organizer(1234L);
        entityManager.persist(persistedOrganizer);
    }

    @Test
    @DisplayName("Finds all stored organizers")
    void finds_organizers() {
        Iterable<Organizer> organizers = repository.findAll();

        assertThat(organizers)
                .containsExactly(persistedOrganizer);
    }

    @Test
    @DisplayName("Persists organizers")
    void persist_organizers() {
        Organizer organizer = repository.save(new Organizer(4321L));

        Organizer result = entityManager.find(Organizer.class, organizer.getId());

        assertThat(result).isEqualToComparingFieldByField(organizer);
    }

    @Test
    @DisplayName("Blows up when persisting an organizer with already stored external ID")
    void fails_persisting_duplicate_external_id() {
        Organizer duplicate = new Organizer(persistedOrganizer.getExternalId());

        assertThatThrownBy(() -> repository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("ORGANIZER_UNIQUE_EXTERNAL_ID");
    }

}