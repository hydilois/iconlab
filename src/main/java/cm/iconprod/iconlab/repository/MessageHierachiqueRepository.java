package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.MessageHierachique;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MessageHierachique entity.
 */
@SuppressWarnings("unused")
public interface MessageHierachiqueRepository extends JpaRepository<MessageHierachique,Long> {

}
