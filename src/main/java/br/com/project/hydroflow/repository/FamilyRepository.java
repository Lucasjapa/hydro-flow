package br.com.project.hydroflow.repository;

import br.com.project.hydroflow.domain.Family;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    Page<Family> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Family> findByHasGutterSystemTrue();

    Page<Family> findByCisternStatus(Family.CisternStatus status, Pageable pageable);
}
