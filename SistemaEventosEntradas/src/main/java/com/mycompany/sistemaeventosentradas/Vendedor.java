/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;

public class Vendedor extends Usuario {
    private String grupo;
    private int totalVentas;

    public Vendedor(String nombre, String correo, String password) {
        super(nombre, correo, password);
        this.grupo = "Sin Asignar";
        this.totalVentas = 0;
    }

    public void asignarGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void registrarVenta() {
        this.totalVentas++;
    }

    // Getters
    public String getGrupo() { return grupo; }
    public int getTotalVentas() { return totalVentas; }
}