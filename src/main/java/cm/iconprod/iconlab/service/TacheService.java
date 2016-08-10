package cm.iconprod.iconlab.service;

import cm.iconprod.iconlab.domain.Projet;
import cm.iconprod.iconlab.domain.Tache;
import cm.iconprod.iconlab.repository.TacheRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.AbstractList;
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

    public class TaskGantt{
        protected String name;
        protected String color;
        protected ZonedDateTime from;
        protected ZonedDateTime to;
        protected Integer progress ;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public ZonedDateTime getFrom() {
            return from;
        }

        public void setFrom(ZonedDateTime from) {
            this.from = from;
        }

        public ZonedDateTime getTo() {
            return to;
        }

        public void setTo(ZonedDateTime to) {
            this.to = to;
        }

        public Integer getProgress() {
            return progress;
        }

        public void setProgress(Integer progress) {
            this.progress = progress;
        }
    }

    public class ParserGantt{
           protected String name ;
           protected List<TaskGantt> tasks = new ArrayList<TaskGantt>(){};

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<TaskGantt> getTasks() {
            return tasks;
        }

        public void setTasks(List<TaskGantt> tasks) {
            this.tasks = tasks;
        }
    }

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
    public List<ParserGantt> findTacheByProjetBelongGantt(Long id){

        List<ParserGantt> liste =new ArrayList<ParserGantt>();
        List<Tache> listePrimitive = findTacheByProjetBelong(id);


        for(Tache tache:listePrimitive){

            ParserGantt elt = new ParserGantt() ;
            TaskGantt elt1 =  new TaskGantt();

             elt.name = tache.getName();
                elt1.name = tache.getName();
                elt1.color = tache.getColor();
                elt1.from = tache.getFromt();
                elt1.to =tache.getTot();
                elt1.progress =tache.getProgress();
             elt.tasks.add(elt1);
            liste.add(elt);

        }

        return liste;
    }
}
