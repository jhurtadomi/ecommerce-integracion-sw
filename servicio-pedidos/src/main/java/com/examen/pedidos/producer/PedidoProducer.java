package com.examen.pedidos.producer;

import com.examen.pedidos.config.RabbitMQConfig;
import com.examen.pedidos.model.PedidoEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoProducer {

    private final RabbitTemplate rabbitTemplate;

    @CircuitBreaker(name = "inventarioService", fallbackMethod = "fallbackPublicar")
    public void publicarPedidoCreado(PedidoEvent event) {
        log.info("[PRODUCER] Publicando evento para pedido ID={} | producto={} | cantidad={}",
                event.getPedidoId(), event.getProductoId(), event.getCantidad());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        log.info("[PRODUCER] ✅ Evento publicado exitosamente en '{}'", RabbitMQConfig.EXCHANGE_NAME);
    }

    public void fallbackPublicar(PedidoEvent event, Throwable ex) {
        log.error("[CIRCUIT BREAKER] ⚠️ Circuito ABIERTO. No se pudo publicar evento para pedido ID={}. Causa: {}",
                event.getPedidoId(), ex.getMessage());
        log.warn("[CIRCUIT BREAKER] Pedido ID={} guardado en BD pero inventario no notificado.",
                event.getPedidoId());
    }
}