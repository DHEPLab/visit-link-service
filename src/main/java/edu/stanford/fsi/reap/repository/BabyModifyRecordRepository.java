package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.dto.BabyModifyRecordDTO;
import edu.stanford.fsi.reap.entity.BabyModifyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface BabyModifyRecordRepository extends JpaRepository<BabyModifyRecord, Long> {

  @Query("from BabyModifyRecord where babyId=?1  and deleted=0 order by createdAt desc")
  List<BabyModifyRecord> findByBabyId(Long id);

  List<BabyModifyRecord> findByBabyIdAndApprovedFalseOrderByLastModifiedAtDesc(Long babyId);
}
