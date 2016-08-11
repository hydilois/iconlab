package cm.iconprod.iconlab.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import cm.iconprod.iconlab.domain.enumeration.Mode;

/**
 * A Documents.
 */
@Entity
@Table(name = "documents")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "documents")
public class Documents implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "sender")
    private String sender;

    @NotNull
    @Lob
    @Column(name = "fichier", nullable = false)
    private byte[] fichier;

    @Column(name = "fichier_content_type", nullable = false)
    private String fichierContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode")
    private Mode mode;

    @Column(name = "actif")
    private Boolean actif;

    @ManyToOne
    private User user;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public byte[] getFichier() {
        return fichier;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }

    public String getFichierContentType() {
        return fichierContentType;
    }

    public void setFichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
        Documents documents = (Documents) o;
        if(documents.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, documents.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Documents{" +
            "id=" + id +
            ", titre='" + titre + "'" +
            ", fichier='" + fichier + "'" +
            ", fichierContentType='" + fichierContentType + "'" +
            ", mode='" + mode + "'" +
            ", actif='" + actif + "'" +
            '}';
    }
}
