package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que registra el historial de batallas completadas.
 * 
 * ESTRUCTURA DE DATOS: LinkedList<RegistroBatalla>
 * 
 * JUSTIFICACIÓN:
 * - LinkedList es ideal para un historial porque proporciona:
 *   * Inserción O(1) al final (addLast), operación más frecuente
 *   * Acceso secuencial eficiente (iteración sobre historial)
 *   * Fácil acceso al último elemento (getLast) para ver batalla reciente
 *   * Mejor rendimiento que ArrayList para inserción al final
 * 
 * ALTERNATIVAS RECHAZADAS:
 * - ArrayList: Inserción O(n) al inicio si queremos cronológico inverso
 * - Stack: No permite acceso aleatorio al historial
 * - Queue: Pensado para consumo, no para registro
 * - TreeSet: Innecesariamente ordenado
 * 
 * El historial mantiene cada batalla completada con:
 * - Ganador (equipo de héroes o enemigos)
 * - Fecha y hora
 * - Duración en turnos
 * - Héroes participantes
 */
public class HistorialBatallas {

    /**
     * Clase interna que registra la información de una batalla.
     */
    public static class RegistroBatalla implements Serializable {
        private static final long serialVersionUID = 1L;

        private boolean victoriaHeroes;
        private LocalDateTime fecha;
        private int turnosUsados;
        private String[] nombresHeroes;

        public RegistroBatalla(boolean victoriaHeroes, int turnosUsados, String[] nombresHeroes) {
            this.victoriaHeroes = victoriaHeroes;
            this.fecha = LocalDateTime.now();
            this.turnosUsados = turnosUsados;
            this.nombresHeroes = nombresHeroes;
        }

        // Getters
        public boolean esVictoriaHeroes() { return victoriaHeroes; }
        public LocalDateTime getFecha() { return fecha; }
        public int getTurnosUsados() { return turnosUsados; }
        public String[] getNombresHeroes() { return nombresHeroes; }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String resultado = victoriaHeroes ? "VICTORIA" : "DERROTA";
            return String.format("[%s] %s - %d turnos - Equipo: %s",
                fecha.format(formatter), resultado, turnosUsados, String.join(", ", nombresHeroes));
        }
    }

    // LinkedList para mantener el orden de batallas (inserción al final O(1))
    private LinkedList<RegistroBatalla> historial;

    /**
     * Constructor que inicializa el historial vacío.
     */
    public HistorialBatallas() {
        this.historial = new LinkedList<>();
    }

    /**
     * Registra una batalla completada.
     * 
     * @param victoriaHeroes true si ganaron los héroes, false si ganaron enemigos
     * @param turnosUsados Número de turnos que duró la batalla
     * @param nombresHeroes Array con los nombres de los héroes
     */
    public void registrarBatalla(boolean victoriaHeroes, int turnosUsados, String[] nombresHeroes) {
        RegistroBatalla registro = new RegistroBatalla(victoriaHeroes, turnosUsados, nombresHeroes);
        historial.addLast(registro);
    }

    /**
     * Obtiene la batalla más reciente.
     * 
     * @return RegistroBatalla más reciente, o null si no hay batallas
     */
    public RegistroBatalla obtenerUltimaBatalla() {
        return historial.isEmpty() ? null : historial.getLast();
    }

    /**
     * Obtiene todo el historial de batallas.
     * 
     * @return Lista con todas las batallas registradas
     */
    public List<RegistroBatalla> obtenerHistorial() {
        return new LinkedList<>(historial);
    }

    /**
     * Obtiene el número total de batallas completadas.
     * 
     * @return Cantidad de batallas en el historial
     */
    public int obtenerTotalBatallas() {
        return historial.size();
    }

    /**
     * Obtiene el número de victorias en el historial.
     * 
     * @return Cantidad de victorias de héroes
     */
    public int obtenerVictorias() {
        return (int) historial.stream()
            .filter(RegistroBatalla::esVictoriaHeroes)
            .count();
    }

    /**
     * Obtiene el número de derrotas en el historial.
     * 
     * @return Cantidad de derrotas
     */
    public int obtenerDerrotas() {
        return historial.size() - obtenerVictorias();
    }

    /**
     * Obtiene el promedio de turnos por batalla.
     * 
     * @return Promedio de turnos, o 0 si no hay batallas
     */
    public double obtenerPromedioTurnos() {
        if (historial.isEmpty()) return 0;
        return historial.stream()
            .mapToInt(RegistroBatalla::getTurnosUsados)
            .average()
            .orElse(0);
    }

    /**
     * Limpia el historial de batallas.
     */
    public void limpiar() {
        historial.clear();
    }

    @Override
    public String toString() {
        if (historial.isEmpty()) {
            return "Sin batallas registradas";
        }
        StringBuilder sb = new StringBuilder("=== HISTORIAL DE BATALLAS ===\n");
        sb.append("Total: ").append(historial.size())
          .append(" | Victorias: ").append(obtenerVictorias())
          .append(" | Derrotas: ").append(obtenerDerrotas())
          .append("\n\n");
        
        for (RegistroBatalla batalla : historial) {
            sb.append(batalla).append("\n");
        }
        return sb.toString();
    }
}
