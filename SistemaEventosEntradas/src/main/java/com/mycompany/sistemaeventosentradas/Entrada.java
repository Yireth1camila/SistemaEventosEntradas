/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;


import java.util.UUID;

public class Entrada {
    private final Cliente cliente;
    private final Vendedor vendedor;
    private final Evento evento;
    private final Nivel nivel;
    private final double precio;
    private final String metodoPago;
    private final String cuentaOrigen;
    private final String fraseCancion;
    private final String codigoVerificacion;

    public Entrada(Cliente cliente, Vendedor vendedor, Evento evento, Nivel nivel, 
                   double precio, String metodoPago, String cuentaOrigen, String fraseCancion) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.evento = evento;
        this.nivel = nivel;
        this.precio = precio;
        this.metodoPago = metodoPago;
        this.cuentaOrigen = cuentaOrigen;
        this.fraseCancion = fraseCancion;
        this.codigoVerificacion = generarCodigoUnico();
    }

    private String generarCodigoUnico() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Genera el PDF de la boleta de acceso, pasando el nombre del administrador 
     * para su impresión.
     * @param nombreAdmin Nombre del administrador que emite el reporte.
     */
    public void generarBoletaPDF(String nombreAdmin) {
        PDFGenerator generator = new PDFGenerator();
        generator.generarPDFEntrada(this, nombreAdmin); 
    }

    // Getters
    public Cliente getCliente() { return cliente; }
    public Vendedor getVendedor() { return vendedor; }
    public Evento getEvento() { return evento; }
    public Nivel getNivel() { return nivel; }
    public double getPrecio() { return precio; }
    public String getMetodoPago() { return metodoPago; }
    public String getCuentaOrigen() { return cuentaOrigen; }
    public String getFraseCancion() { return fraseCancion; }
    public String getCodigoVerificacion() { return codigoVerificacion; }
}