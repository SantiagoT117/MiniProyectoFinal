package modelo;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase que mantiene un registro de aventureros únicos.
 * 
 * ESTRUCTURA DE DATOS: HashSet<String>
 * 
 * JUSTIFICACIÓN:
 * - HashSet es ideal para un registro porque:
 *   * Garantiza unicidad (no hay aventureros duplicados)
 *   * add() y contains() en O(1)
 *   * remove() también en O(1)
 *   * No requiere ordenamiento
 *   * Perfecto para verificaciones de membresía
 * 
 * ALTERNATIVAS RECHAZADAS:
 * - ArrayList: Permitiría duplicados, búsqueda O(n)
 * - TreeSet: Ordenamiento innecesario O(log n)
 * - HashMap: Overkill si solo necesitamos nombres
 * - LinkedHashSet: Mantenimiento de orden innecesario
 * 
 * Este registro garantiza que cada aventurero está registrado
 * una sola vez en el gremio.
 */
public class RegistroAventureros {

    // HashSet para garantizar unicidad y búsqueda O(1)
    private HashSet<String> aventureros;

    /**
     * Constructor que inicializa el registro vacío.
     */
    public RegistroAventureros() {
        this.aventureros = new HashSet<>();
    }

    /**
     * Registra un aventurero en el gremio.
     * 
     * @param nombre Nombre del aventurero
     * @return true si se registró (no existía antes), false si ya estaba registrado
     */
    public boolean registrar(String nombre) {
        if (nombre == null || nombre.isEmpty()) return false;
        return aventureros.add(nombre);
    }

    /**
     * Verifica si un aventurero está registrado.
     * 
     * @param nombre Nombre del aventurero
     * @return true si está en el registro
     */
    public boolean estaRegistrado(String nombre) {
        return aventureros.contains(nombre);
    }

    /**
     * Obtiene el número total de aventureros registrados.
     * 
     * @return Cantidad de aventureros únicos
     */
    public int obtenerTotal() {
        return aventureros.size();
    }

    /**
     * Obtiene el conjunto de todos los aventureros registrados.
     * 
     * @return Set con los nombres de aventureros
     */
    public Set<String> obtenerTodos() {
        return new HashSet<>(aventureros);
    }

    /**
     * Elimina un aventurero del registro.
     * 
     * @param nombre Nombre del aventurero
     * @return true si fue eliminado, false si no estaba registrado
     */
    public boolean eliminar(String nombre) {
        return aventureros.remove(nombre);
    }

    /**
     * Limpia el registro completamente.
     */
    public void limpiar() {
        aventureros.clear();
    }

    @Override
    public String toString() {
        if (aventureros.isEmpty()) {
            return "No hay aventureros registrados";
        }
        StringBuilder sb = new StringBuilder("=== REGISTRO DE AVENTUREROS ===\n");
        sb.append("Total registrados: ").append(aventureros.size()).append("\n\n");
        
        int contador = 1;
        for (String nombre : aventureros) {
            sb.append(contador++).append(". ").append(nombre).append("\n");
        }
        return sb.toString();
    }
}
