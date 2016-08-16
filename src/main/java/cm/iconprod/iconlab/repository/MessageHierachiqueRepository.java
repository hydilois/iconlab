package cm.iconprod.iconlab.repository;


import cm.iconprod.iconlab.domain.MessageHierachique;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the MessageHierachique entity.
 */
@SuppressWarnings("unused")
public interface MessageHierachiqueRepository extends JpaRepository<MessageHierachique,Long> {

    /**
     *
     * @param id
     * @return
     */
    //@Query("SELECT message from MessageHierachique message INNER JOIN Projet p.messages ON p.messages.id = message.projet_id INNER JOIN Compte c.projets ON c.projets.id = p.compte_id where c.id =:id")
    @Query("SELECT message from MessageHierachique message JOIN message.projet p JOIN p.compte c WHERE c.id=:id")
    List<MessageHierachique> findMessagesByCompte(@Param("id") Long id);

   
     @Query("select message from MessageHierachique message where message.sender = ?#{principal.username}")
     List<MessageHierachique> findByMessageIsCurrentUser();
}
