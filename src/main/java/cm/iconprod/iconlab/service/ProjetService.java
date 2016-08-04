package cm.iconprod.iconlab.service;

import cm.iconprod.iconlab.domain.Projet;
import cm.iconprod.iconlab.repository.ProjetRepository;
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
public class ProjetService {

    @Inject
    private ProjetRepository projetRepository;

    public List<Projet> findProjetByCompteBelong(Long idCompte){
        List<Projet> listeProjetTotal = projetRepository.findAll();
        List<Projet> listeRetour = new ArrayList<Projet>();
        for(Projet projet:listeProjetTotal){
            if(projet.getCompte().getId().equals(idCompte)){
                listeRetour.add(projet);
            }
        }
        return listeRetour;
    }

}
