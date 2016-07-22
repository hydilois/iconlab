package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.Tache;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tache entity.
 */
@SuppressWarnings("unused")
public interface TacheRepository extends JpaRepository<Tache,Long> {

    @Query("select tache from Tache tache where tache.user.login = ?#{principal.username}")
    List<Tache> findByUserIsCurrentUser();

}
