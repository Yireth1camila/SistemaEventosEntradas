/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;

public class Evento {
    private final String nombre;
    private final String artista;
    private final String lugar;
    private final String fecha;
    private final double precioVip;
    private final double precioPremium;
    private final double precioGeneral;

    public Evento(String nombre, String artista, String lugar, String fecha, 
                  double precioVip, double precioPremium, double precioGeneral) {
        this.nombre = nombre;
        this.artista = artista;
        this.lugar = lugar;
        this.fecha = fecha;
        this.precioVip = precioVip;
        this.precioPremium = precioPremium;
        this.precioGeneral = precioGeneral;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getArtista() { return artista; }
    public String getLugar() { return lugar; }
    public String getFecha() { return fecha; }
    public double getPrecioVip() { return precioVip; }
    public double getPrecioPremium() { return precioPremium; }
    public double getPrecioGeneral() { return precioGeneral; }
    
    @Override
    public String toString() {
        return " (" + artista + ", " + fecha + ")";
    }
}