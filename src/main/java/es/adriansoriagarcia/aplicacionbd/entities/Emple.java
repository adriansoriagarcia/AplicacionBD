/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.adriansoriagarcia.aplicacionbd.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "EMPLE")
@NamedQueries({
    @NamedQuery(name = "Emple.findAll", query = "SELECT e FROM Emple e"),
    @NamedQuery(name = "Emple.findByEmpNo", query = "SELECT e FROM Emple e WHERE e.empNo = :empNo"),
    @NamedQuery(name = "Emple.findByNombre", query = "SELECT e FROM Emple e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Emple.findByApellido", query = "SELECT e FROM Emple e WHERE e.apellido = :apellido"),
    @NamedQuery(name = "Emple.findByOficio", query = "SELECT e FROM Emple e WHERE e.oficio = :oficio"),
    @NamedQuery(name = "Emple.findByDir", query = "SELECT e FROM Emple e WHERE e.dir = :dir"),
    @NamedQuery(name = "Emple.findByFechaAlt", query = "SELECT e FROM Emple e WHERE e.fechaAlt = :fechaAlt"),
    @NamedQuery(name = "Emple.findByNumHijos", query = "SELECT e FROM Emple e WHERE e.numHijos = :numHijos"),
    @NamedQuery(name = "Emple.findByEstadoCivil", query = "SELECT e FROM Emple e WHERE e.estadoCivil = :estadoCivil"),
    @NamedQuery(name = "Emple.findBySalario", query = "SELECT e FROM Emple e WHERE e.salario = :salario"),
    @NamedQuery(name = "Emple.findByComision", query = "SELECT e FROM Emple e WHERE e.comision = :comision"),
    @NamedQuery(name = "Emple.findByBaja", query = "SELECT e FROM Emple e WHERE e.baja = :baja"),
    @NamedQuery(name = "Emple.findByImagen", query = "SELECT e FROM Emple e WHERE e.imagen = :imagen")})
public class Emple implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "EMP_NO")
    private Short empNo;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "OFICIO")
    private String oficio;
    @Column(name = "DIR")
    private Short dir;
    @Column(name = "FECHA_ALT")
    @Temporal(TemporalType.DATE)
    private Date fechaAlt;
    @Column(name = "NUM_HIJOS")
    private Short numHijos;
    @Column(name = "ESTADO_CIVIL")
    private Character estadoCivil;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SALARIO")
    private BigDecimal salario;
    @Column(name = "COMISION")
    private Integer comision;
    @Column(name = "BAJA")
    private Boolean baja;
    @Column(name = "IMAGEN")
    private String imagen;
    @JoinColumn(name = "DEPT_NO", referencedColumnName = "DEPT_NO")
    @ManyToOne
    private Depart deptNo;

    public Emple() {
    }

    public Emple(Short empNo) {
        this.empNo = empNo;
    }

    public Emple(Short empNo, String nombre) {
        this.empNo = empNo;
        this.nombre = nombre;
    }

    public Short getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Short empNo) {
        this.empNo = empNo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public Short getDir() {
        return dir;
    }

    public void setDir(Short dir) {
        this.dir = dir;
    }

    public Date getFechaAlt() {
        return fechaAlt;
    }

    public void setFechaAlt(Date fechaAlt) {
        this.fechaAlt = fechaAlt;
    }

    public Short getNumHijos() {
        return numHijos;
    }

    public void setNumHijos(Short numHijos) {
        this.numHijos = numHijos;
    }

    public Character getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(Character estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Integer getComision() {
        return comision;
    }

    public void setComision(Integer comision) {
        this.comision = comision;
    }

    public Boolean getBaja() {
        return baja;
    }

    public void setBaja(Boolean baja) {
        this.baja = baja;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Depart getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Depart deptNo) {
        this.deptNo = deptNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empNo != null ? empNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emple)) {
            return false;
        }
        Emple other = (Emple) object;
        if ((this.empNo == null && other.empNo != null) || (this.empNo != null && !this.empNo.equals(other.empNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.adriansoriagarcia.aplicacionbd.entities.Emple[ empNo=" + empNo + " ]";
    }
    
}
