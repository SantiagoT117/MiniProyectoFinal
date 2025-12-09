package vista;

import modelo.*;
import java.awt.*;

import javax.swing.*;

import controlador.ControladorBatalla;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación de la vista con interfaz gráfica de usuario (GUI).
 * Utiliza Swing para crear una ventana interactiva con:
 * - Paneles para mostrar héroes y enemigos
 * - Barras de progreso para HP de cada personaje
 * - Botones para las acciones (Atacar, Habilidad, Guardar, Cargar)
 * - Área de texto para el log de eventos
 * 
 * Características:
 * - Actualización visual en tiempo real de las barras de HP
 * - Interfaz responsiva con listeners de eventos
 * - Organización en paneles (héroes a la izquierda, enemigos al centro, log a la derecha)
 * 
 * Limitaciones conocidas:
 * - Las barras de HP no se actualizan correctamente después de cargar una partida
 * - La sincronización entre GUI y lógica de batalla requiere el uso de locks
 */
public class VistaGUI extends JFrame implements VistaJuego{

    // Componentes principales de la GUI
    private JFrame frame;
    private ControladorBatalla controlador;

    // Mapas para rastrear HP máximo y barras de progreso de cada personaje
    private Map<Heroe, Integer> hpMaxHeroes = new HashMap<>();
    private Map<Enemigo, Integer> hpMaxEnemigos = new HashMap<>();
    private Map<Heroe, JProgressBar> barrasHeroes = new HashMap<>();
    private Map<Enemigo, JProgressBar> barrasEnemigos = new HashMap<>();

    // Paneles y componentes de la interfaz
    private JPanel panelHeroes;
    private JPanel panelEnemigos;
    private JTextArea salida;
    private JScrollPane scroll;
    
    // Botones de acción
    private JButton btnatacar;
    private JButton btnhabilidad;
    private JButton btnGuardar;
    private JButton btnCargar;
    private JButton btnDeshacer;
    private JButton btnRehacer;
    private JPanel panelEstado;
    private JTextArea areaLog;
    
    private JButton btnVolverMenu;
    
    // Variables para manejar la selección del usuario
    private int accionElegida = -1;
    private int enemigoElegido = -1;

    // Locks para sincronizar la interacción del usuario con la lógica del juego
    private final Object lockAccion = new Object();
    private final Object lockEnemigo = new Object();

    /**
     * Constructor de la vista GUI.
     * Configura la ventana principal, crea todos los paneles y componentes,
     * y establece los listeners para los botones.
     */
    public VistaGUI(){

        // Configuración de la ventana principal
        setTitle("Dragon Quest VIII");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        

        // Área de salida de texto (log de eventos)
        salida = new JTextArea();
        salida.setEditable(false);
        salida.setFont(new Font("Consolas", Font.PLAIN, 14));
        salida.setBackground(new Color(30, 30, 30));
        salida.setForeground(Color.WHITE);

        JScrollPane scrollSalida = new JScrollPane(salida);
        scrollSalida.setPreferredSize(new Dimension(400, 180));
    

                // Inicializar componentes visuales
                panelEstado = new JPanel(new GridLayout(2, 4, 10, 10));
                panelEstado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                areaLog = new JTextArea();
                areaLog.setEditable(false);
                areaLog.setFont(new Font("Consolas", Font.PLAIN, 12));
                areaLog.setBackground(new Color(12, 12, 18));
                areaLog.setForeground(Color.WHITE);

                JScrollPane scroll = new JScrollPane(areaLog);
                scroll.setPreferredSize(new Dimension(400, 200));

                add(scroll, BorderLayout.EAST);

                panelHeroes = new JPanel();
                panelHeroes.setLayout(new GridLayout(3, 1));
                panelHeroes.setBorder(BorderFactory.createTitledBorder("Heroes"));
                panelHeroes.setPreferredSize(new Dimension(250, 600));

                add(panelHeroes, BorderLayout.WEST);

                panelEnemigos = new JPanel();
                panelEnemigos.setLayout(new GridLayout(3, 1));
                panelEnemigos.setBorder(BorderFactory.createTitledBorder("Enemigos"));
                panelEnemigos.setPreferredSize(new Dimension(250, 600));

                add(panelEnemigos, BorderLayout.CENTER);

                JPanel panelBotones = new JPanel();

                btnatacar = new JButton("Atacar");
                btnhabilidad = new JButton("Habilidad");
                btnGuardar = new JButton("Guardar");
                btnCargar = new JButton("Cargar");
                btnDeshacer = new JButton("⟲ Deshacer");
                btnRehacer = new JButton("⟳ Rehacer");

                // Inicialmente deshabilitados (se habilitarán cuando haya acciones)
                btnDeshacer.setEnabled(false);
                btnRehacer.setEnabled(false);

                panelBotones.add(btnDeshacer);
                panelBotones.add(btnatacar);
                panelBotones.add(btnhabilidad);
                panelBotones.add(btnGuardar);
                panelBotones.add(btnCargar);
                panelBotones.add(btnRehacer);

                add(panelBotones, BorderLayout.SOUTH);

                btnVolverMenu = new JButton("Salir");
                panelBotones.add(btnVolverMenu);
                btnVolverMenu.addActionListener(e -> dispose());

                btnatacar.addActionListener(e -> elegir(1));
                btnhabilidad.addActionListener(e -> elegir(2));
                // llama los actionlistener con los botones nuevos 
                btnGuardar.addActionListener(e -> controlador.guardarpartida());
                btnCargar.addActionListener(e -> controlador.cargarpartida());
                btnDeshacer.addActionListener(e -> elegir(0));
                btnRehacer.addActionListener(e -> elegir(9));

                setVisible(true);

                JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                bottom.add(btnVolverMenu);

                add(panelEstado, BorderLayout.CENTER);
                add(scroll, BorderLayout.EAST);
                add(bottom, BorderLayout.SOUTH);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // esta funcion si muestra a los heroes creados
    private JPanel generarHeroe(Heroe h){

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        card.setBackground(new Color(30,30,30)); 

        JLabel nombre = new JLabel(h.getNombre() + " //velocidad: " + h.getVelocidad() );
        nombre.setForeground(Color.WHITE);
        nombre.setFont(new Font("Arial", Font.BOLD, 12));

        hpMaxHeroes.putIfAbsent(h, h.getHp());
        int hpMax = hpMaxHeroes.get(h);

        JProgressBar vida = new JProgressBar(0, hpMax);
        vida.setValue(h.getHp());
        vida.setStringPainted(true);

        barrasHeroes.put(h, vida);

        card.add(nombre, BorderLayout.NORTH);
        card.add(vida, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel generarEnemigo(Enemigo e, int index){

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        card.setBackground(new Color(30,30,30)); 

        JLabel nombre = new JLabel(e.getNombre() + " //velocidad: " + e.getVelocidad() );
        nombre.setForeground(Color.WHITE);
        nombre.setFont(new Font("Arial", Font.BOLD, 14));
        
        hpMaxEnemigos.putIfAbsent(e, e.getHp());
        int hpMax = hpMaxEnemigos.get(e);

        JProgressBar vida = new JProgressBar(0, hpMax);
        vida.setValue(e.getHp());
        vida.setStringPainted(true);
    
        barrasEnemigos.put(e, vida);

        card.add(nombre, BorderLayout.NORTH);
        card.add(vida, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e1) {
                synchronized (lockEnemigo) {
                    enemigoElegido = index;
                    lockEnemigo.notify();
                }
            }
        });
        
        return card;
    }

    // funcion que actualiza las barras segun la vida del personaje 
    public void actualizarBarras(){
        for (Heroe h : barrasHeroes.keySet()){
            barrasHeroes.get(h).setValue(h.getHp());
        }

        for (Enemigo e : barrasEnemigos.keySet()){
            barrasEnemigos.get(e).setValue(e.getHp());
        }
    }

    private void elegir(int n) {
        synchronized (lockAccion) {
            accionElegida = n;
            lockAccion.notify();
        }
    }

    // aqui estan las funciones del VistaJuego, no usa Todas ya que la GUI no necesiat ciertas funciones que la terminal si 
    @Override
    public void mostrarMensaje(String mensaje) {
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    @Override
    public void mostrarEstado(Heroe heroe, Enemigo enemigo) {
        mostrarMensaje("-------------- HEROES -----------\n");
        mostrarMensaje(heroe.getNombre() + " //HP " + heroe.getHp() + " //MP " + heroe.getMp() + " //Ataque " + heroe.getAtaque());

        mostrarMensaje("-------- ENEMGIOS ---------\n");
        mostrarMensaje(enemigo.getNombre() + " //HP " + enemigo.getHp() + " //MP " + enemigo.getMp() + " //Ataque " + enemigo.getAtaque());
    }

    @Override
    public void mostrarHeroes(Heroe[] heroe) {

        barrasHeroes.clear();
        hpMaxHeroes.clear();

        panelHeroes.removeAll();
        for(Heroe h : heroe){
            hpMaxHeroes.put(h, h.getHp());
            panelHeroes.add(generarHeroe(h));
        }

        panelHeroes.revalidate();
        panelHeroes.repaint();
    }

    @Override
    public void mostrarEnemigos(Enemigo[] enemigo) {
        barrasEnemigos.clear();
        hpMaxEnemigos.clear();

        panelEnemigos.removeAll();

        for(int i = 0 ; i < enemigo.length; i++){
            Enemigo e = enemigo[i];
            hpMaxEnemigos.put(e, e.getHp());
            panelEnemigos.add(generarEnemigo(enemigo[i], i));
        }

        panelEnemigos.revalidate();
        panelEnemigos.repaint();
    }

    @Override
    public void mostrarVictoria() {
        mostrarMensaje("Victoria!!");
    }

    @Override
    public void mostrarDerrota() {
        mostrarMensaje("Derrota :C");
    }

    @Override
    public void esperarEnter() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'esperarEnter'");
    }

    @Override
    public int elegirAccion(Heroe heroe) {
        mostrarMensaje("Tunrno de " + heroe.getNombre());

        accionElegida = -1;
        mostrarMensaje("Elige una accion");

        // Actualizar estado de botones undo/redo
        actualizarBotonesUndoRedo();

        synchronized (lockAccion) {
            while (accionElegida == -1) {
                try { lockAccion.wait(); } catch (InterruptedException e) {}
            }
        }
        return accionElegida;
    }

    @Override
    public int seleccionarEnemigo(Enemigo[] enemigos) {
        enemigoElegido = -1;
        mostrarMensaje("Selecciona a un enemigo para atacarlo");
        synchronized (lockEnemigo) {
            while (enemigoElegido == -1) {
                try { lockEnemigo.wait(); } catch (InterruptedException e) {}
            }
        }
        return enemigoElegido;
    }

    @Override
    public int seleccionarHeroe(Heroe[] heroes) {
        mostrarMensaje("Selecciona a un heroe");
        return 0;
    }

    @Override
    public int seleccionarHeroeMuerto(Heroe[] heroes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'seleccionarHeroeMuerto'");
    }

    @Override
    public int leerEntero() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leerEntero'");
    }


    @Override
    public void iniciar(ControladorBatalla ctrl) {
        this.controlador = ctrl;
        setVisible(true);
    }

    /**
     * Actualiza el estado (habilitado/deshabilitado) de los botones de undo/redo
     * según la disponibilidad de acciones en el SistemaUndoRedo.
     */
    private void actualizarBotonesUndoRedo() {
        if (controlador != null) {
            btnDeshacer.setEnabled(controlador.puedeDeshacer());
            btnRehacer.setEnabled(controlador.puedeRehacer());
        }
    }

    
}