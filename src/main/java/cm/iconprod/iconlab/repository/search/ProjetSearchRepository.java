package cm.iconprod.iconlab.repository.search;

import cm.iconprod.iconlab.domain.Projet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Projet entity.
 */
public interface ProjetSearchRepository extends ElasticsearchRepository<Projet, Long> {
}
