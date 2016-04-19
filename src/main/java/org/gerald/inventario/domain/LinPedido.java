package org.gerald.inventario.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LinPedido.
 */
@Entity
@Table(name = "lin_pedido")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LinPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cantidad_pedido")
    private Integer cantidadPedido;

    @Column(name = "cantidad_autorizada")
    private Integer cantidadAutorizada;

    @Column(name = "cantidad_despachada")
    private Integer cantidadDespachada;

    @ManyToOne
    private Pedido pedido;

    @ManyToOne
    private Material material;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadPedido() {
        return cantidadPedido;
    }

    public void setCantidadPedido(Integer cantidadPedido) {
        this.cantidadPedido = cantidadPedido;
    }

    public Integer getCantidadAutorizada() {
        return cantidadAutorizada;
    }

    public void setCantidadAutorizada(Integer cantidadAutorizada) {
        this.cantidadAutorizada = cantidadAutorizada;
    }

    public Integer getCantidadDespachada() {
        return cantidadDespachada;
    }

    public void setCantidadDespachada(Integer cantidadDespachada) {
        this.cantidadDespachada = cantidadDespachada;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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
        LinPedido linPedido = (LinPedido) o;
        if(linPedido.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, linPedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LinPedido{" +
            "id=" + id +
            ", cantidadPedido='" + cantidadPedido + "'" +
            ", cantidadAutorizada='" + cantidadAutorizada + "'" +
            ", cantidadDespachada='" + cantidadDespachada + "'" +
            '}';
    }
}
