package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.entity.ExportVisit;
import edu.stanford.fsi.reap.entity.enumerations.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InterfaceName: ExportVisitRepository
 * Description:
 * author: huangwenxing 2021-5-6 10:16
 */
@Repository
public interface ExportVisitRepository extends JpaRepository<ExportVisit, Long> {

  List<ExportVisit> findByStatusInOrderByCreatedAtDesc(List<VisitStatus> status);


  @Query(value = "SELECT * from visit where status='NOT_STARTED'", nativeQuery=true)
  List<ExportVisit> findListByStatusNotStart();
}
