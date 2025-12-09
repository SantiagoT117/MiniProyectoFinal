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
    // Sistema de undo/redo basado en dos pilas (undo y redo)
    private final SistemaUndoRedo undoRedo = new SistemaUndoRedo();
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

        // Interceptar comandos meta (undo/redo) antes del switch
        // Estos comandos NO consumen el turno del héroe
        if (accion == 0) {
            deshacer();
            return; // No consumir turno
        }
        if (accion == 9) {
            rehacer();
            return; // No consumir turno
        }

        switch (accion) {
            case 1: // Atacar
            try {
                // Capturar estado previo del héroe y del objetivo para permitir undo
                int hpPrev = heroe.getHp();
                int mpPrev = heroe.getMp();

                // Seleccionar objetivo
                int idx = vista.seleccionarEnemigo(enemigos);
                if (idx < 0 || idx >= enemigos.length) {
                    throw new IndexOutOfBoundsException();
                }

                Enemigo objetivo = enemigos[idx];
                // Capturar HP/MP del objetivo ANTES de atacar
                int hpObjetivoPrev = objetivo.getHp();
                int mpObjetivoPrev = objetivo.getMp();
                
                heroe.atacar(objetivo);

                // Registrar acción en la pila de undo/redo con info del objetivo
                undoRedo.registrarAccion(
                    new SistemaUndoRedo.Accion(
                        "Atacó a " + objetivo.getNombre(),
                        heroe.getNombre(),
                        hpPrev,
                        mpPrev,
                        SistemaUndoRedo.Accion.TipoAccion.ATAQUE,
                        objetivo.getNombre(),
                        hpObjetivoPrev,
                        mpObjetivoPrev
                    )
                );

                vista.mostrarMensaje(heroe.getNombre() + " ataco a " + objetivo.getNombre());
                vista.actualizarBarras();

            } catch (IndexOutOfBoundsException e){
                vista.mostrarMensaje("Opción inválida. Selecciona un enemigo existente");
                turnoHeroe(heroe); // Reintentar el turno
            }
            break;


            case 2: // Habilidad
                try {
                    // Capturar estado previo del héroe y del objetivo para permitir undo
                    int hpPrev = heroe.getHp();
                    int mpPrev = heroe.getMp();

                    // Seleccionar objetivo para la habilidad
                    int idx = vista.seleccionarEnemigo(enemigos);
                    if (idx < 0 || idx >= enemigos.length) {
                        throw new IndexOutOfBoundsException();
                    }

                    Enemigo objetivo = enemigos[idx];
                    // Capturar HP/MP del objetivo ANTES de usar habilidad
                    int hpObjetivoPrev = objetivo.getHp();
                    int mpObjetivoPrev = objetivo.getMp();
                    
                    // Verificar que tenga MP suficiente para habilidad (ejemplo: 20 MP)
                    if (heroe.getMp() < 20) {
                        vista.mostrarMensaje("No tienes MP suficiente para usar habilidad.");
                        break;
                    }

                    // Ejecutar habilidad según tipo de héroe
                    boolean exito = false;
                    String descripcionHabilidad = "";
                    
                    switch (heroe.getTipo()) {
                        case MAGO:
                        case DRUIDA:
                            exito = heroe.LanzaHechizoSueño(objetivo);
                            descripcionHabilidad = "Lanzó hechizo a " + objetivo.getNombre();
                            break;
                        case GUERRERO:
                        case PALADIN:
                            exito = heroe.provocarEnemigo(objetivo);
                            descripcionHabilidad = "Provocó a " + objetivo.getNombre();
                            break;
                        default:
                            vista.mostrarMensaje("Este tipo de héroe no tiene habilidades disponibles.");
                            break;
                    }

                    if (exito) {
                        // Registrar acción en la pila de undo/redo con info del objetivo
                        undoRedo.registrarAccion(
                            new SistemaUndoRedo.Accion(
                                descripcionHabilidad,
                                heroe.getNombre(),
                                hpPrev,
                                mpPrev,
                                SistemaUndoRedo.Accion.TipoAccion.HECHIZO,
                                objetivo.getNombre(),
                                hpObjetivoPrev,
                                mpObjetivoPrev
                            )
                        );

                        vista.mostrarMensaje(heroe.getNombre() + " usó habilidad: " + descripcionHabilidad);
                        vista.actualizarBarras();
                    } else {
                        vista.mostrarMensaje("No se pudo ejecutar la habilidad.");
                    }

                } catch (IndexOutOfBoundsException e) {
                    vista.mostrarMensaje("Opción inválida. Selecciona un enemigo existente");
                    turnoHeroe(heroe); // Reintentar el turno
                }
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

    /**METODOS UNDO REDO */

    /**
     * Busca un héroe por nombre.
     * @param nombre Nombre del héroe a buscar
     * @return El héroe si existe, null en caso contrario
     */
    private Heroe buscarHeroe(String nombre) {
        for (Heroe h : heroes) {
            if (h.getNombre().equals(nombre)) {
                return h;
            }
        }
        return null;
    }

    /**
     * Busca un enemigo por nombre.
     * @param nombre Nombre del enemigo a buscar
     * @return El enemigo si existe, null en caso contrario
     */
    private Enemigo buscarEnemigo(String nombre) {
        for (Enemigo e : enemigos) {
            if (e.getNombre().equals(nombre)) {
                return e;
            }
        }
        return null;
    }

    public void deshacer(){
        if (undoRedo.puedeDeshacer()){
            SistemaUndoRedo.Accion acc = undoRedo.deshacer();
            
            if (acc != null) {
                // Restaurar HP/MP del actor (el que realizó la acción)
                Personaje actor = buscarHeroe(acc.getPersonajeName());
                if (actor == null) {
                    actor = buscarEnemigo(acc.getPersonajeName());
                }
                
                if (actor != null) {
                    actor.setHp(acc.getHpAnterior());
                    actor.setMp(acc.getMpAnterior());
                }
                
                // Restaurar HP/MP del objetivo (el que fue afectado)
                if (acc.getObjetivoName() != null && !acc.getObjetivoName().isEmpty()) {
                    Personaje objetivo = buscarHeroe(acc.getObjetivoName());
                    if (objetivo == null) {
                        objetivo = buscarEnemigo(acc.getObjetivoName());
                    }
                    
                    if (objetivo != null) {
                        objetivo.setHp(acc.getHpAnteriorObjetivo());
                        objetivo.setMp(acc.getMpAnteriorObjetivo());
                    }
                }
                
                vista.actualizarBarras();
                vista.mostrarMensaje("⟲ Accion deshecha: " + acc.getDescripcion());
            }
        } else {
            vista.mostrarMensaje("No hay acciones para deshacer");
        }
    }

    public void rehacer(){
        if (undoRedo.puedeRehacer()){
            SistemaUndoRedo.Accion acc = undoRedo.rehacer();
            
            if (acc != null) {
                // Restaurar HP/MP del actor (el que realizó la acción)
                Personaje actor = buscarHeroe(acc.getPersonajeName());
                if (actor == null) {
                    actor = buscarEnemigo(acc.getPersonajeName());
                }
                
                if (actor != null) {
                    actor.setHp(acc.getHpAnterior());
                    actor.setMp(acc.getMpAnterior());
                }
                
                // Restaurar HP/MP del objetivo (el que fue afectado)
                if (acc.getObjetivoName() != null && !acc.getObjetivoName().isEmpty()) {
                    Personaje objetivo = buscarHeroe(acc.getObjetivoName());
                    if (objetivo == null) {
                        objetivo = buscarEnemigo(acc.getObjetivoName());
                    }
                    
                    if (objetivo != null) {
                        objetivo.setHp(acc.getHpAnteriorObjetivo());
                        objetivo.setMp(acc.getMpAnteriorObjetivo());
                    }
                }
                
                vista.actualizarBarras();
                vista.mostrarMensaje("⟳ Accion rehecha: " + acc.getDescripcion());
            }
        } else {
            vista.mostrarMensaje("No hay acciones para rehacer");
        }
    }

    /**
     * Verifica si hay acciones disponibles para deshacer.
     * @return true si la pila de undo no está vacía
     */
    public boolean puedeDeshacer() {
        return undoRedo.puedeDeshacer();
    }

    /**
     * Verifica si hay acciones disponibles para rehacer.
     * @return true si la pila de redo no está vacía
     */
    public boolean puedeRehacer() {
        return undoRedo.puedeRehacer();
    }

    /**
     * Inicializa la vista con este controlador y comienza la batalla.
     */
    public void iniciar(){
        vista.iniciar(this);
        iniciarBatalla();
    }


}
