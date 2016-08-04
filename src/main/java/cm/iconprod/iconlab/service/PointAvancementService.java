package cm.iconprod.iconlab.service;

import cm.iconprod.iconlab.domain.PointAvancement;
import cm.iconprod.iconlab.repository.PointAvancementRepository;
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
public class PointAvancementService {

    @Inject
    private PointAvancementRepository pointAvancementRepository;

   public List<PointAvancement> findPointAvancementByProjetBelong(Long id){
        List<PointAvancement>listePointAvancementTotal = pointAvancementRepository.findAll();
        List<PointAvancement>listeRetour = new ArrayList<>();
        for(PointAvancement pointAvancement:listePointAvancementTotal){
            if(pointAvancement.getTache().getId().equals(id)){
                listeRetour.add(pointAvancement);
            }
        }
        return listeRetour;
    }
}
