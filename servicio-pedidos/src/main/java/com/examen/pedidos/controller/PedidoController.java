package com.examen.pedidos.controller;

import com.examen.pedidos.model.Pedido;
import com.examen.pedidos.model.PedidoEvent;
import com.examen.pedidos.producer.PedidoProducer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoProducer pedidoProducer;

    @PersistenceContext
    private EntityManager em;

    @PostMapping
    @Transactional
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        log.info("[CONTROLLER] Nuevo pedido: producto={} cantidad={}",
                pedido.getProductoId(), pedido.getCantidad());

        // 1. Persistir en BD
        em.persist(pedido);
        em.flush();

        // 2. Publicar evento con Circuit Breaker
        PedidoEvent evento = PedidoEvent.desde(pedido);
        pedidoProducer.publicarPedidoCreado(evento);

        log.info("[CONTROLLER] ✅ Pedido ID={} creado", pedido.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "pedidoId", pedido.getId(),
                "estado",   pedido.getEstado(),
                "mensaje",  "Pedido creado. Inventario se actualizará de forma asíncrona."
        ));
    }
}