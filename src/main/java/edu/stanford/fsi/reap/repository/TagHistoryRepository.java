package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.entity.Tag;
import edu.stanford.fsi.reap.entity.TagHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author hookszhang
 */
@Repository
public interface TagHistoryRepository extends JpaRepository<TagHistory, Long> {

    @Override
    TagHistory save(TagHistory entity);

    @Override
    void deleteById(Long id);

    Optional<Tag> findFirstByName(String tag);
}
