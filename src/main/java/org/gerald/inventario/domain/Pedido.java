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
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "numero_pedido")
    private Integer numeroPedido;

    @Column(name = "justificacion")
    private String justificacion;

    @Column(name = "autoriza_id")
    private Integer autorizaId;

    @NotNull
    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

    @Column(name = "fecha_autoriza")
    private LocalDate fechaAutoriza;

    @ManyToOne
    private Proyecto proyecto;

    @OneToMany(mappedBy = "pedido")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SolicitudCompra> solicitudCompras = new HashSet<>();

    @OneToMany(mappedBy = "pedido")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LinPedido> linPedidos = new HashSet<>();

    @ManyToOne
    private FlujoPedido flujoPedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
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

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDate getFechaAutoriza() {
        return fechaAutoriza;
    }

    public void setFechaAutoriza(LocalDate fechaAutoriza) {
        this.fechaAutoriza = fechaAutoriza;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Set<SolicitudCompra> getSolicitudCompras() {
        return solicitudCompras;
    }

    public void setSolicitudCompras(Set<SolicitudCompra> solicitudCompras) {
        this.solicitudCompras = solicitudCompras;
    }

    public Set<LinPedido> getLinPedidos() {
        return linPedidos;
    }

    public void setLinPedidos(Set<LinPedido> linPedidos) {
        this.linPedidos = linPedidos;
    }

    public FlujoPedido getFlujoPedido() {
        return flujoPedido;
    }

    public void setFlujoPedido(FlujoPedido flujoPedido) {
        this.flujoPedido = flujoPedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pedido pedido = (Pedido) o;
        if(pedido.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + id +
            ", numeroPedido='" + numeroPedido + "'" +
            ", justificacion='" + justificacion + "'" +
            ", autorizaId='" + autorizaId + "'" +
            ", fechaPedido='" + fechaPedido + "'" +
            ", fechaAutoriza='" + fechaAutoriza + "'" +
            '}';
    }
}
