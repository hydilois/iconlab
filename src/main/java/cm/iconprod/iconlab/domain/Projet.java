package cm.iconprod.iconlab.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Projet.
 */
//Creation d'un entite Projet
@Entity
@Table(name = "projet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projet")
public class Projet implements Serializable {

    /*private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;
    // cool la programmation
    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "fichier_projet")
    private byte[] fichierProjet;

    @Column(name = "fichier_projet_content_type")
    private String fichierProjetContentType;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "actif")
    private Boolean actif;

    @ManyToOne
    private Compte compte;

    @OneToMany(mappedBy = "projet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MessageHierachique> messages = new HashSet<>();

    @OneToMany(mappedBy = "projet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Commentaire> commentaires = new HashSet<>();

    @OneToMany(mappedBy = "projet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tache> taches = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFichierProjet() {
        return fichierProjet;
    }

    public void setFichierProjet(byte[] fichierProjet) {
        this.fichierProjet = fichierProjet;
    }

    public String getFichierProjetContentType() {
        return fichierProjetContentType;
    }

    public void setFichierProjetContentType(String fichierProjetContentType) {
        this.fichierProjetContentType = fichierProjetContentType;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Set<MessageHierachique> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageHierachique> messageHierachiques) {
        this.messages = messageHierachiques;
    }

    public Set<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(Set<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public Set<Tache> getTaches() {
        return taches;
    }

    public void setTaches(Set<Tache> taches) {
        this.taches = taches;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Projet projet = (Projet) o;
        if(projet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Projet{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            ", fichierProjet='" + fichierProjet + "'" +
            ", fichierProjetContentType='" + fichierProjetContentType + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", actif='" + actif + "'" +
            '}';
    }*/

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "fichier_projet")
    private byte[] fichierProjet;

    @Column(name = "fichier_projet_content_type")
    private String fichierProjetContentType;



    @Column(name = "fromt")
    private LocalDate fromt;

    @Column(name = "tot")
    private LocalDate tot;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "height")
    private String height;

    @Column(name = "sortable")
    private Boolean sortable;

    @Column(name = "classes")
    private String classes;

    @Column(name = "color")
    private String color;

    @Column(name = "parent")
    private String parent;

    @Column(name = "tooltips")
    private Boolean tooltips;

    @ManyToOne
    private Compte compte;

    @OneToMany(mappedBy = "projet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MessageHierachique> messages = new HashSet<>();

    @OneToMany(mappedBy = "projet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Commentaire> commentaires = new HashSet<>();

    @OneToMany(mappedBy = "projet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tache> taches = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFichierProjet() {
        return fichierProjet;
    }

    public void setFichierProjet(byte[] fichierProjet) {
        this.fichierProjet = fichierProjet;
    }

    public String getFichierProjetContentType() {
        return fichierProjetContentType;
    }

    public void setFichierProjetContentType(String fichierProjetContentType) {
        this.fichierProjetContentType = fichierProjetContentType;
    }

    public LocalDate getTot() {
        return tot;
    }

    public void setTot(LocalDate to) {
        this.tot = to;
    }

    public LocalDate getFromt() {
        return fromt;
    }

    public void setFromt(LocalDate from) {
        this.fromt = from;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Boolean isSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Boolean isTooltips() {
        return tooltips;
    }

    public void setTooltips(Boolean tooltips) {
        this.tooltips = tooltips;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Set<MessageHierachique> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageHierachique> messageHierachiques) {
        this.messages = messageHierachiques;
    }

    public Set<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(Set<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public Set<Tache> getTaches() {
        return taches;
    }

    public void setTaches(Set<Tache> taches) {
        this.taches = taches;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Projet projet = (Projet) o;
        if(projet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Projet{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            ", fichierProjet='" + fichierProjet + "'" +
            ", fichierProjetContentType='" + fichierProjetContentType + "'" +
            ", tot='" + tot + "'" +
            ", fromt='" + fromt + "'" +
            ", actif='" + actif + "'" +
            ", height='" + height + "'" +
            ", sortable='" + sortable + "'" +
            ", classes='" + classes + "'" +
            ", color='" + color + "'" +
            ", parent='" + parent + "'" +
            ", tooltips='" + tooltips + "'" +
            '}';
    }
}
