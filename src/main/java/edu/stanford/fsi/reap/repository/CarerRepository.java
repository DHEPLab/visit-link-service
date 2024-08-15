package edu.stanford.fsi.reap.repository;

import edu.stanford.fsi.reap.entity.Carer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarerRepository extends JpaRepository<Carer, Long> {
    @Override
    Carer save(Carer entity);

    @Override
    void deleteById(Long id);

    List<Carer> findByBabyIdOrderByMasterDesc(Long babyId);

    List<Carer> findByBabyIdAndBabyChwIdOrderByMasterDesc(Long babyId, Long chwId);

    Optional<Carer> findOneByBabyIdAndMasterIsTrue(Long id);

    List<Carer> findAllByBabyId(Long id);

    @Query(
            nativeQuery = true,
            value = "select id from healthy.carer where baby_id=?1"
    )
    List<Long> findCarerIdByBabyId(Long babyId);
}
