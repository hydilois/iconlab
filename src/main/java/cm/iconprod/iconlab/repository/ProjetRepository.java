package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.Projet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Projet entity.
 */
@SuppressWarnings("unused")
public interface
ProjetRepository extends JpaRepository<Projet,Long> {

    @Query("select projet from Projet projet where projet.user.login = ?#{principal.username}")
    Page<Projet> findByUserIsCurrentUser(Pageable pageable);

}