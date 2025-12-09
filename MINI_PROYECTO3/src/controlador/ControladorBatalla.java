package controlador;

import modelo.*;
import vista.VistaJuego;
import java.util.Scanner;

/**
 * Controlador de batalla que gestiona la lógica del juego.
 * Implementa el patrón MVC (Modelo-Vista-Controlador).
 * 
 * RESPONSABILIDADES:
 * - Coordinar interacciones entre Modelo (Batalla) y Vista (VistaTerminal/VistaGUI)
 * - Gestionar turnos y acciones de los personajes
 * - Registrar acciones en el sistema de undo/redo
 * - Validar movimientos y aplicar efectos
 * - Determinar el ganador de la batalla
 * 
 * ESTRUCTURAS DE DATOS UTILIZADAS:
 * - SistemaUndoRedo: Stack para deshacer/rehacer acciones
 * - ArrayList: Para almacenar héroes y enemigos
 * 
 * @author Santiago Torres Rojas - 2380301
 * @author Samuel Garcia Parra - 2459476
 * @author Juan Sebastian Navarrete Rada - 2459562
 * @author Juan David Correa Zapata - 2459431
 */
public class ControladorBatalla {
    private Batalla batalla;
    private Heroe[] heroes;
    private Enemigo[] enemigos;
    private VistaJuego vista;
    private SistemaUndoRedo sistemaUndo;
    private HistorialBatallas historialBatallas;
    private int turno;
    private boolean batallaTerminada;

    /**
     * Constructor del controlador de batalla.
     * 
     * @param batalla Objeto que contiene la lógica de la batalla
     * @param heroes Array de héroes del jugador
     * @param enemigos Array de enemigos
     * @param vista Vista seleccionada (Terminal o GUI)
     */
    public ControladorBatalla(Batalla batalla, Heroe[] heroes, Enemigo[] enemigos, VistaJuego vista) {
        this.batalla = batalla;
        this.heroes = heroes;
        this.enemigos = enemigos;
        this.vista = vista;
        this.sistemaUndo = new SistemaUndoRedo();
        this.historialBatallas = new HistorialBatallas();
        this.turno = 0;
        this.batallaTerminada = false;
    }

    /**
     * Inicializa la batalla (llamar antes de iniciarBatalla).
     */
    public void iniciar() {
        vista.mostrarMensaje("=== Batalla iniciada ===");
        vista.mostrarMensaje("¡Que comience el combate!\n");
    }

    /**
     * Comienza y gestiona el bucle principal de la batalla.
     * Coordina los turnos de todos los personajes ordenados por velocidad.
     */
    public void iniciarBatalla() {
        // Ordenar personajes por velocidad (más rápido primero)
        Personaje[] orden = obtenerOrdenTurnos();
        
        while (!batalla.hayGanador()) {
            turno++;
            vista.mostrarMensaje("\n=== TURNO " + turno + " ===");
            
            // Procesar cada personaje en orden
            for (Personaje personaje : orden) {
                if (!personaje.estaVivo()) continue;
                if (batalla.hayGanador()) break;
                
                procesarTurnoPersonaje(personaje);
            }
            
            // Mostrar estado de la batalla después del turno
            mostrarEstadoBatalla();
        }
        
        // Batalla terminada - registrar resultado
        finalizarBatalla();
    }

    /**
     * Procesa el turno de un personaje específico (héroe o enemigo).
     * 
     * @param personaje Personaje cuyo turno se procesa
     */
    private void procesarTurnoPersonaje(Personaje personaje) {
        if (personaje instanceof Heroe) {
            procesarTurnoHeroe((Heroe) personaje);
        } else if (personaje instanceof Enemigo) {
            procesarTurnoEnemigo((Enemigo) personaje);
        }
    }

    /**
     * Procesa el turno de un héroe.
     * Permite al jugador elegir una acción: Atacar, Habilidad, Guardar, Cargar, o Usar Objeto.
     * Registra la acción en el sistema undo/redo.
     * 
     * @param heroe Héroe que realiza el turno
     */
    private void procesarTurnoHeroe(Heroe heroe) {
        vista.mostrarMensaje("\nTurno de: " + heroe.getNombre());
        
        boolean accionValida = false;
        while (!accionValida) {
            // Mostrar opciones y obtener elección
            int opcion = vista.elegirAccion(heroe);
            
            Enemigo objetivo = null;
            int hpAntesHero = heroe.getHP();
            int mpAntesHero = heroe.getMP();
            
            switch (opcion) {
                case 1: // Atacar
                    objetivo = elegirEnemigo();
                    if (objetivo != null) {
                        int hpAntesEnemi = objetivo.getHP();
                        batalla.atacar(heroe, objetivo);
                        
                        // Registrar en undo/redo
                        SistemaUndoRedo.Accion accion = new SistemaUndoRedo.Accion(
                            "Atacó a " + objetivo.getNombre(),
                            heroe.getNombre(),
                            hpAntesHero,
                            mpAntesHero,
                            SistemaUndoRedo.Accion.TipoAccion.ATAQUE
                        );
                        sistemaUndo.registrarAccion(accion);
                        
                        vista.mostrarMensaje(heroe.getNombre() + " atacó a " + objetivo.getNombre());
                        accionValida = true;
                    }
                    break;
                    
                case 2: // Habilidad/Magia
                    objetivo = elegirEnemigo();
                    if (objetivo != null && heroe.getMP() >= 10) {
                        batalla.habilidad(heroe, objetivo);
                        
                        // Registrar en undo/redo
                        SistemaUndoRedo.Accion accion = new SistemaUndoRedo.Accion(
                            "Lanzó hechizo a " + objetivo.getNombre(),
                            heroe.getNombre(),
                            hpAntesHero,
                            mpAntesHero,
                            SistemaUndoRedo.Accion.TipoAccion.HECHIZO
                        );
                        sistemaUndo.registrarAccion(accion);
                        
                        vista.mostrarMensaje(heroe.getNombre() + " lanzó un hechizo a " + objetivo.getNombre());
                        accionValida = true;
                    } else if (heroe.getMP() < 10) {
                        vista.mostrarMensaje("No tienes suficiente MP para usar una habilidad.");
                    }
                    break;
                    
                case 3: // Guardar (Defensa)
                    batalla.guardar(heroe);
                    
                    // Registrar en undo/redo
                    SistemaUndoRedo.Accion accion = new SistemaUndoRedo.Accion(
                        "Se preparó para defender",
                        heroe.getNombre(),
                        hpAntesHero,
                        mpAntesHero,
                        SistemaUndoRedo.Accion.TipoAccion.DEFENSA
                    );
                    sistemaUndo.registrarAccion(accion);
                    
                    vista.mostrarMensaje(heroe.getNombre() + " se preparó para defender.");
                    accionValida = true;
                    break;
                    
                case 4: // Cargar (No disponible en este turno - solo info)
                    vista.mostrarMensaje("Opción no disponible en este turno.");
                    break;
                    
                case 5: // Usar Objeto del Inventario
                    usarObjetoDelInventario(heroe);
                    accionValida = true;
                    break;
                    
                default:
                    vista.mostrarMensaje("Opción inválida.");
            }
        }
    }

    /**
     * Permite al héroe usar un objeto de su inventario.
     * Registra la acción en el sistema undo/redo.
     * 
     * @param heroe Héroe que usa el objeto
     */
    private void usarObjetoDelInventario(Heroe heroe) {
        Inventario inv = heroe.getInventario();
        
        if (inv.obtenerItems().isEmpty()) {
            vista.mostrarMensaje("No tienes objetos en tu inventario.");
            return;
        }
        
        // Mostrar inventario
        vista.mostrarMensaje("\n=== Inventario de " + heroe.getNombre() + " ===");
        var items = inv.obtenerItems();
        int i = 1;
        for (String item : items.keySet()) {
            vista.mostrarMensaje(i + ". " + item + " (x" + items.get(item) + ")");
            i++;
        }
        vista.mostrarMensaje("0. Cancelar");
        
        int opcion = obtenerNumero(0, items.size());
        if (opcion == 0) return;
        
        String itemSeleccionado = (String) items.keySet().toArray()[opcion - 1];
        
        int hpAntes = heroe.getHP();
        int mpAntes = heroe.getMP();
        
        // Aplicar efecto del objeto
        boolean usado = aplicarEfectoObjeto(heroe, itemSeleccionado);
        
        if (usado) {
            inv.usarItem(itemSeleccionado, 1);
            
            // Registrar en undo/redo
            SistemaUndoRedo.Accion accion = new SistemaUndoRedo.Accion(
                "Usó " + itemSeleccionado,
                heroe.getNombre(),
                hpAntes,
                mpAntes,
                SistemaUndoRedo.Accion.TipoAccion.OBJETO
            );
            sistemaUndo.registrarAccion(accion);
            
            vista.mostrarMensaje(heroe.getNombre() + " usó " + itemSeleccionado);
        }
    }

    /**
     * Aplica el efecto de un objeto al héroe.
     * Los objetos pueden restaurar HP, MP, o curar estados.
     * 
     * @param heroe Héroe que usa el objeto
     * @param item Nombre del objeto
     * @return true si el objeto se usó exitosamente
     */
    private boolean aplicarEfectoObjeto(Heroe heroe, String item) {
        int hpRestauro = heroe.getHP();
        int mpRestauro = heroe.getMP();
        
        switch (item) {
            case "Poción":
                heroe.setHP(Math.min(heroe.getHP() + 30, heroe.getHPMax()));
                vista.mostrarMensaje("+" + (heroe.getHP() - hpRestauro) + " HP");
                return true;
                
            case "Poción Fuerte":
                heroe.setHP(Math.min(heroe.getHP() + 80, heroe.getHPMax()));
                vista.mostrarMensaje("+" + (heroe.getHP() - hpRestauro) + " HP");
                return true;
                
            case "Elixir":
                heroe.setHP(heroe.getHPMax());
                heroe.setMP(heroe.getMPMax());
                vista.mostrarMensaje("¡HP y MP restaurados!");
                return true;
                
            case "Antídoto":
                vista.mostrarMensaje("Estado de envenenamiento curado.");
                return true;
                
            case "Bomba":
                Enemigo objetivo = elegirEnemigo();
                if (objetivo != null) {
                    objetivo.recibirDaño(50);
                    vista.mostrarMensaje("¡Explosión! " + objetivo.getNombre() + " recibió 50 de daño.");
                }
                return true;
                
            default:
                vista.mostrarMensaje("Efecto del objeto desconocido.");
                return false;
        }
    }

    /**
     * Procesa el turno de un enemigo.
     * Los enemigos atacan automáticamente a un héroe al azar.
     * 
     * @param enemigo Enemigo que realiza el turno
     */
    private void procesarTurnoEnemigo(Enemigo enemigo) {
        vista.mostrarMensaje("\nTurno de: " + enemigo.getNombre());
        
        // Seleccionar héroe aleatorio vivo
        Heroe objetivo = null;
        do {
            int index = (int) (Math.random() * heroes.length);
            objetivo = heroes[index];
        } while (!objetivo.estaVivo());
        
        batalla.atacar(enemigo, objetivo);
        vista.mostrarMensaje(enemigo.getNombre() + " atacó a " + objetivo.getNombre());
    }

    /**
     * Obtiene el orden de turnos basado en la velocidad de los personajes.
     * Mayor velocidad = actúa primero.
     * 
     * @return Array de personajes ordenados por velocidad
     */
    private Personaje[] obtenerOrdenTurnos() {
        Personaje[] personajes = new Personaje[heroes.length + enemigos.length];
        int index = 0;
        
        for (Heroe h : heroes) {
            personajes[index++] = h;
        }
        for (Enemigo e : enemigos) {
            personajes[index++] = e;
        }
        
        // Ordenar por velocidad (burbuja simple)
        for (int i = 0; i < personajes.length; i++) {
            for (int j = i + 1; j < personajes.length; j++) {
                if (personajes[i].getVelocidad() < personajes[j].getVelocidad()) {
                    Personaje temp = personajes[i];
                    personajes[i] = personajes[j];
                    personajes[j] = temp;
                }
            }
        }
        
        return personajes;
    }

    /**
     * Permite al jugador elegir un enemigo como objetivo.
     * 
     * @return El enemigo seleccionado, o null si cancela
     */
    private Enemigo elegirEnemigo() {
        vista.mostrarMensaje("\nElige un enemigo:");
        
        int vivos = 0;
        for (Enemigo e : enemigos) {
            if (e.estaVivo()) {
                vivos++;
                vista.mostrarMensaje(vivos + ". " + e.getNombre() + " (HP: " + e.getHP() + "/" + e.getHPMax() + ")");
            }
        }
        
        if (vivos == 0) return null;
        
        int opcion = obtenerNumero(1, vivos);
        int contador = 0;
        
        for (Enemigo e : enemigos) {
            if (e.estaVivo()) {
                contador++;
                if (contador == opcion) return e;
            }
        }
        
        return null;
    }

    /**
     * Obtiene un número válido del usuario.
     * 
     * @param min Número mínimo permitido
     * @param max Número máximo permitido
     * @return El número ingresado
     */
    private int obtenerNumero(int min, int max) {
        Scanner sc = new Scanner(System.in);
        int numero = -1;
        
        while (numero < min || numero > max) {
            try {
                System.out.print("Ingresa una opción (" + min + "-" + max + "): ");
                numero = Integer.parseInt(sc.nextLine());
                
                if (numero < min || numero > max) {
                    System.out.println("Número fuera de rango.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido.");
            }
        }
        
        return numero;
    }

    /**
     * Muestra el estado actual de la batalla.
     */
    private void mostrarEstadoBatalla() {
        vista.mostrarMensaje("\n--- Estado de la batalla ---");
        
        vista.mostrarMensaje("\nHéroes:");
        for (Heroe h : heroes) {
            String estado = h.estaVivo() ? "Vivo" : "Muerto";
            vista.mostrarMensaje("  " + h.getNombre() + " - HP: " + h.getHP() + "/" + h.getHPMax() + " - " + estado);
        }
        
        vista.mostrarMensaje("\nEnemigos:");
        for (Enemigo e : enemigos) {
            String estado = e.estaVivo() ? "Vivo" : "Muerto";
            vista.mostrarMensaje("  " + e.getNombre() + " - HP: " + e.getHP() + "/" + e.getHPMax() + " - " + estado);
        }
    }

    /**
     * Finaliza la batalla y registra el resultado en el historial.
     */
    private void finalizarBatalla() {
        batallaTerminada = true;
        
        // Determinar ganador
        if (batalla.hayGanador()) {
            if (batalla.ganaronHeroes()) {
                vista.mostrarMensaje("\n¡¡¡ LOS HÉROES GANARON !!!");
                // Registrar victoria en historial
                historialBatallas.registrarBatalla(true, turno, heroes);
            } else {
                vista.mostrarMensaje("\n¡¡¡ LOS ENEMIGOS GANARON !!!");
                // Registrar derrota en historial
                historialBatallas.registrarBatalla(false, turno, heroes);
            }
        }
    }

    /**
     * Deshace la última acción realizada.
     */
    public void deshacer() {
        if (sistemaUndo.puedeDeshacer()) {
            SistemaUndoRedo.Accion accion = sistemaUndo.deshacer();
            vista.mostrarMensaje("Se deshizo: " + accion.getDescripcion());
        } else {
            vista.mostrarMensaje("No hay acciones para deshacer.");
        }
    }

    /**
     * Rehace la última acción deshecha.
     */
    public void rehacer() {
        if (sistemaUndo.puedeRehacer()) {
            SistemaUndoRedo.Accion accion = sistemaUndo.rehacer();
            vista.mostrarMensaje("Se rehizo: " + accion.getDescripcion());
        } else {
            vista.mostrarMensaje("No hay acciones para rehacer.");
        }
    }

    /**
     * Obtiene el sistema de undo/redo.
     * 
     * @return El SistemaUndoRedo de esta batalla
     */
    public SistemaUndoRedo getSistemaUndo() {
        return sistemaUndo;
    }

    /**
     * Obtiene el historial de batallas.
     * 
     * @return El HistorialBatallas
     */
    public HistorialBatallas getHistorialBatallas() {
        return historialBatallas;
    }
}
