/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author stefan
 */
@Entity
@Table(name = "review")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Review.findAll", query = "SELECT r FROM Review r"),
    @NamedQuery(name = "Review.findByReviewID", query = "SELECT r FROM Review r WHERE r.reviewID = :reviewID"),
//    @NamedQuery(name = "Review.getMarkStats", query = "SELECT r.ocena, count(*) FROM Review r GROUP BY r.ocena"),
    @NamedQuery(name = "Review.findByDatum", query = "SELECT r FROM Review r WHERE r.datum = :datum"),
    @NamedQuery(name = "Review.findByOcena", query = "SELECT r FROM Review r WHERE r.ocena = :ocena")})
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "reviewID")
    private Integer reviewID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum")
    private int datum;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 16777215)
    @Column(name = "sadrzaj")
    private String sadrzaj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ocena")
    private int ocena;
    @JoinColumn(name = "authorID", referencedColumnName = "authorID")
    @ManyToOne
    private Author authorID;
    @JoinColumn(name = "filmID", referencedColumnName = "filmID")
    @ManyToOne
    private Film filmID;

    public Review() {
    }

    public Review(Integer reviewID) {
        this.reviewID = reviewID;
    }

    public Review(Integer reviewID, int datum, String sadrzaj, int ocena) {
        this.reviewID = reviewID;
        this.datum = datum;
        this.sadrzaj = sadrzaj;
        this.ocena = ocena;
    }

    public Integer getReviewID() {
        return reviewID;
    }

    public void setReviewID(Integer reviewID) {
        this.reviewID = reviewID;
    }

    public int getDatum() {
        return datum;
    }

    public void setDatum(int datum) {
        this.datum = datum;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public Author getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Author authorID) {
        this.authorID = authorID;
    }

    public Film getFilmID() {
        return filmID;
    }

    public void setFilmID(Film filmID) {
        this.filmID = filmID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewID != null ? reviewID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Review)) {
            return false;
        }
        Review other = (Review) object;
        if ((this.reviewID == null && other.reviewID != null) || (this.reviewID != null && !this.reviewID.equals(other.reviewID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rs.filmoteka.domain.Review[ reviewID=" + reviewID + " ]";
    }
    
}
