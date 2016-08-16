package cm.iconprod.iconlab.repository;

import cm.iconprod.iconlab.domain.Documents;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Documents entity.
 */
@SuppressWarnings("unused")
public interface DocumentsRepository extends JpaRepository<Documents,Long> {

    @Query("select documents from Documents documents where documents.user.login = ?#{principal.username}")
    List<Documents> findByUserIsCurrentUser();

    @Query("select documents from Documents documents where documents.sender = ?#{principal.username}")
    List<Documents> findByDocumentIsCurrentCompteUser();

}
