/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.book;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author nabih
 */
@Entity
@Table(name = "categorie")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Categorie.findAll", query = "SELECT c FROM Categorie c")
        , @NamedQuery(name = "Categorie.findByIdbook", query = "SELECT c FROM Categorie c WHERE c.categoriePK.idbook = :idbook")
        , @NamedQuery(name = "Categorie.findByCategorie", query = "SELECT c FROM Categorie c WHERE c.categoriePK.categorie = :categorie")})
public class Categorie implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CategoriePK categoriePK = new CategoriePK();
    @JoinColumn(name = "idbook", referencedColumnName = "idbook", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Book book = new Book();

    public Categorie() {
    }

    public Categorie(CategoriePK categoriePK) {
        this.categoriePK = categoriePK;
    }

    public Categorie(int idbook, String categorie) {
        this.categoriePK = new CategoriePK(idbook, categorie);
    }

    public CategoriePK getCategoriePK() {
        return categoriePK;
    }

    public void setCategoriePK(CategoriePK categoriePK) {
        this.categoriePK = categoriePK;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoriePK != null ? categoriePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categorie)) {
            return false;
        }
        Categorie other = (Categorie) object;
        if ((this.categoriePK == null && other.categoriePK != null) || (this.categoriePK != null && !this.categoriePK.equals(other.categoriePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.Categorie[ categoriePK=" + categoriePK + " ]";
    }

}
