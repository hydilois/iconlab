package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.Commentaire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Commentaire entity.
 */
@SuppressWarnings("unused")
public interface CommentaireRepository extends JpaRepository<Commentaire,Long> {

}
