/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;


import javax.swing.*;
import java.awt.*;

public class SistemaGUI extends JFrame {
    
    private final Sistema sistema;
    private final JPanel cardPanel;
    private final CardLayout cardLayout;

    public static final String PANEL_LOGIN = "Login";
    public static final String PANEL_ADMIN = "Admin";
    public static final String PANEL_CLIENTE = "Cliente";
    public static final String PANEL_VENDEDOR = "Vendedor";

    public SistemaGUI(Sistema sistema) {
        this.sistema = sistema; // <-- CLAVE: Guardar la referencia al Sistema
        setTitle("Sistema de Eventos (Final)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null); 
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(crearPanelLogin(), PANEL_LOGIN); 
        cardPanel.add(crearPanelAdmin(), PANEL_ADMIN);
        cardPanel.add(crearPanelCliente(), PANEL_CLIENTE);
        cardPanel.add(crearPanelVendedor(), PANEL_VENDEDOR);

        add(cardPanel);
        cardLayout.show(cardPanel, PANEL_LOGIN);
    }
    
    // El main es solo un lanzador, no se usa si lanzas desde SistemaEventosEntradas
    /*
    public static void main(String[] args) {
        Sistema sistema = new Sistema(); 
        SwingUtilities.invokeLater(() -> {
            SistemaGUI ventana = new SistemaGUI(sistema);
            ventana.setVisible(true);
        });
    }
    */

    public void mostrarPanel(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }

    private JPanel crearPanelNavegacion() {
        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnVolver = new JButton("⬅️ Volver");
        // CLAVE: Usar SistemaGUI.this para resolver el scope
        btnVolver.addActionListener(e -> SistemaGUI.this.mostrarPanel(PANEL_LOGIN));
        
        JButton btnSalir = new JButton("🚪 Salir");
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea salir de la aplicación?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        panelNav.add(btnVolver);
        panelNav.add(btnSalir);
        return panelNav;
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelBotones.add(new JLabel("Seleccione su Rol:", SwingConstants.CENTER));
        
        JButton btnAdmin = new JButton("1. Administrador");
        JButton btnCliente = new JButton("2. Cliente");
        JButton btnVendedor = new JButton("3. Vendedor");
        
        btnAdmin.addActionListener(e -> mostrarPanel(PANEL_ADMIN));
        btnCliente.addActionListener(e -> mostrarPanel(PANEL_CLIENTE));
        btnVendedor.addActionListener(e -> mostrarPanel(PANEL_VENDEDOR));

        panelBotones.add(btnAdmin);
        panelBotones.add(btnCliente);
        panelBotones.add(btnVendedor);
        
        panel.add(panelBotones);
        return panel;
    }
    
    private JPanel crearPanelAdmin() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JTextField txtCorreo = new JTextField(15);
        JPasswordField txtClave = new JPasswordField(15);
        JButton btnLogin = new JButton("Ingresar como Admin");
        
        JPanel panelLogin = new JPanel(new FlowLayout());
        panelLogin.add(new JLabel("Correo:"));
        panelLogin.add(txtCorreo);
        panelLogin.add(new JLabel("Clave:"));
        panelLogin.add(txtClave);
        panelLogin.add(btnLogin);
        
        panelContenido.add(new JLabel("<html><h2 style='text-align:center;'>Menú de Administrador</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        panelContenido.add(panelLogin, BorderLayout.SOUTH);

        JPanel panelFunciones = new JPanel(new GridLayout(5, 1, 10, 10));
        
        JButton btnCrearEvento = new JButton("1. Crear Evento");
        JButton btnEliminarEvento = new JButton("2. Eliminar Evento");
        JButton btnVerAuditoria = new JButton("3. Ver Auditoría");
        JButton btnGenerarPDF = new JButton("4. Generar Reporte PDF");
        JButton btnAsignarGrupo = new JButton("5. Asignar Grupo Vendedor");
        
        panelFunciones.add(btnCrearEvento);
        panelFunciones.add(btnEliminarEvento);
        panelFunciones.add(btnVerAuditoria);
        panelFunciones.add(btnGenerarPDF);
        panelFunciones.add(btnAsignarGrupo);
        
        for (Component comp : panelFunciones.getComponents()) comp.setEnabled(false);

        // Lógica de Login
        btnLogin.addActionListener(e -> {
            String correo = txtCorreo.getText();
            String clave = new String(txtClave.getPassword());
            if (sistema.loginAdmin(correo, clave)) { // Llama al método del objeto 'sistema'
                JOptionPane.showMessageDialog(this, "✅ Login exitoso.");
                for (Component comp : panelFunciones.getComponents()) comp.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Credenciales incorrectas.");
                for (Component comp : panelFunciones.getComponents()) comp.setEnabled(false);
            }
        });

        // Lógica de funciones: Todas llaman a los métodos del objeto 'sistema'
        btnCrearEvento.addActionListener(e -> sistema.crearEventoGUI(SistemaGUI.this));
        btnEliminarEvento.addActionListener(e -> sistema.eliminarEventoGUI(SistemaGUI.this));
        
        btnVerAuditoria.addActionListener(e -> {
            String auditoriaTexto = sistema.obtenerAuditoriaGUI();
            JTextArea textArea = new JTextArea(auditoriaTexto);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(SistemaGUI.this, scrollPane, "📊 Registro de Auditoría de Ventas", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnGenerarPDF.addActionListener(e -> {
            try {
                // Asumo que PDFGenerator existe y tiene un constructor sin argumentos
                new PDFGenerator().generarPDFRegistro(sistema.getRegistro()); 
                JOptionPane.showMessageDialog(SistemaGUI.this, "✅ Reporte de Ventas PDF generado.", "PDF Generado", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(SistemaGUI.this, "❌ Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnAsignarGrupo.addActionListener(e -> sistema.asignarGrupoGUI(SistemaGUI.this));

        panelContenido.add(panelFunciones, BorderLayout.CENTER);
        
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(crearPanelNavegacion(), BorderLayout.SOUTH); 
        
        return panelPrincipal;
    }

    private JPanel crearPanelCliente() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelContenido = new JPanel(new GridBagLayout());
        
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelBotones.add(new JLabel("<html><h2 style='text-align:center;'>Menú de Cliente</h2></html>", SwingConstants.CENTER));

        JButton btnRegistrarse = new JButton("1. Registrarse");
        JButton btnComprar = new JButton("2. Comprar Entrada");
        
        btnRegistrarse.addActionListener(e -> sistema.registrarClienteGUI(SistemaGUI.this));
        btnComprar.addActionListener(e -> sistema.comprarEntradaGUI(SistemaGUI.this));

        panelBotones.add(btnRegistrarse);
        panelBotones.add(btnComprar);
        
        panelContenido.add(panelBotones);
        
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(crearPanelNavegacion(), BorderLayout.SOUTH); 

        return panelPrincipal;
    }
    
    private JPanel crearPanelVendedor() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelContenido = new JPanel(new GridBagLayout());
        
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelBotones.add(new JLabel("<html><h2 style='text-align:center;'>Menú de Vendedor</h2></html>", SwingConstants.CENTER));

        JButton btnRegistrarVendedor = new JButton("1. Registrar Vendedor");
        JButton btnLogin = new JButton("2. Iniciar Sesión");
        
        btnLogin.addActionListener(e -> sistema.loginVendedorGUI(SistemaGUI.this));
        btnRegistrarVendedor.addActionListener(e -> sistema.registrarVendedorGUI(SistemaGUI.this));

        panelBotones.add(btnRegistrarVendedor);
        panelBotones.add(btnLogin);
        
        panelContenido.add(panelBotones);
        
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(crearPanelNavegacion(), BorderLayout.SOUTH); 
        
        return panelPrincipal;
    }
}