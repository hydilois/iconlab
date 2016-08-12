package cm.iconprod.iconlab.service;

import cm.iconprod.iconlab.domain.*;
import cm.iconprod.iconlab.repository.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static cm.iconprod.iconlab.repository.UserRepository.*;

/**
 * Created by ICONProd on 12/08/2016.
 */
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



    //this Innerclass help us to organize our statistik data after sending in frontend
    public class CounterStatistik{
        private  int tacheLength;
        private  int articleLength;
        private  int commentaireLength;
        private  int compteLength;
        private  int messageHierachiqueLength;
        private  int projeteLength;
        private  int documentLength;
        private  int userLength;
        private  int pointAvancementLength;

        public CounterStatistik(int tacheLength, int pointAvancementLength,int userLength,int documentLength,int projeteLength,int messageHierachiqueLength,int compteLength,int commentaireLength,int articleLength) {
            this.tacheLength = tacheLength;
            this.pointAvancementLength = pointAvancementLength;
            this.userLength = userLength;
            this.documentLength = documentLength;
            this.projeteLength = projeteLength;
            this.messageHierachiqueLength = messageHierachiqueLength;
            this.compteLength = compteLength;
            this.commentaireLength = commentaireLength;
            this.articleLength = articleLength;
        }

        public int getTacheLength() {
            return tacheLength;
        }

        public void setTacheLength(int tacheLength) {
            this.tacheLength = tacheLength;
        }

        public int getPointAvancementLength() {
            return pointAvancementLength;
        }

        public void setPointAvancementLength(int pointAvancementLength) {
            this.pointAvancementLength = pointAvancementLength;
        }

        public int getUserLength() {
            return userLength;
        }

        public void setUserLength(int userLength) {
            this.userLength = userLength;
        }

        public int getDocumentLength() {
            return documentLength;
        }

        public void setDocumentLength(int documentLength) {
            this.documentLength = documentLength;
        }

        public int getProjeteLength() {
            return projeteLength;
        }

        public void setProjeteLength(int projeteLength) {
            this.projeteLength = projeteLength;
        }

        public int getMessageHierachiqueLength() {
            return messageHierachiqueLength;
        }

        public void setMessageHierachiqueLength(int messageHierachiqueLength) {
            this.messageHierachiqueLength = messageHierachiqueLength;
        }

        public int getCommentaireLength() {
            return commentaireLength;
        }

        public void setCommentaireLength(int commentaireLength) {
            this.commentaireLength = commentaireLength;
        }

        public int getCompteLength() {
            return compteLength;
        }

        public void setCompteLength(int compteLength) {
            this.compteLength = compteLength;
        }

        public int getArticleLength() {
            return articleLength;
        }

        public void setArticleLength(int articleLength) {
            this.articleLength = articleLength;
        }
    }

    public CounterStatistik getStatData(){

        int listeTaches = tacheRepository.findAll().size();
        int listeArticles = articleRepository.findAll().size();
        int listeCommentaires = commentaireRepository.findAll().size();
        int listeComptes = compteRepository.findAll().size();
        int listeDocuments = documentsRepository.findAll().size();
        int listeMessageHierachiques = messageHierachiqueRepository.findAll().size();
        int listePointAvancements = pointAvancementRepository.findAll().size();
        int listeProjets = projetRepository.findAll().size();
        int listeUsers = userRepository.findAll().size();

        CounterStatistik statObj = new CounterStatistik(listeTaches,
                                    listePointAvancements,
                                    listeUsers,
                                    listeDocuments,
                                    listeProjets,
                                    listeMessageHierachiques,
                                    listeComptes,listeCommentaires,listeArticles);

        return statObj;
    }




}
