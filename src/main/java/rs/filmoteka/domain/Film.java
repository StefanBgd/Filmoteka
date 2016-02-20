/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author stefan
 */
@Entity
@Table(name = "film")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Film.findAll", query = "SELECT f FROM Film f"),
    @NamedQuery(name = "Film.findByFilmID", query = "SELECT f FROM Film f WHERE f.filmID = :filmID"),
    @NamedQuery(name = "Film.findByTitle", query = "SELECT f FROM Film f WHERE f.title = :title"),
    @NamedQuery(name = "Film.findByYear", query = "SELECT f FROM Film f WHERE f.year = :year"),
    @NamedQuery(name = "Film.findByDirector", query = "SELECT f FROM Film f WHERE f.director = :director"),
    @NamedQuery(name = "Film.findByPlot", query = "SELECT f FROM Film f WHERE f.plot = :plot"),
    @NamedQuery(name = "Film.findByPoster", query = "SELECT f FROM Film f WHERE f.poster = :poster"),
    @NamedQuery(name = "Film.findByRating", query = "SELECT f FROM Film f WHERE f.rating = :rating")})
public class Film implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "filmID")
    private String filmID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Column(name = "year")
    private int year;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "director")
    private String director;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "plot")
    private String plot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "poster")
    private String poster;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rating")
    private String rating;
    @OneToMany(mappedBy = "filmID")
    private List<Review> reviewList;

    public Film() {
    }

    public Film(String filmID) {
        this.filmID = filmID;
    }

    public Film(String filmID, String title, int year, String director, String plot, String poster, String rating) {
        this.filmID = filmID;
        this.title = title;
        this.year = year;
        this.director = director;
        this.plot = plot;
        this.poster = poster;
        this.rating = rating;
    }

    public String getFilmID() {
        return filmID;
    }

    public void setFilmID(String filmID) {
        this.filmID = filmID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @XmlTransient
    @JsonIgnore
    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filmID != null ? filmID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Film)) {
            return false;
        }
        Film other = (Film) object;
        if ((this.filmID == null && other.filmID != null) || (this.filmID != null && !this.filmID.equals(other.filmID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rs.filmoteka.domain.Film[ filmID=" + filmID + " ]";
    }
    
}
