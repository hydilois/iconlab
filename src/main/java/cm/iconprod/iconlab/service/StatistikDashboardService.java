package cm.iconprod.iconlab.service;

import cm.iconprod.iconlab.domain.*;
import cm.iconprod.iconlab.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static cm.iconprod.iconlab.repository.UserRepository.*;

/**
 * Created by ICONProd on 12/08/2016.
 */
@Service
@Transactional
public class StatistikDashboardService {

    @Inject
    private ArticleRepository articleRepository;

    @Inject
    private CommentaireRepository commentaireRepository;

    @Inject
    private CompteRepository compteRepository;

    @Inject
    private DocumentsRepository documentsRepository;

    @Inject
    private MessageHierachiqueRepository messageHierachiqueRepository;

    @Inject
    private PointAvancementRepository pointAvancementRepository;

    @Inject
    private ProjetRepository projetRepository;

    @Inject
    private TacheRepository tacheRepository;

    @Inject
    private UserRepository userRepository;



    public List<Integer> getStatData(){

        List<Integer> listeretour = new ArrayList<Integer>();

        int listeTaches = tacheRepository.findAll().size();
        int listeArticles = articleRepository.findAll().size();
        int listeCommentaires = commentaireRepository.findAll().size();
        int listeComptes = compteRepository.findAll().size();
        int listeDocuments = documentsRepository.findAll().size();
        int listeMessageHierachiques = messageHierachiqueRepository.findAll().size();
        int listePointAvancements = pointAvancementRepository.findAll().size();
        int listeProjets = projetRepository.findAll().size();
        int listeUsers = userRepository.findAll().size();


        listeretour.add(0,listeTaches);
        listeretour.add(1,listePointAvancements);
        listeretour.add(2,listeDocuments);
        listeretour.add(3,listeProjets);
        listeretour.add(4,listeMessageHierachiques);
        listeretour.add(5,listeComptes);
        listeretour.add(6,listeArticles);
        listeretour.add(7,listeCommentaires);
        listeretour.add(8,listeUsers);

        System.out.println(listeTaches+" i m here "+listeArticles+"edmin "+listeDocuments);

        return listeretour;
    }




}
