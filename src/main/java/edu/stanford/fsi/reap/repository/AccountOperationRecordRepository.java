package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.entity.AccountOperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOperationRecordRepository extends JpaRepository<AccountOperationRecord, Long> {

    public List<AccountOperationRecord> findByAccountIdAndRevert(Long accuntId, Boolean revert);
}
