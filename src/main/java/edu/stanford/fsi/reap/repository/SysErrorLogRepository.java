package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.entity.ErrorLog;
import edu.stanford.fsi.reap.entity.enumerations.ErrorLogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysErrorLogRepository extends JpaRepository<ErrorLog, Long> {

  @Query("from ErrorLog where type = ?1 and typeId = ?2")
  List<ErrorLog> findByTypeAndTypeId(ErrorLogType type, Long typeId);

}
