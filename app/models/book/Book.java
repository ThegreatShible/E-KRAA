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
import java.util.Date;
import java.util.List;

/**
 * @author nabih
 */
@Entity
@Table(name = "book")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b")
        , @NamedQuery(name = "Book.findByIdbook", query = "SELECT b FROM Book b WHERE b.idbook = :idbook")
        , @NamedQuery(name = "Book.findByContent", query = "SELECT b FROM Book b WHERE b.content = :content")
        , @NamedQuery(name = "Book.findByLastmodifdate", query = "SELECT b FROM Book b WHERE b.lastmodifdate = :lastmodifdate")
        , @NamedQuery(name = "Book.findByLanguage", query = "SELECT b FROM Book b WHERE b.language = :language")
        , @NamedQuery(name = "Book.findByDifficulty", query = "SELECT b FROM Book b WHERE b.difficulty = :difficulty")})
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbook")
    private Integer idbook = 0;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "content")
    private String content = "";
    @Basic(optional = false)
    @NotNull
    @Column(name = "lastmodifdate")
    @Temporal(TemporalType.DATE)
    private Date lastmodifdate = new Date();
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "language")
    private String language = "";
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "difficulty")
    private String difficulty = "";
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<Categorie> categorieList = new ArrayList();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<Questions> questionsList = new ArrayList();

    public Book() {
    }

    public Book(String content, Date lastmodifdate, String language, String difficulty, List<Categorie> categorieList, List<Questions> questionsList) {
        this.content = content;
        this.lastmodifdate = lastmodifdate;
        this.language = language;
        this.difficulty = difficulty;
        this.categorieList = categorieList;
        this.questionsList = questionsList;
    }

    public Book(Integer idbook) {
        this.idbook = idbook;
    }

    public Book(Integer idbook, String content, Date lastmodifdate, String language, String difficulty) {
        this.idbook = idbook;
        this.content = content;
        this.lastmodifdate = lastmodifdate;
        this.language = language;
        this.difficulty = difficulty;
    }

    public Integer getIdbook() {
        return idbook;
    }

    public void setIdbook(Integer idbook) {
        this.idbook = idbook;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getLastmodifdate() {
        return lastmodifdate;
    }

    public void setLastmodifdate(Date lastmodifdate) {
        this.lastmodifdate = lastmodifdate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @XmlTransient
    public List<Categorie> getCategorieList() {
        return categorieList;
    }

    public void setCategorieList(List<Categorie> categorieList) {
        this.categorieList = categorieList;
    }

    @XmlTransient
    public List<Questions> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(List<Questions> questionsList) {
        this.questionsList = questionsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbook != null ? idbook.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.idbook == null && other.idbook != null) || (this.idbook != null && !this.idbook.equals(other.idbook))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.Book[ idbook=" + idbook + " ]";
    }

}
