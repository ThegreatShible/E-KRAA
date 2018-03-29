/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.book;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author nabih
 */
@Embeddable
public class CategoriePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idbook")
    private int idbook = 0;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "categorie")
    private String categorie = "";

    public CategoriePK() {
    }

    public CategoriePK(int idbook, String categorie) {
        this.idbook = idbook;
        this.categorie = categorie;
    }

    public int getIdbook() {
        return idbook;
    }

    public void setIdbook(int idbook) {
        this.idbook = idbook;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idbook;
        hash += (categorie != null ? categorie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriePK)) {
            return false;
        }
        CategoriePK other = (CategoriePK) object;
        if (this.idbook != other.idbook) {
            return false;
        }
        if ((this.categorie == null && other.categorie != null) || (this.categorie != null && !this.categorie.equals(other.categorie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.CategoriePK[ idbook=" + idbook + ", categorie=" + categorie + " ]";
    }

}
