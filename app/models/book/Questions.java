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
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nabih
 */
@Entity
@Table(name = "questions")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Questions.findAll", query = "SELECT q FROM Questions q")
        , @NamedQuery(name = "Questions.findByIdbook", query = "SELECT q FROM Questions q WHERE q.questionsPK.idbook = :idbook")
        , @NamedQuery(name = "Questions.findByNumquestion", query = "SELECT q FROM Questions q WHERE q.questionsPK.numquestion = :numquestion")
        , @NamedQuery(name = "Questions.findByContent", query = "SELECT q FROM Questions q WHERE q.content = :content")
        , @NamedQuery(name = "Questions.findByMultiplechoices", query = "SELECT q FROM Questions q WHERE q.multiplechoices = :multiplechoices")})
public class Questions implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected QuestionsPK questionsPK = new QuestionsPK();
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "content")
    private String content = "";
    @Basic(optional = false)
    @NotNull
    @Column(name = "multiplechoices")
    private boolean multiplechoices = false;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questions")
    private List<Answer> answerList = new ArrayList<>();
    @JoinColumn(name = "idbook", referencedColumnName = "idbook", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Book book = new Book();

    public Questions() {
    }

    public Questions(QuestionsPK questionsPK) {
        this.questionsPK = questionsPK;
    }

    public Questions(QuestionsPK questionsPK, String content, boolean multiplechoices) {
        this.questionsPK = questionsPK;
        this.content = content;
        this.multiplechoices = multiplechoices;
    }

    public Questions(int idbook, short numquestion) {
        this.questionsPK = new QuestionsPK(idbook, numquestion);
    }

    public QuestionsPK getQuestionsPK() {
        return questionsPK;
    }

    public void setQuestionsPK(QuestionsPK questionsPK) {
        this.questionsPK = questionsPK;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getMultiplechoices() {
        return multiplechoices;
    }

    public void setMultiplechoices(boolean multiplechoices) {
        this.multiplechoices = multiplechoices;
    }

    @XmlTransient
    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
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
        hash += (questionsPK != null ? questionsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Questions)) {
            return false;
        }
        Questions other = (Questions) object;
        if ((this.questionsPK == null && other.questionsPK != null) || (this.questionsPK != null && !this.questionsPK.equals(other.questionsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.Questions[ questionsPK=" + questionsPK + " ]";
    }

}
