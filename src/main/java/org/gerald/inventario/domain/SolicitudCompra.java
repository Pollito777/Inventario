package org.gerald.inventario.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SolicitudCompra.
 */
@Entity
@Table(name = "solicitud_compra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SolicitudCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "numero_solicitud")
    private Integer numeroSolicitud;

    @Column(name = "justificacion")
    private String justificacion;

    @Column(name = "autoriza_id")
    private Integer autorizaId;

    @Column(name = "orden_id")
    private Integer ordenId;

    @NotNull
    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "fecha_autoriza")
    private LocalDate fechaAutoriza;

    @Column(name = "fecha_orden")
    private LocalDate fechaOrden;

    @ManyToOne
    private FlujoCompra flujoCompra;

    @OneToMany(mappedBy = "solicitudCompra")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LinSolicitud> linSolicituds = new HashSet<>();

    @ManyToOne
    private Pedido pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(Integer numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public Integer getAutorizaId() {
        return autorizaId;
    }

    public void setAutorizaId(Integer autorizaId) {
        this.autorizaId = autorizaId;
    }

    public Integer getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Integer ordenId) {
        this.ordenId = ordenId;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDate getFechaAutoriza() {
        return fechaAutoriza;
    }

    public void setFechaAutoriza(LocalDate fechaAutoriza) {
        this.fechaAutoriza = fechaAutoriza;
    }

    public LocalDate getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public FlujoCompra getFlujoCompra() {
        return flujoCompra;
    }

    public void setFlujoCompra(FlujoCompra flujoCompra) {
        this.flujoCompra = flujoCompra;
    }

    public Set<LinSolicitud> getLinSolicituds() {
        return linSolicituds;
    }

    public void setLinSolicituds(Set<LinSolicitud> linSolicituds) {
        this.linSolicituds = linSolicituds;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SolicitudCompra solicitudCompra = (SolicitudCompra) o;
        if(solicitudCompra.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, solicitudCompra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SolicitudCompra{" +
            "id=" + id +
            ", numeroSolicitud='" + numeroSolicitud + "'" +
            ", justificacion='" + justificacion + "'" +
            ", autorizaId='" + autorizaId + "'" +
            ", ordenId='" + ordenId + "'" +
            ", fechaSolicitud='" + fechaSolicitud + "'" +
            ", fechaAutoriza='" + fechaAutoriza + "'" +
            ", fechaOrden='" + fechaOrden + "'" +
            '}';
    }
}
