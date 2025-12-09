package controlador;

import java.io.IOException;
import java.util.ArrayList;

import modelo.*;
import vista.*;

/**
 * Controlador principal del sistema de batalla.
 * Implementa el patrón MVC coordinando la lógica del juego entre el modelo y la vista.
 * 
 * Responsabilidades:
 * - Gestionar el flujo de la batalla (turnos de héroes y enemigos)
 * - Determinar el orden de ataque basado en la velocidad
 * - Procesar las acciones del jugador (atacar, habilidades, guardar/cargar)
 * - Verificar condiciones de victoria/derrota
 * - Coordinar las actualizaciones entre modelo y vista
 */
public class ControladorBatalla{
    
    private Batalla batalla;
    private Heroe[] heroes;
    private Enemigo[] enemigos;
    private VistaJuego vista;
    private Object[] ordenTurnos;

    /**
     * Constructor del controlador de batalla.
     * 
     * @param batalla Objeto batalla que contiene el estado del juego
     * @param heroes Array de héroes participantes
     * @param enemigos Array de enemigos participantes
     * @param vista Vista seleccionada (Terminal o GUI)
     */
    public ControladorBatalla(Batalla batalla, Heroe[] heroes, Enemigo[] enemigos, VistaJuego vista){
        this.heroes = heroes;
        this.enemigos = enemigos;
        this.vista = vista;
        this.batalla = batalla;
    }


    /**
     * Calcula el orden de ataque de todos los personajes según su velocidad.
     * Los personajes más rápidos actúan primero.
     * 
     * Algoritmo:
     * 1. Agrupa todos los héroes y enemigos en una lista
     * 2. Itera buscando el personaje más rápido disponible
     * 3. Lo añade al orden y lo elimina de la lista temporal
     * 4. Repite hasta ordenar a todos
     * 
     * @return ArrayList con los personajes ordenados de más rápido a más lento
     */
    private ArrayList<Object> ordenAtaque(){
        ArrayList<Object> todos = new ArrayList<>();

        // Agregar todos los héroes
        for (Heroe h : heroes){
            todos.add(h);
        }

        // Agregar todos los enemigos
        for(Enemigo e : enemigos){
            todos.add(e);
        }

        ArrayList<Object> orden = new ArrayList<>();

        // Ordenar por velocidad (selección del más rápido repetidamente)
        while(!todos.isEmpty()){
            Object masRapido = todos.get(0);

            for(Object o : todos){

                // Obtener velocidad según el tipo de personaje
                int velO = (o instanceof Heroe)? ((Heroe)o).getVelocidad() : ((Enemigo)o).getVelocidad();

                int velMR = (masRapido instanceof Heroe)? ((Heroe)masRapido).getVelocidad() : ((Enemigo)masRapido).getVelocidad(); 
                
                if(velO > velMR){
                    masRapido = o;
                }
            }

            orden.add(masRapido);
            todos.remove(masRapido);
        }
        return orden;
    }

    /**
     * Inicia y gestiona el bucle principal de la batalla.
     * 
     * Flujo:
     * 1. Muestra información inicial (equipos y orden de ataque)
     * 2. Ejecuta turnos de héroes (controlados por el jugador)
     * 3. Ejecuta turnos de enemigos (automáticos)
     * 4. Verifica condiciones de victoria/derrota después de cada acción
     * 5. Continúa hasta que un equipo sea eliminado
     */
    public void iniciarBatalla() {

        vista.mostrarMensaje("¡La batalla comienza!");
        vista.mostrarHeroes(heroes);
        vista.mostrarEnemigos(enemigos);
        vista.mostrarMensaje("El orden de ataque es: " + ordenAtaque());

        // Bucle principal: continúa mientras ambos equipos tengan miembros vivos
        while (hayHeroesVivos() && hayEnemigosVivos()) {

            // Turno de cada héroe vivo
            for (Heroe heroe : heroes) {
                if (heroe.esta_vivo()) {
                    turnoHeroe(heroe);

                    // Verificar victoria después de cada acción del héroe
                    if (!hayEnemigosVivos()) {
                        vista.mostrarVictoria();
                        return;
                    }
                }
            }

            // Turno de cada enemigo vivo
            for (Enemigo enemigo : enemigos) {
                if (enemigo.esta_vivo()) {
                    turnoEnemigo(enemigo);

                    // Verificar derrota después de cada acción del enemigo
                    if (!hayHeroesVivos()) {
                        vista.mostrarDerrota();
                        return;
                    }
                }
            }
        }
    }

    /**
     * Guarda el estado actual de la partida en un archivo.
     * Utiliza try-catch para manejar errores de escritura.
     */
    public void guardarpartida(){
        try{
            // Delega en el modelo Batalla para guardar en save.txt
            batalla.guardarpartida("save.txt");
            vista.mostrarMensaje("Partida guardada con exito");
        }catch(Exception e){
            // Muestra mensaje de error si falla el guardado
            vista.mostrarMensaje("Error al guardar" + e.getMessage());
        }
    }

    /**
     * Carga una partida guardada desde un archivo.
     * Actualiza los arrays de héroes y enemigos y la visualización.
     * Utiliza try-catch para manejar errores de lectura.
     */
    public void cargarpartida(){
        try{
            // Delega en el modelo Batalla para cargar desde save.txt
            batalla.cargarpartida("save.txt");
            
            // Actualizar referencias a los equipos cargados
            this.heroes = batalla.getEquipoHeroes();
            this.enemigos = batalla.getEquipoEnemigos();
            
            // Actualizar la vista con los datos cargados
            vista.mostrarHeroes(this.heroes);
            vista.mostrarEnemigos(this.enemigos);
            vista.mostrarMensaje("Partida cargada exitosamente");
        }catch(Exception e){
            // Muestra mensaje de error si falla la carga
            vista.mostrarMensaje("Error al cargar" + e.getMessage());
        }

    }

    /**
     * Gestiona el turno de un héroe.
     * Presenta las opciones al jugador y ejecuta la acción elegida.
     * 
     * Opciones disponibles:
     * 1. Atacar - Selecciona un enemigo y lo ataca
     * 2. Habilidad - (No implementado aún)
     * 3. Guardar partida - Guarda el estado actual
     * 4. Cargar partida - Carga una partida guardada
     * 
     * Utiliza try-catch para validar la selección de enemigo.
     * 
     * @param heroe Héroe que realizará la acción
     */
    private void turnoHeroe(Heroe heroe) {
        vista.mostrarEstado(heroe, primerEnemigoVivo());

        int accion = vista.elegirAccion(heroe);

        switch (accion) {
            case 1: // Atacar
            try { 
                // Try-catch para verificar que se seleccione un enemigo válido
                int idx = vista.seleccionarEnemigo(enemigos);
                
                // Validar que el índice esté en rango
                if(idx < 0 || idx > enemigos.length){
                    throw new IndexOutOfBoundsException();
                }
                
                Enemigo objetivo = enemigos[idx];
                heroe.atacar(objetivo);

                vista.mostrarMensaje(heroe.getNombre() + " ataco a " + objetivo.getNombre());
                vista.actualizarBarras();

            }catch (IndexOutOfBoundsException e){
                vista.mostrarMensaje("Opción inválida. Selecciona un enemigo existente");
                turnoHeroe(heroe); // Reintentar el turno
            }
            break;


            case 2: // Habilidad (no implementada)
                vista.mostrarMensaje("no esta implementada por temas de presupuesto");
                break;
            
            case 3: // Guardar partida
                try {
                    batalla.guardarpartida("save.txt");
                    vista.mostrarMensaje("Partida guardada correctamente");
                } catch (IOException e) {
                    vista.mostrarMensaje("Error al guardar la partida");
                }
                break;

            case 4: // Cargar partida
                try {
                    batalla.cargarpartida("save.txt");
                    vista.actualizarBarras();
                    vista.mostrarMensaje("Partida cargada correctamente");

                } catch (IOException e) {
                    vista.mostrarMensaje("No se pudo cargar la partida");
                }
                break;

            default:
                vista.mostrarMensaje("Opcion invalida");
                turnoHeroe(heroe); // Reintentar el turno
                break;
        }
    }

    /**
     * Gestiona el turno automático de un enemigo.
     * El enemigo ataca al primer héroe vivo encontrado.
     * 
     * @param enemigo Enemigo que realizará la acción
     */
    private void turnoEnemigo(Enemigo enemigo) {
        Heroe objetivo = primerHeroeVivo();
        enemigo.atacar(objetivo);
        vista.mostrarMensaje(enemigo.getNombre() + " atacó a " + objetivo.getNombre());
    }

    /**
     * Verifica si hay al menos un héroe vivo.
     * 
     * @return true si hay héroes vivos, false si todos están muertos
     */
    private boolean hayHeroesVivos() {
        for (Heroe h : heroes)
            if (h.esta_vivo()) return true;
        return false;
    }

    /**
     * Verifica si hay al menos un enemigo vivo.
     * 
     * @return true si hay enemigos vivos, false si todos están muertos
     */
    private boolean hayEnemigosVivos() {
        for (Enemigo e : enemigos)
            if (e.esta_vivo()) return true;
        return false;
    }

    /**
     * Encuentra el primer héroe vivo en el array.
     * 
     * @return Primer héroe vivo, o null si todos están muertos
     */
    private Heroe primerHeroeVivo() {
        for (Heroe h : heroes) if (h.esta_vivo()) return h;
        return null;
    }

    /**
     * Encuentra el primer enemigo vivo en el array.
     * 
     * @return Primer enemigo vivo, o null si todos están muertos
     */
    private Enemigo primerEnemigoVivo() {
        for (Enemigo e : enemigos) if (e.esta_vivo()) return e;
        return null;
    }   

    /**
     * Inicializa la vista con este controlador y comienza la batalla.
     */
    public void iniciar(){
        vista.iniciar(this);
        iniciarBatalla();
    }


}
