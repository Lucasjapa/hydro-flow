package br.com.project.hydroflow.repository;

import br.com.project.hydroflow.domain.MonthlyRainfall;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyRainfallRepository extends JpaRepository<MonthlyRainfall, Long> {

    List<MonthlyRainfall> findByYear(Integer year);
}
