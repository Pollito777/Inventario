package org.gerald.inventario.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Permiso.
 */
@Entity
@Table(name = "permiso")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Permiso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ver")
    private Boolean ver;

    @Column(name = "crear")
    private Boolean crear;

    @Column(name = "modificar")
    private Boolean modificar;

    @Column(name = "autorizar")
    private Boolean autorizar;

    @OneToOne
    @JoinColumn(unique = true)
    private Formulario formularios;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Rol rols;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public Boolean isCrear() {
        return crear;
    }

    public void setCrear(Boolean crear) {
        this.crear = crear;
    }

    public Boolean isModificar() {
        return modificar;
    }

    public void setModificar(Boolean modificar) {
        this.modificar = modificar;
    }

    public Boolean isAutorizar() {
        return autorizar;
    }

    public void setAutorizar(Boolean autorizar) {
        this.autorizar = autorizar;
    }

    public Formulario getFormularios() {
        return formularios;
    }

    public void setFormularios(Formulario formulario) {
        this.formularios = formulario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRols() {
        return rols;
    }

    public void setRols(Rol rol) {
        this.rols = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Permiso permiso = (Permiso) o;
        if(permiso.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, permiso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Permiso{" +
            "id=" + id +
            ", ver='" + ver + "'" +
            ", crear='" + crear + "'" +
            ", modificar='" + modificar + "'" +
            ", autorizar='" + autorizar + "'" +
            '}';
    }
}
