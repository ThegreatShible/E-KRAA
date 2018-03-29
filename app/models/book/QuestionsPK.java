/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.book;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author nabih
 */
@Embeddable
public class QuestionsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idbook")
    private int idbook = 0;
    @Basic(optional = false)
    @Column(name = "numquestion")
    private short numquestion = 0;

    public QuestionsPK() {
    }

    public QuestionsPK(int idbook, short numquestion) {
        this.idbook = idbook;
        this.numquestion = numquestion;
    }

    public int getIdbook() {
        return idbook;
    }

    public void setIdbook(int idbook) {
        this.idbook = idbook;
    }

    public short getNumquestion() {
        return numquestion;
    }

    public void setNumquestion(short numquestion) {
        this.numquestion = numquestion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idbook;
        hash += (int) numquestion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionsPK)) {
            return false;
        }
        QuestionsPK other = (QuestionsPK) object;
        if (this.idbook != other.idbook) {
            return false;
        }
        if (this.numquestion != other.numquestion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.QuestionsPK[ idbook=" + idbook + ", numquestion=" + numquestion + " ]";
    }

}
