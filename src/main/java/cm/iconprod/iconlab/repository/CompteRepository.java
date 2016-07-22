package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.Compte;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Compte entity.
 */
@SuppressWarnings("unused")
public interface CompteRepository extends JpaRepository<Compte,Long> {

    @Query("select compte from Compte compte where compte.user.login = ?#{principal.username}")
    List<Compte> findByUserIsCurrentUser();

}
