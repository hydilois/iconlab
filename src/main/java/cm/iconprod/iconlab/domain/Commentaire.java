package cm.iconprod.iconlab.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Commentaire.
 */
@Entity
@Table(name = "commentaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "commentaire")
public class Commentaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "contenu", nullable = false)
    private String contenu;

    @Column(name = "auteur")
    private String auteur;

    @Column(name = "date_post")
    private ZonedDateTime datePost;

    @Column(name = "actif")
    private Boolean actif;

    @ManyToOne
    private Projet projet;

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public ZonedDateTime getDatePost() {
        return datePost;
    }

    public void setDatePost(ZonedDateTime datePost) {
        this.datePost = datePost;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Commentaire commentaire = (Commentaire) o;
        if(commentaire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, commentaire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Commentaire{" +
            "id=" + id +
            ", contenu='" + contenu + "'" +
            ", datePost='" + datePost + "'" +
            ", actif='" + actif + "'" +
            '}';
    }
}
