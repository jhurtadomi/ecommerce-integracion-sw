package com.examen.inventario.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class InventarioService {

    private final Map<String, Integer> stock = new ConcurrentHashMap<>(Map.of(
            "PROD-001", 100,
            "PROD-002", 50,
            "PROD-003", 200
    ));

    public void reducirStock(String productoId, int cantidad) {
        int disponible = stock.getOrDefault(productoId, 0);
        log.info("[INVENTARIO] Stock actual de '{}': {} | Solicitado: {}", productoId, disponible, cantidad);

        if (disponible < cantidad) {
            throw new IllegalStateException("Stock insuficiente para: " + productoId);
        }

        stock.put(productoId, disponible - cantidad);
        log.info("[INVENTARIO] ✅ Stock actualizado: {} → {} unidades", disponible, disponible - cantidad);
    }
}