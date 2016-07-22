package cm.iconprod.iconlab.repository.search;

import cm.iconprod.iconlab.domain.Documents;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Documents entity.
 */
public interface DocumentsSearchRepository extends ElasticsearchRepository<Documents, Long> {
}
