package cm.iconprod.iconlab.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import cm.iconprod.iconlab.domain.enumeration.Role;

/**
 * A Tache.
 */
@Entity
@Table(name = "tache")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tache")
public class Tache implements Serializable {

private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "fichier_joint")
    private byte[] fichierJoint;

    @Column(name = "fichier_joint_content_type")
    private String fichierJointContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;


    @Column(name = "fromt")
    private ZonedDateTime fromt;

    @Column(name = "tot")
    private ZonedDateTime tot;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "color")
    private String color;

    @Column(name = "data")
    private String data;

    @Column(name = "movable")
    private Boolean movable;

    @Column(name = "progress")
    private Integer progress;

    @Column(name = "lct")
    private LocalDate lct;

    @Column(name = "est")
    private LocalDate est;

    @ManyToOne
    private Projet projet;

    @OneToMany(mappedBy = "tache")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PointAvancement> pointAvancements = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFichierJoint() {
        return fichierJoint;
    }

    public void setFichierJoint(byte[] fichierJoint) {
        this.fichierJoint = fichierJoint;
    }

    public String getFichierJointContentType() {
        return fichierJointContentType;
    }

    public void setFichierJointContentType(String fichierJointContentType) {
        this.fichierJointContentType = fichierJointContentType;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ZonedDateTime getTot() {
        return tot;
    }

    public void setTot(ZonedDateTime to) {
        this.tot = to;
    }

    public ZonedDateTime getFromt() {
        return fromt;
    }

    public void setFromt(ZonedDateTime from) {
        this.fromt = from;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean isMovable() {
        return movable;
    }

    public void setMovable(Boolean movable) {
        this.movable = movable;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public LocalDate getLct() {
        return lct;
    }

    public void setLct(LocalDate lct) {
        this.lct = lct;
    }

    public LocalDate getEst() {
        return est;
    }

    public void setEst(LocalDate est) {
        this.est = est;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public Set<PointAvancement> getPointAvancements() {
        return pointAvancements;
    }

    public void setPointAvancements(Set<PointAvancement> pointAvancements) {
        this.pointAvancements = pointAvancements;
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
        Tache tache = (Tache) o;
        if(tache.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tache.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tache{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", fichierJoint='" + fichierJoint + "'" +
            ", fichierJointContentType='" + fichierJointContentType + "'" +
            ", role='" + role + "'" +
            ", tot='" + tot + "'" +
            ", fromt='" + fromt + "'" +
            ", actif='" + actif + "'" +
            ", color='" + color + "'" +
            ", data='" + data + "'" +
            ", movable='" + movable + "'" +
            ", progress='" + progress + "'" +
            ", lct='" + lct + "'" +
            ", est='" + est + "'" +
            '}';
    }

}
