package com.examen.inventario.consumer;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PedidoEvent {
    private Long          pedidoId;
    private String        productoId;
    private int           cantidad;
    private String        estado;
    private LocalDateTime timestamp;
}