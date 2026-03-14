package br.com.project.hydroflow.repository;

import br.com.project.hydroflow.domain.WaterDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterDeliveryRepository extends JpaRepository<WaterDelivery, Long> {}
