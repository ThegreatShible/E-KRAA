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
public class AnswerPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idbook")
    private int idbook = 0;
    @Basic(optional = false)
    @Column(name = "idquestion")
    private short idquestion = 0;
    @Basic(optional = false)
    @Column(name = "idanswer")
    private short idanswer = 0;

    public AnswerPK() {
    }

    public AnswerPK(int idbook, short idquestion, short idanswer) {
        this.idbook = idbook;
        this.idquestion = idquestion;
        this.idanswer = idanswer;
    }

    public int getIdbook() {
        return idbook;
    }

    public void setIdbook(int idbook) {
        this.idbook = idbook;
    }

    public short getIdquestion() {
        return idquestion;
    }

    public void setIdquestion(short idquestion) {
        this.idquestion = idquestion;
    }

    public short getIdanswer() {
        return idanswer;
    }

    public void setIdanswer(short idanswer) {
        this.idanswer = idanswer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idbook;
        hash += (int) idquestion;
        hash += (int) idanswer;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnswerPK)) {
            return false;
        }
        AnswerPK other = (AnswerPK) object;
        if (this.idbook != other.idbook) {
            return false;
        }
        if (this.idquestion != other.idquestion) {
            return false;
        }
        if (this.idanswer != other.idanswer) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.AnswerPK[ idbook=" + idbook + ", idquestion=" + idquestion + ", idanswer=" + idanswer + " ]";
    }

}
