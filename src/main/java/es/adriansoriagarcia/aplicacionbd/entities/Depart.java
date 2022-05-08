/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.adriansoriagarcia.aplicacionbd.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "DEPART")
@NamedQueries({
    @NamedQuery(name = "Depart.findAll", query = "SELECT d FROM Depart d"),
    @NamedQuery(name = "Depart.findByDeptNo", query = "SELECT d FROM Depart d WHERE d.deptNo = :deptNo"),
    @NamedQuery(name = "Depart.findByDnombre", query = "SELECT d FROM Depart d WHERE d.dnombre = :dnombre"),
    @NamedQuery(name = "Depart.findByLoc", query = "SELECT d FROM Depart d WHERE d.loc = :loc")})
public class Depart implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DEPT_NO")
    private Short deptNo;
    @Column(name = "DNOMBRE")
    private String dnombre;
    @Column(name = "LOC")
    private String loc;
    @OneToMany(mappedBy = "deptNo")
    private Collection<Emple> empleCollection;

    public Depart() {
    }

    public Depart(Short deptNo) {
        this.deptNo = deptNo;
    }

    public Short getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Short deptNo) {
        this.deptNo = deptNo;
    }

    public String getDnombre() {
        return dnombre;
    }

    public void setDnombre(String dnombre) {
        this.dnombre = dnombre;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Collection<Emple> getEmpleCollection() {
        return empleCollection;
    }

    public void setEmpleCollection(Collection<Emple> empleCollection) {
        this.empleCollection = empleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deptNo != null ? deptNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Depart)) {
            return false;
        }
        Depart other = (Depart) object;
        if ((this.deptNo == null && other.deptNo != null) || (this.deptNo != null && !this.deptNo.equals(other.deptNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.adriansoriagarcia.aplicacionbd.entities.Depart[ deptNo=" + deptNo + " ]";
    }
    
}
