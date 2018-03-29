/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author nabih
 */
@Entity
@Table(name = "test")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Test.findAll", query = "SELECT t FROM Test t")
        , @NamedQuery(name = "Test.findByName", query = "SELECT t FROM Test t WHERE t.name = :name")})
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "name")
    private String name;

    public Test() {
    }

    public Test(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Test)) {
            return false;
        }
        Test other = (Test) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistance.Test[ name=" + name + " ]";
    }

}
