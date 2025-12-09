package modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Clase que representa el inventario de un héroe.
 * 
 * ESTRUCTURA DE DATOS: HashMap<String, Integer>
 * 
 * JUSTIFICACIÓN:
 * - HashMap es la estructura ideal para un inventario porque proporciona:
 *   * Acceso O(1) al buscar un ítem por nombre (operación muy frecuente)
 *   * Inserción y eliminación O(1) (cuando se añaden/usan ítems)
 *   * Permite duplicados de ítems (valor = cantidad)
 *   * Sin necesidad de ordenamiento
 * 
 * ALTERNATIVAS RECHAZADAS:
 * - ArrayList: Búsqueda O(n), necesitaría recorrer todo
 * - TreeMap: Acceso O(log n), ordenamiento innecesario
 * - LinkedList: Búsqueda O(n), optimizado para inserción en extremos
 * - HashSet: No permite duplicados ni contar cantidad
 * 
 * MÁXIMO 5 ÍTEMS por héroe según especificaciones del proyecto.
 */
public class Inventario {
    
    private HashMap<String, Integer> items;
    private static final int MAX_ITEMS = 5;
    private static final int MAX_CANTIDAD_POR_ITEM = 99;

    /**
     * Constructor que inicializa un inventario vacío.
     */
    public Inventario() {
        this.items = new HashMap<>();
    }

    /**
     * Añade un ítem al inventario.
     * Si el ítem ya existe, incrementa la cantidad.
     * 
     * @param nombre Nombre del ítem a añadir
     * @param cantidad Cantidad a añadir
     * @return true si se añadió exitosamente, false si se excede el límite
     */
    public boolean agregarItem(String nombre, int cantidad) {
        if (cantidad <= 0) return false;

        if (items.containsKey(nombre)) {
            // Ítem ya existe: incrementar cantidad
            int cantidadActual = items.get(nombre);
            int nuevaCantidad = Math.min(cantidadActual + cantidad, MAX_CANTIDAD_POR_ITEM);
            items.put(nombre, nuevaCantidad);
            return true;
        } else {
            // Ítem nuevo: verificar límite de ítems diferentes
            if (items.size() >= MAX_ITEMS) {
                return false; // Inventario lleno
            }
            items.put(nombre, Math.min(cantidad, MAX_CANTIDAD_POR_ITEM));
            return true;
        }
    }

    /**
     * Usa (consume) una cantidad de un ítem del inventario.
     * Si la cantidad llega a 0, el ítem se elimina.
     * 
     * @param nombre Nombre del ítem a usar
     * @param cantidad Cantidad a consumir
     * @return true si se usó exitosamente, false si no hay suficiente
     */
    public boolean usarItem(String nombre, int cantidad) {
        if (!items.containsKey(nombre)) return false;
        
        int cantidadActual = items.get(nombre);
        if (cantidadActual < cantidad) return false;

        int nuevaCantidad = cantidadActual - cantidad;
        if (nuevaCantidad == 0) {
            items.remove(nombre);
        } else {
            items.put(nombre, nuevaCantidad);
        }
        return true;
    }

    /**
     * Obtiene la cantidad de un ítem en el inventario.
     * 
     * @param nombre Nombre del ítem
     * @return Cantidad del ítem, o 0 si no existe
     */
    public int obtenerCantidad(String nombre) {
        return items.getOrDefault(nombre, 0);
    }

    /**
     * Verifica si el inventario contiene un ítem específico.
     * 
     * @param nombre Nombre del ítem
     * @return true si existe en el inventario
     */
    public boolean contiene(String nombre) {
        return items.containsKey(nombre);
    }

    /**
     * Obtiene el conjunto de nombres de todos los ítems en el inventario.
     * 
     * @return Set con los nombres de los ítems
     */
    public Set<String> obtenerItems() {
        return items.keySet();
    }

    /**
     * Obtiene el mapa completo de ítems y sus cantidades.
     * 
     * @return HashMap con ítems y cantidades
     */
    public Map<String, Integer> obtenerInventarioCompleto() {
        return new HashMap<>(items);
    }

    /**
     * Verifica si el inventario está lleno.
     * 
     * @return true si contiene 5 ítems diferentes
     */
    public boolean estaLleno() {
        return items.size() >= MAX_ITEMS;
    }

    /**
     * Obtiene la cantidad de espacios ocupados en el inventario.
     * 
     * @return Número de ítems diferentes
     */
    public int obtenerEspacios() {
        return items.size();
    }

    /**
     * Limpia todo el inventario.
     */
    public void limpiar() {
        items.clear();
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "Inventario vacío";
        }
        StringBuilder sb = new StringBuilder("=== INVENTARIO ===\n");
        for (String nombre : items.keySet()) {
            sb.append(nombre).append(" x").append(items.get(nombre)).append("\n");
        }
        return sb.toString();
    }
}
