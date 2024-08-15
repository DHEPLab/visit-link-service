package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.entity.CommunityHouseWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityHouseWorkerRepository extends JpaRepository<CommunityHouseWorker, Long> {

    @Override
    CommunityHouseWorker save(CommunityHouseWorker entity);

    @Override
    void deleteById(Long id);

    Optional<CommunityHouseWorker> findFirstByIdentity(String identity);

    List<CommunityHouseWorker> findBySupervisorId(Long id);

    List<CommunityHouseWorker> findByOrderByCreatedAtDesc();
}
