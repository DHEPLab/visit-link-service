package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author hookszhang
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Override
    Tag save(Tag entity);

    @Override
    void deleteById(Long id);

    Optional<Tag> findFirstByName(String tag);

}
