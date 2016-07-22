package cm.iconprod.iconlab.repository.search;

import cm.iconprod.iconlab.domain.Compte;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Compte entity.
 */
public interface CompteSearchRepository extends ElasticsearchRepository<Compte, Long> {
}
