/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemaeventosentradas;




import javax.swing.SwingUtilities;

public class SistemaEventosEntradas {
    public static void main(String[] args) {
        Sistema sistemaDeVentas = new Sistema();
        
        SwingUtilities.invokeLater(() -> {
            SistemaGUI ventanaPrincipal = new SistemaGUI(sistemaDeVentas);
            ventanaPrincipal.setVisible(true);
        });
    }
}
