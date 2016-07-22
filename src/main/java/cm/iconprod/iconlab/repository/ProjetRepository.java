package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.Projet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Projet entity.
 */
@SuppressWarnings("unused")
public interface ProjetRepository extends JpaRepository<Projet,Long> {

    @Query("select projet from Projet projet where projet.user.login = ?#{principal.username}")
    List<Projet> findByUserIsCurrentUser();

}
