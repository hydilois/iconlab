package cm.iconprod.iconlab.repository.search;

import cm.iconprod.iconlab.domain.Tache;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Tache entity.
 */
public interface TacheSearchRepository extends ElasticsearchRepository<Tache, Long> {
}
