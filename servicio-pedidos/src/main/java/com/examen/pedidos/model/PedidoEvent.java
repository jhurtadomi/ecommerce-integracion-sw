package com.examen.pedidos.model;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PedidoEvent {

    private Long          pedidoId;
    private String        productoId;
    private int           cantidad;
    private String        estado;
    private LocalDateTime timestamp;

    public static PedidoEvent desde(Pedido pedido) {
        return PedidoEvent.builder()
                .pedidoId(pedido.getId())
                .productoId(pedido.getProductoId())
                .cantidad(pedido.getCantidad())
                .estado("CREADO")
                .timestamp(LocalDateTime.now())
                .build();
    }
}