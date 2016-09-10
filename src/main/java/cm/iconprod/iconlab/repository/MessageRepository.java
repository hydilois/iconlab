package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.Message;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("select distinct message from Message message left join fetch message.users")
    List<Message> findAllWithEagerRelationships();


    @Query("select message from Message message left join fetch message.users where message.id =:id")
    Message findOneWithEagerRelationships(@Param("id") Long id);

}
