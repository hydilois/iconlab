package cm.iconprod.iconlab.repository.search;

import cm.iconprod.iconlab.domain.PointAvancement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PointAvancement entity.
 */
public interface PointAvancementSearchRepository extends ElasticsearchRepository<PointAvancement, Long> {
}
