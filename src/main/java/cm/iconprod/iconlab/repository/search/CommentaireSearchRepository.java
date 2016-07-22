package cm.iconprod.iconlab.repository.search;

import cm.iconprod.iconlab.domain.Commentaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Commentaire entity.
 */
public interface CommentaireSearchRepository extends ElasticsearchRepository<Commentaire, Long> {
}
