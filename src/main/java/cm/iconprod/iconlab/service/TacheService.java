package cm.iconprod.iconlab.service;

import cm.iconprod.iconlab.domain.Projet;
import cm.iconprod.iconlab.domain.Tache;
import cm.iconprod.iconlab.repository.TacheRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ElSha on 03/08/2016.
 */
@Service
@Transactional
public class TacheService {
    @Inject
    private TacheRepository tacheRepository;

   public List<Tache> findTacheByProjetBelong(Long id){
        List<Tache>listeTacheTotal = tacheRepository.findAll();
        List<Tache>listeRetour = new ArrayList<Tache>();
        for(Tache tache:listeTacheTotal){
            if(tache.getProjet().getId().equals(id)){
                listeRetour.add(tache);
            }
        }
        return listeRetour;
    }
}
