package modelo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase que gestiona el sistema de turnos para la atención en el gremio.
 * 
 * ESTRUCTURA DE DATOS: Queue<String> (LinkedList como implementación)
 * 
 * JUSTIFICACIÓN:
 * - Queue (FIFO - First In First Out) es perfecta para turnos porque:
 *   * offer() y poll() en O(1)
 *   * Garantiza orden FIFO (primer llegado, primer atendido)
 *   * Semántica clara: enqueue al llegar, dequeue al atender
 *   * Ideal para simular "líneas de espera"
 * 
 * ALTERNATIVAS RECHAZADAS:
 * - Stack: LIFO, incorrecto para turnos (último sería primero)
 * - LinkedList: Menos clara semánticamente, requiere más cuidado
 * - PriorityQueue: Innecesaria, el turno es temporal, no por prioridad
 * - ArrayList: Rendimiento O(n) para dequeue
 * 
 * Este sistema registra aventureros en orden de llegada
 * para atención de servicios del gremio.
 */
public class SistemaGremio {

    /**
     * Clase interna que representa una solicitud de atención en el gremio.
     */
    public static class SolicitudAtencion {
        private String nombreAventurero;
        private String tipoServicio;
        private long tiempoLlegada;

        public SolicitudAtencion(String nombreAventurero, String tipoServicio) {
            this.nombreAventurero = nombreAventurero;
            this.tipoServicio = tipoServicio;
            this.tiempoLlegada = System.currentTimeMillis();
        }

        public String getNombreAventurero() { return nombreAventurero; }
        public String getTipoServicio() { return tipoServicio; }
        public long getTiempoLlegada() { return tiempoLlegada; }

        @Override
        public String toString() {
            return nombreAventurero + " (" + tipoServicio + ")";
        }
    }

    // Queue FIFO para mantener el orden de llegada
    private Queue<SolicitudAtencion> turnoGremio;

    /**
     * Constructor que inicializa la cola de turnos vacía.
     */
    public SistemaGremio() {
        this.turnoGremio = new LinkedList<>();
    }

    /**
     * Registra un aventurero en el turno del gremio.
     * Lo añade al final de la cola (FIFO).
     * 
     * @param nombreAventurero Nombre del aventurero
     * @param tipoServicio Tipo de servicio solicitado
     * @return true si se añadió correctamente
     */
    public boolean solicitarAtencion(String nombreAventurero, String tipoServicio) {
        if (nombreAventurero == null || nombreAventurero.isEmpty()) return false;
        
        SolicitudAtencion solicitud = new SolicitudAtencion(nombreAventurero, tipoServicio);
        return turnoGremio.offer(solicitud);
    }

    /**
     * Atiende al siguiente aventurero en la cola.
     * Extrae el primer elemento (FIFO).
     * 
     * @return La solicitud atendida, o null si la cola está vacía
     */
    public SolicitudAtencion atenderSiguiente() {
        return turnoGremio.poll();
    }

    /**
     * Obtiene sin remover la siguiente solicitud a atender.
     * 
     * @return La siguiente solicitud, o null si la cola está vacía
     */
    public SolicitudAtencion obtenerProximo() {
        return turnoGremio.peek();
    }

    /**
     * Obtiene el número de aventureros esperando en la cola.
     * 
     * @return Tamaño de la cola
     */
    public int obtenerNumeroEnCola() {
        return turnoGremio.size();
    }

    /**
     * Verifica si hay aventureros esperando.
     * 
     * @return true si la cola no está vacía
     */
    public boolean hayAventurosEsperando() {
        return !turnoGremio.isEmpty();
    }

    /**
     * Limpia la cola de turnos.
     */
    public void limpiar() {
        turnoGremio.clear();
    }

    /**
     * Muestra el estado actual de la cola de turnos.
     * 
     * @return String con información del turno
     */
    @Override
    public String toString() {
        if (turnoGremio.isEmpty()) {
            return "No hay aventureros en espera";
        }
        StringBuilder sb = new StringBuilder("=== TURNOS DEL GREMIO ===\n");
        sb.append("Aventureros en espera: ").append(turnoGremio.size()).append("\n");
        sb.append("Próximo a atender: ").append(obtenerProximo()).append("\n");
        return sb.toString();
    }
}
