package vista;

import modelo.Heroe;
import modelo.Enemigo;
import controlador.ControladorBatalla;

/**
 * Interfaz que define el contrato para todas las vistas del juego.
 * Implementada por VistaTerminal y VistaGUI.
 * 
 * Establece los métodos que cualquier vista debe proporcionar para:
 * - Mostrar información del estado de la batalla
 * - Recibir entrada del usuario
 * - Actualizar la representación visual
 * - Mostrar mensajes y resultados
 * 
 * Permite intercambiar entre diferentes tipos de vista (Terminal/GUI)
 * sin cambiar la lógica del controlador (patrón Strategy).
 */
public interface VistaJuego {

    /**
     * Inicializa la vista con una referencia al controlador.
     * 
     * @param controlador Controlador que gestiona la lógica de batalla
     */
    public void iniciar(ControladorBatalla controlador);

    /**
     * Actualiza las barras de vida/magia en la interfaz.
     * (Principalmente usado en GUI)
     */
    void actualizarBarras();
    
    /**
     * Muestra un mensaje genérico al usuario.
     * 
     * @param mensaje Texto a mostrar
     */
    void mostrarMensaje(String mensaje);
    
    /**
     * Muestra el estado actual de un héroe y un enemigo.
     * 
     * @param heroe Héroe cuyo estado se mostrará
     * @param enemigo Enemigo cuyo estado se mostrará
     */
    void mostrarEstado(Heroe heroe, Enemigo enemigo);
    
    /**
     * Muestra la información de todos los héroes.
     * 
     * @param heroe Array de héroes a mostrar
     */
    void mostrarHeroes(Heroe[] heroe);
    
    /**
     * Muestra la información de todos los enemigos.
     * 
     * @param enemigo Array de enemigos a mostrar
     */
    void mostrarEnemigos(Enemigo[] enemigo);
    
    /**
     * Muestra un mensaje de victoria.
     */
    void mostrarVictoria();
    
    /**
     * Muestra un mensaje de derrota.
     */
    void mostrarDerrota();
    
    /**
     * Pausa la ejecución hasta que el usuario presione Enter.
     * (Principalmente usado en Terminal)
     */
    void esperarEnter();
    
    /**
     * Presenta las opciones de acción al jugador y retorna la elegida.
     * 
     * @param heroe Héroe que realizará la acción
     * @return Número de la acción elegida (1=Atacar, 2=Habilidad, 3=Guardar, 4=Cargar)
     */
    int elegirAccion(Heroe heroe);
    
    /**
     * Permite al jugador seleccionar un enemigo objetivo.
     * 
     * @param enemigos Array de enemigos disponibles
     * @return Índice del enemigo seleccionado
     */
    int seleccionarEnemigo(Enemigo[] enemigos);
    
    /**
     * Permite al jugador seleccionar un héroe aliado.
     * 
     * @param heroes Array de héroes disponibles
     * @return Índice del héroe seleccionado
     */
    int seleccionarHeroe(Heroe[] heroes);
    
    /**
     * Permite al jugador seleccionar un héroe muerto (para revivir).
     * 
     * @param heroes Array de héroes
     * @return Índice del héroe muerto seleccionado
     */
    int seleccionarHeroeMuerto(Heroe[] heroes);

    /**
     * Lee un número entero de la entrada del usuario.
     * 
     * @return Número entero ingresado
     */
    int leerEntero();


}
