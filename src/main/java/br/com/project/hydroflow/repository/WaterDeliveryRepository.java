package br.com.project.hydroflow.repository;

import br.com.project.hydroflow.domain.WaterDelivery;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterDeliveryRepository extends JpaRepository<WaterDelivery, Long> {

    @Query("SELECT w FROM WaterDelivery w WHERE YEAR(w.deliveryDate) = :year AND w.family.id = :familyId")
    List<WaterDelivery> findByYearAndFamilyId(Integer year, Long familyId);
}
