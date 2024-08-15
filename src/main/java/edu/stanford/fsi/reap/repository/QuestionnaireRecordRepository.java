package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.entity.QuestionnaireRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireRecordRepository extends JpaRepository<QuestionnaireRecord, Long> {

    @Override
    QuestionnaireRecord save(QuestionnaireRecord entity);

    @Override
    void deleteById(Long id);

    List<QuestionnaireRecord> findByVisitIdOrderByTitleNoAsc(Long visitId);

}

