package cm.iconprod.iconlab.repository.search;

import cm.iconprod.iconlab.domain.MessageHierachique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MessageHierachique entity.
 */
public interface MessageHierachiqueSearchRepository extends ElasticsearchRepository<MessageHierachique, Long> {
}
