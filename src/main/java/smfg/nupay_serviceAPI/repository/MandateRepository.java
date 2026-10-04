package smfg.nupay_serviceAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smfg.nupay_serviceAPI.entity.Mandate;

import java.util.Optional;

@Repository
public interface MandateRepository extends JpaRepository<Mandate,Long> {

    Optional<Mandate> findBySourceRequestId(String sourceRequestId);

    boolean existsBySourceRequestId(String sourceRequestId);


}
