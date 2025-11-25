/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;


import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.util.ArrayList;
import java.util.Optional; 
import java.util.Scanner; 

public class Sistema {

    private final ArrayList<Evento> eventos = new ArrayList<>();
    private final ArrayList<Cliente> clientes = new ArrayList<>();
    private final ArrayList<Vendedor> vendedores = new ArrayList<>();
    // Credenciales Admin: admin@mail.com / 123
    private final Administrador admin = new Administrador("Admin", "admin@mail.com", "123"); 
    private final RegistroVentas registro = new RegistroVentas();
    private final Scanner sc = new Scanner(System.in); 

    public Sistema() {
        // Datos de prueba para que las acciones funcionen inmediatamente
        vendedores.add(new Vendedor("Vendedor1", "v1@mail.com", "123"));
        clientes.add(new Cliente("ClienteEj", "c1@mail.com", "123"));
        eventos.add(new Evento("Concierto Pop", "Artista X", "Estadio", "01/01/2026", 150.0, 100.0, 50.0));
    }

    public RegistroVentas getRegistro() { return registro; }
    
    public String obtenerAuditoriaGUI() { 
        // Asume que RegistroVentas tiene este método que retorna String
        return registro.obtenerAuditoria();
    }
    
    public boolean loginAdmin(String correo, String clave) {
        return admin.getCorreo().equals(correo) && admin.validarPassword(clave);
    }
    
    // --- FUNCIONES ADMIN (Los parentesis (JFrame parent) son CRUCIALES) ---
    
    public void crearEventoGUI(JFrame parent) {
        // Lógica de JOptionPane para crear evento
        JTextField n = new JTextField(); JTextField a = new JTextField(); JTextField l = new JTextField();
        JTextField f = new JTextField(); JTextField pv = new JTextField(); JTextField pp = new JTextField();
        JTextField pg = new JTextField();
        Object[] message = {"Nombre:", n, "Artista:", a, "Lugar:", l, "Fecha (DD/MM/AAAA):", f, "Precio VIP:", pv, "Precio Premium:", pp, "Precio General:", pg};

        int option = JOptionPane.showConfirmDialog(parent, message, "Crear Nuevo Evento", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double precioVip = Double.parseDouble(pv.getText());
                double precioPremium = Double.parseDouble(pp.getText());
                double precioGeneral = Double.parseDouble(pg.getText());
                eventos.add(new Evento(n.getText(), a.getText(), l.getText(), f.getText(), precioVip, precioPremium, precioGeneral));
                JOptionPane.showMessageDialog(parent, "✅ Evento creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "❌ Error: Los precios deben ser valores numéricos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void eliminarEventoGUI(JFrame parent) {
        if (eventos.isEmpty()) { JOptionPane.showMessageDialog(parent, "No hay eventos para eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE); return; }
        String[] nombresEventos = eventos.stream().map(e -> e.getNombre() + " (" + e.getArtista() + ")").toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(parent, "Seleccione el evento a eliminar:", "Eliminar Evento", JOptionPane.QUESTION_MESSAGE, null, nombresEventos, nombresEventos[0]);
        if (seleccion != null) {
            String nombre = seleccion.substring(0, seleccion.indexOf('(')).trim();
            boolean eliminado = eventos.removeIf(e -> e.getNombre().equals(nombre));
            if (eliminado) { JOptionPane.showMessageDialog(parent, "✅ Evento " + nombre + " eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE); }
        }
    }
    
    public void asignarGrupoGUI(JFrame parent) {
        if (vendedores.isEmpty()) { JOptionPane.showMessageDialog(parent, "No hay vendedores.", "Información", JOptionPane.INFORMATION_MESSAGE); return; }
        String[] nombresVendedores = vendedores.stream().map(Vendedor::getNombre).toArray(String[]::new);
        String vendSeleccionado = (String) JOptionPane.showInputDialog(parent, "Seleccione Vendedor:", "Asignar Grupo", JOptionPane.QUESTION_MESSAGE, null, nombresVendedores, nombresVendedores[0]);
        if (vendSeleccionado != null) {
            String grupo = (String) JOptionPane.showInputDialog(parent, "Asignar Grupo (A/B):", "Grupo", JOptionPane.QUESTION_MESSAGE, null, new String[]{"A", "B"}, "A");
            if (grupo != null) {
                Optional<Vendedor> vOpt = vendedores.stream().filter(v -> v.getNombre().equals(vendSeleccionado)).findFirst();
                vOpt.ifPresent(v -> { v.asignarGrupo(grupo); JOptionPane.showMessageDialog(parent, "✅ Grupo " + grupo + " asignado a " + vendSeleccionado + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE); });
            }
        }
    }

    // --- FUNCIONES CLIENTE ---

    public void registrarClienteGUI(JFrame parent) {
        JTextField n = new JTextField(); JTextField c = new JTextField(); JPasswordField p = new JPasswordField();
        Object[] message = {"Nombre:", n, "Correo:", c, "Clave:", p};
        int option = JOptionPane.showConfirmDialog(parent, message, "Registro de Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String correo = c.getText();
            boolean existe = clientes.stream().anyMatch(cl -> cl.getCorreo().equalsIgnoreCase(correo));
            if (existe) { JOptionPane.showMessageDialog(parent, "Error: El correo ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            clientes.add(new Cliente(n.getText(), correo, new String(p.getPassword())));
            JOptionPane.showMessageDialog(parent, "✅ Registro de Cliente exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void comprarEntradaGUI(JFrame parent) {
        // Lógica completa de compra (se mantiene igual)
        if (eventos.isEmpty() || vendedores.isEmpty()) { JOptionPane.showMessageDialog(parent, "No hay eventos o vendedores disponibles.", "Información", JOptionPane.INFORMATION_MESSAGE); return; }
        
        JTextField c = new JTextField(); JPasswordField p = new JPasswordField();
        Object[] loginMsg = {"Correo:", c, "Clave:", p};
        int loginOption = JOptionPane.showConfirmDialog(parent, loginMsg, "Login Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (loginOption != JOptionPane.OK_OPTION) return;
        
        Optional<Cliente> clienteOpt = clientes.stream().filter(x -> x.getCorreo().equals(c.getText()) && x.validarPassword(new String(p.getPassword()))).findFirst();
        if (clienteOpt.isEmpty()) { JOptionPane.showMessageDialog(parent, "❌ Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE); return; }
        Cliente cliente = clienteOpt.get();

        String[] eventosOpciones = eventos.stream().map(e -> e.getNombre() + " - " + e.getArtista()).toArray(String[]::new);
        String eventoSeleccionadoStr = (String) JOptionPane.showInputDialog(parent, "Seleccione Evento:", "Comprar Entrada", JOptionPane.QUESTION_MESSAGE, null, eventosOpciones, eventosOpciones[0]);
        if (eventoSeleccionadoStr == null) return;
        Evento evento = eventos.stream().filter(e -> (e.getNombre() + " - " + e.getArtista()).equals(eventoSeleccionadoStr)).findFirst().get();

        String[] nivelesOpciones = {"VIP ($" + evento.getPrecioVip() + ")", "Premium ($" + evento.getPrecioPremium() + ")", "General ($" + evento.getPrecioGeneral() + ")"};
        String nivelSeleccionadoStr = (String) JOptionPane.showInputDialog(parent, "Seleccione Nivel:", "Nivel", JOptionPane.QUESTION_MESSAGE, null, nivelesOpciones, nivelesOpciones[0]);
        if (nivelSeleccionadoStr == null) return;
        
        Nivel nivel; double precio;
        if (nivelSeleccionadoStr.startsWith("VIP")) { nivel = Nivel.VIP; precio = evento.getPrecioVip(); }
        else if (nivelSeleccionadoStr.startsWith("Premium")) { nivel = Nivel.PREMIUM; precio = evento.getPrecioPremium(); }
        else { nivel = Nivel.GENERAL; precio = evento.getPrecioGeneral(); }

        String[] vendedoresOpciones = vendedores.stream().map(Vendedor::getNombre).toArray(String[]::new);
        String vendedorSeleccionadoStr = (String) JOptionPane.showInputDialog(parent, "Seleccione Vendedor:", "Vendedor", JOptionPane.QUESTION_MESSAGE, null, vendedoresOpciones, vendedoresOpciones[0]);
        if (vendedorSeleccionadoStr == null) return;
        Vendedor vendedor = vendedores.stream().filter(v -> v.getNombre().equals(vendedorSeleccionadoStr)).findFirst().get();

        JTextField mp = new JTextField(); JTextField co = new JTextField();
        Object[] pagoMsg = {"Método de pago:", mp, "Cuenta origen:", co};
        int pagoOption = JOptionPane.showConfirmDialog(parent, pagoMsg, "Datos de Pago", JOptionPane.OK_CANCEL_OPTION);
        if (pagoOption != JOptionPane.OK_OPTION) return;

        String frase = "“La música es el alma de este evento”";
        Entrada entrada = new Entrada(cliente, vendedor, evento, nivel, precio, mp.getText(), co.getText(), frase);
        registro.registrar(entrada);
        vendedor.registrarVenta();

        entrada.generarBoletaPDF(admin.getNombre()); 
        JOptionPane.showMessageDialog(parent, "✅ Entrada comprada y PDF generado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // --- FUNCIONES VENDEDOR ---

    public void registrarVendedorGUI(JFrame parent) {
        JTextField n = new JTextField(); JTextField c = new JTextField(); JPasswordField p = new JPasswordField();
        Object[] message = {"Nombre:", n, "Correo:", c, "Clave:", p};
        int option = JOptionPane.showConfirmDialog(parent, message, "Registro de Vendedor", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String correo = c.getText();
            boolean existe = vendedores.stream().anyMatch(vend -> vend.getCorreo().equalsIgnoreCase(correo));
            if (existe) { JOptionPane.showMessageDialog(parent, "Error: El correo ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            vendedores.add(new Vendedor(n.getText(), correo, new String(p.getPassword())));
            JOptionPane.showMessageDialog(parent, "✅ Registro de Vendedor exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loginVendedorGUI(JFrame parent) {
        JTextField c = new JTextField(); JPasswordField p = new JPasswordField();
        Object[] loginMsg = {"Correo:", c, "Clave:", p};
        int loginOption = JOptionPane.showConfirmDialog(parent, loginMsg, "Login Vendedor", JOptionPane.OK_CANCEL_OPTION);
        if (loginOption != JOptionPane.OK_OPTION) return;
        
        Vendedor v = vendedores.stream().filter(x -> x.getCorreo().equals(c.getText()) && x.validarPassword(new String(p.getPassword()))).findFirst().orElse(null);

        if (v == null) { JOptionPane.showMessageDialog(parent, "❌ Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE); return; }

        String info = String.format("Bienvenido %s\nGrupo: %s\nVentas realizadas: %d", v.getNombre(), v.getGrupo(), v.getTotalVentas());
        JOptionPane.showMessageDialog(parent, info, "Información de Vendedor", JOptionPane.INFORMATION_MESSAGE);
    }
}