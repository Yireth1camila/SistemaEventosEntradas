/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;



public enum Nivel {
    VIP("VIP", "#FFD700"), 
    PREMIUM("Premium", "#C0C0C0"), 
    GENERAL("General", "#CD7F32");

    private final String nombre;
    private final String colorHex;

    Nivel(String nombre, String colorHex) {
        this.nombre = nombre;
        this.colorHex = colorHex;
    }

    public String getNombre() { return nombre; }
    public String getColorHex() { return colorHex; }
}