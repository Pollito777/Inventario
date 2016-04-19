package org.gerald.inventario.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LinSolicitud.
 */
@Entity
@Table(name = "lin_solicitud")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LinSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cantidad_solicitada")
    private Integer cantidadSolicitada;

    @Column(name = "cantidad_autorizada")
    private Integer cantidadAutorizada;

    @Column(name = "cantidad_comprada")
    private Integer cantidadComprada;

    @NotNull
    @Column(name = "precio_unitario", nullable = false)
    private Float precioUnitario;

    @ManyToOne
    private SolicitudCompra solicitudCompra;

    @ManyToOne
    private Material material;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Integer cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Integer getCantidadAutorizada() {
        return cantidadAutorizada;
    }

    public void setCantidadAutorizada(Integer cantidadAutorizada) {
        this.cantidadAutorizada = cantidadAutorizada;
    }

    public Integer getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(Integer cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public SolicitudCompra getSolicitudCompra() {
        return solicitudCompra;
    }

    public void setSolicitudCompra(SolicitudCompra solicitudCompra) {
        this.solicitudCompra = solicitudCompra;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinSolicitud linSolicitud = (LinSolicitud) o;
        if(linSolicitud.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, linSolicitud.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LinSolicitud{" +
            "id=" + id +
            ", cantidadSolicitada='" + cantidadSolicitada + "'" +
            ", cantidadAutorizada='" + cantidadAutorizada + "'" +
            ", cantidadComprada='" + cantidadComprada + "'" +
            ", precioUnitario='" + precioUnitario + "'" +
            '}';
    }
}
