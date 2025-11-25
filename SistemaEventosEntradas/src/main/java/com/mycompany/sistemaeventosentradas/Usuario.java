/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;


public abstract class Usuario {
    protected String nombre;
    protected String correo;
    protected String password;

    public Usuario(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }

    public boolean validarPassword(String pass) {
        return this.password.equals(pass);
    }
}
