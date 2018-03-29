/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.book;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author nabih
 */
@Entity
@Table(name = "answer")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a")
        , @NamedQuery(name = "Answer.findByIdbook", query = "SELECT a FROM Answer a WHERE a.answerPK.idbook = :idbook")
        , @NamedQuery(name = "Answer.findByIdquestion", query = "SELECT a FROM Answer a WHERE a.answerPK.idquestion = :idquestion")
        , @NamedQuery(name = "Answer.findByIdanswer", query = "SELECT a FROM Answer a WHERE a.answerPK.idanswer = :idanswer")
        , @NamedQuery(name = "Answer.findByContent", query = "SELECT a FROM Answer a WHERE a.content = :content")
        , @NamedQuery(name = "Answer.findByRight", query = "SELECT a FROM Answer a WHERE a.right = :right")})
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnswerPK answerPK = new AnswerPK();
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "content")
    private String content = "";
    @Basic(optional = false)
    @NotNull
    @Column(name = "right")
    private boolean right = false;
    @JoinColumns({
            @JoinColumn(name = "idbook", referencedColumnName = "idbook", insertable = false, updatable = false)
            , @JoinColumn(name = "idquestion", referencedColumnName = "numquestion", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Questions questions = new Questions();

    public Answer() {
    }

    public Answer(AnswerPK answerPK) {
        this.answerPK = answerPK;
    }

    public Answer(AnswerPK answerPK, String content, boolean right) {
        this.answerPK = answerPK;
        this.content = content;
        this.right = right;
    }

    public Answer(int idbook, short idquestion, short idanswer) {
        this.answerPK = new AnswerPK(idbook, idquestion, idanswer);
    }

    public AnswerPK getAnswerPK() {
        return answerPK;
    }

    public void setAnswerPK(AnswerPK answerPK) {
        this.answerPK = answerPK;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public Questions getQuestions() {
        return questions;
    }

    public void setQuestions(Questions questions) {
        this.questions = questions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (answerPK != null ? answerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Answer)) {
            return false;
        }
        Answer other = (Answer) object;
        if ((this.answerPK == null && other.answerPK != null) || (this.answerPK != null && !this.answerPK.equals(other.answerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.Answer[ answerPK=" + answerPK + " ]";
    }

}
