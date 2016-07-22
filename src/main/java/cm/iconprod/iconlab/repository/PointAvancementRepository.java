package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.PointAvancement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PointAvancement entity.
 */
@SuppressWarnings("unused")
public interface PointAvancementRepository extends JpaRepository<PointAvancement,Long> {

}
