/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;


import java.util.ArrayList;
import java.util.List;

public class RegistroVentas {
    
    // Lista principal de todas las entradas vendidas.
    private final List<Entrada> entradas = new ArrayList<>();
    
    // Lista de logs de auditoría simplificados (Strings) para la GUI.
    private final ArrayList<String> auditoria = new ArrayList<>(); 

    /**
     * Registra una nueva venta de entrada y añade un registro de auditoría.
     * @param entrada El objeto Entrada recién creado.
     */
    public void registrar(Entrada entrada) {
        entradas.add(entrada);
        
        // Crear un log detallado para la auditoría
        String log = String.format("Venta ID %d: Cliente %s | Vendedor %s | Evento %s | Nivel %s | Precio %.2f",
                entradas.size(), // Usamos el tamaño como un ID simple
                entrada.getCliente().getNombre(), 
                entrada.getVendedor().getNombre(),
                entrada.getEvento().getNombre(),
                entrada.getNivel().getNombre(),
                entrada.getPrecio());
                
        auditoria.add(log);
    }
    
    /**
     * Obtiene una copia inmutable de la lista de entradas vendidas.
     * @return Lista de entradas.
     */
    public List<Entrada> getEntradas() {
        return new ArrayList<>(entradas); 
    }
    
    /**
     * Retorna la auditoría de ventas como una sola cadena, lista para ser mostrada en la GUI.
     * Es el método que usa Sistema.obtenerAuditoriaGUI().
     * @return Cadena con todos los logs de auditoría, separados por saltos de línea.
     */
    public String obtenerAuditoria() {
        if (auditoria.isEmpty()) {
            return "No hay registros de auditoría de ventas.";
        }
        // Une todos los logs en una sola cadena para el JTextArea
        return String.join("\n", auditoria);
    }
    
    /**
     * Método auxiliar para imprimir la auditoría en la consola (opcional).
     */
    public void mostrarAuditoria() {
        System.out.println("\n--- AUDITORÍA DE VENTAS ---");
        System.out.println(obtenerAuditoria());
        System.out.println("---------------------------");
    }
}