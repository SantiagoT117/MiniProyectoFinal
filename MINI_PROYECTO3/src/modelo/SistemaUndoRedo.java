    package modelo;

import java.util.Stack;

/**
 * Clase que implementa el sistema de deshacer/rehacer acciones en batalla.
 * 
 * ESTRUCTURA DE DATOS: Stack<Accion> (dos stacks: uno para undo, otro para redo)
 * 
 * JUSTIFICACIÓN:
 * - Stack es la estructura perfecta para undo/redo porque:
 *   * push() y pop() en O(1)
 *   * LIFO (Last In First Out) es semanticamente correcto:
 *     - Deshacer la última acción realizada
 *     - Rehacer la última acción deshecha
 *   * peek() permite ver sin consumir
 *   * Muy eficiente para este caso de uso
 * 
 * ALTERNATIVAS RECHAZADAS:
 * - Queue: FIFO es incorrecto, desharíamos la primera acción
 * - LinkedList: Menos clara, requiere más cuidado en operaciones
 * - ArrayList: Acceso rápido pero semántica confusa
 * - Deque: Innecesariamente flexible
 * 
 * Mantiene dos pilas:
 * - pilaUndo: acciones realizadas que pueden deshacerse
 * - pilaRedo: acciones deshacidas que pueden rehacerse
 * 
 * Cada acción contiene:
 * - Personaje afectado
 * - Tipo de acción (ataque, defensa, etc.)
 * - Estado anterior (para restaurar)
 */
public class SistemaUndoRedo {

    /**
     * Clase que representa una acción realizable/deshacible.
     */
    public static class Accion {
        private String descripcion;
        private String personajeName;
        private int hpAnterior;
        private int mpAnterior;
        private TipoAccion tipo;
        
        // Campos para guardar el estado del objetivo (enemigo/aliado afectado)
        private String objetivoName;
        private int hpAnteriorObjetivo;
        private int mpAnteriorObjetivo;

        public enum TipoAccion {
            ATAQUE("Ataque"),
            DEFENSA("Defensa"),
            HECHIZO("Hechizo"),
            OBJETO("Usar Objeto"),
            CURACION("Curación");

            private String nombre;

            TipoAccion(String nombre) {
                this.nombre = nombre;
            }

            public String getNombre() { return nombre; }
        }

        /**
         * Constructor de una acción.
         * 
         * @param descripcion Descripción de la acción
         * @param personajeName Nombre del personaje que realizó la acción
         * @param hpAnterior HP anterior del personaje afectado
         * @param mpAnterior MP anterior del personaje afectado
         * @param tipo Tipo de acción
         */
        public Accion(String descripcion, String personajeName, int hpAnterior, int mpAnterior, TipoAccion tipo) {
            this.descripcion = descripcion;
            this.personajeName = personajeName;
            this.hpAnterior = hpAnterior;
            this.mpAnterior = mpAnterior;
            this.tipo = tipo;
            this.objetivoName = null;
            this.hpAnteriorObjetivo = 0;
            this.mpAnteriorObjetivo = 0;
        }

        /**
         * Constructor de una acción con información del objetivo afectado.
         * 
         * @param descripcion Descripción de la acción
         * @param personajeName Nombre del personaje que realizó la acción
         * @param hpAnterior HP anterior del actor
         * @param mpAnterior MP anterior del actor
         * @param tipo Tipo de acción
         * @param objetivoName Nombre del personaje afectado (enemigo/aliado)
         * @param hpAnteriorObjetivo HP anterior del objetivo
         * @param mpAnteriorObjetivo MP anterior del objetivo
         */
        public Accion(String descripcion, String personajeName, int hpAnterior, int mpAnterior, TipoAccion tipo,
                      String objetivoName, int hpAnteriorObjetivo, int mpAnteriorObjetivo) {
            this.descripcion = descripcion;
            this.personajeName = personajeName;
            this.hpAnterior = hpAnterior;
            this.mpAnterior = mpAnterior;
            this.tipo = tipo;
            this.objetivoName = objetivoName;
            this.hpAnteriorObjetivo = hpAnteriorObjetivo;
            this.mpAnteriorObjetivo = mpAnteriorObjetivo;
        }

        // Getters
        public String getDescripcion() { return descripcion; }
        public String getPersonajeName() { return personajeName; }
        public int getHpAnterior() { return hpAnterior; }
        public int getMpAnterior() { return mpAnterior; }
        public TipoAccion getTipo() { return tipo; }
        
        // Getters para el objetivo
        public String getObjetivoName() { return objetivoName; }
        public int getHpAnteriorObjetivo() { return hpAnteriorObjetivo; }
        public int getMpAnteriorObjetivo() { return mpAnteriorObjetivo; }

        @Override
        public String toString() {
            return tipo.nombre + ": " + descripcion;
        }
    }

    // Dos pilas para undo/redo
    private Stack<Accion> pilaUndo;  // Acciones que pueden deshacerse
    private Stack<Accion> pilaRedo;  // Acciones deshacidas que pueden rehacerse

    /**
     * Constructor que inicializa ambas pilas.
     */
    public SistemaUndoRedo() {
        this.pilaUndo = new Stack<>();
        this.pilaRedo = new Stack<>();
    }

    /**
     * Registra una acción realizada.
     * Añade a la pila de undo y limpia la pila de redo.
     * (Si realizas una acción después de deshacer, pierdes el historial de redo)
     * 
     * @param accion La acción a registrar
     */
    public void registrarAccion(Accion accion) {
        pilaUndo.push(accion);
        // Limpiar redo cuando se realiza una nueva acción
        pilaRedo.clear();
    }

    /**
     * Deshace la última acción.
     * Mueve de la pila undo a la pila redo.
     * 
     * @return La acción deshecha, o null si no hay acciones para deshacer
     */
    public Accion deshacer() {
        if (pilaUndo.isEmpty()) return null;
        
        Accion accion = pilaUndo.pop();
        pilaRedo.push(accion);
        return accion;
    }

    /**
     * Rehace la última acción deshecha.
     * Mueve de la pila redo a la pila undo.
     * 
     * @return La acción rehecha, o null si no hay acciones para rehacer
     */
    public Accion rehacer() {
        if (pilaRedo.isEmpty()) return null;
        
        Accion accion = pilaRedo.pop();
        pilaUndo.push(accion);
        return accion;
    }

    /**
     * Verifica si hay acciones para deshacer.
     * 
     * @return true si la pila undo no está vacía
     */
    public boolean puedeDeshacer() {
        return !pilaUndo.isEmpty();
    }

    /**
     * Verifica si hay acciones para rehacer.
     * 
     * @return true si la pila redo no está vacía
     */
    public boolean puedeRehacer() {
        return !pilaRedo.isEmpty();
    }

    /**
     * Obtiene la última acción sin consumirla.
     * 
     * @return Última acción en undo, o null si está vacía
     */
    public Accion obtenerUltimaAccion() {
        return pilaUndo.isEmpty() ? null : pilaUndo.peek();
    }

    /**
     * Obtiene el número de acciones en el historial de undo.
     * 
     * @return Tamaño de la pila undo
     */
    public int obtenerNumeroDeshacer() {
        return pilaUndo.size();
    }

    /**
     * Obtiene el número de acciones en el historial de redo.
     * 
     * @return Tamaño de la pila redo
     */
    public int obtenerNumeroRehacer() {
        return pilaRedo.size();
    }

    /**
     * Limpia todo el historial.
     */
    public void limpiar() {
        pilaUndo.clear();
        pilaRedo.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== HISTORIAL UNDO/REDO ===\n");
        sb.append("Acciones para deshacer: ").append(pilaUndo.size()).append("\n");
        sb.append("Acciones para rehacer: ").append(pilaRedo.size()).append("\n");
        
        if (!pilaUndo.isEmpty()) {
            sb.append("\nÚltima acción: ").append(pilaUndo.peek()).append("\n");
        }
        
        return sb.toString();
    }
}
