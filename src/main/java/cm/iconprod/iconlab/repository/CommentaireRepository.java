package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.Commentaire;
import cm.iconprod.iconlab.domain.MessageHierachique;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Commentaire entity.
 */
@SuppressWarnings("unused")
public interface CommentaireRepository extends JpaRepository<Commentaire,Long> {
    @Query("SELECT commentaire from Commentaire commentaire JOIN commentaire.projet p WHERE p.id=:id")
    List<Commentaire> findCommentaireByProject(@Param("id") Long id);

}
