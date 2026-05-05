package com.examen.inventario.consumer;

import com.examen.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventarioConsumer {

    private final InventarioService inventarioService;

    @RabbitListener(queues = "cola.pedidos.creados")
    public void onPedidoCreado(PedidoEvent event) {
        log.info("[CONSUMER] ✉️ Evento recibido | pedidoId={} | producto={} | cantidad={}",
                event.getPedidoId(), event.getProductoId(), event.getCantidad());
        try {
            inventarioService.reducirStock(event.getProductoId(), event.getCantidad());
            log.info("[CONSUMER] ✅ Stock actualizado para '{}'", event.getProductoId());
        } catch (Exception ex) {
            log.error("[CONSUMER] ❌ Error: {}", ex.getMessage());
            throw ex;
        }
    }
}