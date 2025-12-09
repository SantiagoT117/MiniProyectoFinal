package modelo;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase auxiliar para inicializar ítems y equiparlos a los héroes.
 * Define todos los ítems disponibles en el juego.
 * 
 * Ítems disponibles (basados en Dragon Quest VIII):
 * 1. Poción - Restaura 30 HP
 * 2. Poción Fuerte - Restaura 60 HP
 * 3. Elixir - Restaura todo HP y MP
 * 4. Antídoto - Cura efectos negativos
 * 5. Bomba - Causa 40 daño a enemigos
 * 6. Espada Sagrada - Aumenta ataque en 30
 * 7. Escudo Magnífico - Aumenta defensa en 20
 * 8. Armadura Plateada - Aumenta defensa en 15
 * 9. Bola de Hielo - Hechizo hielo 25 daño
 */
public class GestorObjetos {

    // Mapa estático con todos los ítems del juego
    private static final Map<String, Item> itemsDisponibles = new HashMap<>();

    static {
        // Inicializar todos los ítems disponibles
        itemsDisponibles.put("Poción", new Item("Poción", TipoItem.POCION, "Restaura 30 HP", 30));
        itemsDisponibles.put("Poción Fuerte", new Item("Poción Fuerte", TipoItem.POCION_FUERTE, "Restaura 60 HP", 60));
        itemsDisponibles.put("Elixir", new Item("Elixir", TipoItem.ELIXIR, "Restaura todo HP y MP", 999));
        itemsDisponibles.put("Antídoto", new Item("Antídoto", TipoItem.ANTIDOTO, "Cura efectos negativos", 1));
        itemsDisponibles.put("Bomba", new Item("Bomba", TipoItem.BOMBA, "Causa 40 daño a enemigos", 40));
        itemsDisponibles.put("Espada Sagrada", new Item("Espada Sagrada", TipoItem.ESPADA, "Aumenta ataque en 30", 30));
        itemsDisponibles.put("Escudo Magnífico", new Item("Escudo Magnífico", TipoItem.ESCUDO, "Aumenta defensa en 20", 20));
        itemsDisponibles.put("Armadura Plateada", new Item("Armadura Plateada", TipoItem.ARMADURA, "Aumenta defensa en 15", 15));
        itemsDisponibles.put("Bola de Hielo", new Item("Bola de Hielo", TipoItem.HIELO, "Hechizo hielo 25 daño", 25));
    }

    /**
     * Obtiene un ítem del catálogo por su nombre.
     * 
     * @param nombre Nombre del ítem
     * @return Item si existe, null si no
     */
    public static Item obtenerItem(String nombre) {
        return itemsDisponibles.get(nombre);
    }

    /**
     * Obtiene el mapa de todos los ítems disponibles.
     * 
     * @return Map con todos los ítems
     */
    public static Map<String, Item> obtenerTodosItems() {
        return new HashMap<>(itemsDisponibles);
    }

    /**
     * Equipa a un héroe con ítems iniciales.
     * Cada héroe recibe 5 ítems al inicio del juego.
     * 
     * @param heroe Héroe a equipar
     */
    public static void equiparHeroeInicial(Heroe heroe) {
        Inventario inv = heroe.getInventario();
        
        // Todos los héroes reciben una poción al inicio
        inv.agregarItem("Poción", 3);
        
        // Según el tipo de héroe, reciben ítems especiales
        switch (heroe.getTipo()) {
            case GUERRERO:
                inv.agregarItem("Espada Sagrada", 1);
                inv.agregarItem("Escudo Magnífico", 1);
                inv.agregarItem("Poción Fuerte", 2);
                break;
            case MAGO:
                inv.agregarItem("Bola de Hielo", 2);
                inv.agregarItem("Elixir", 1);
                break;
            case DRUIDA:
                inv.agregarItem("Antídoto", 2);
                inv.agregarItem("Elixir", 1);
                inv.agregarItem("Poción Fuerte", 1);
                break;
            case PALADIN:
                inv.agregarItem("Escudo Magnífico", 1);
                inv.agregarItem("Armadura Plateada", 1);
                inv.agregarItem("Elixir", 1);
                break;
        }
    }

    /**
     * Obtiene una descripción de todos los ítems disponibles.
     * 
     * @return String con información de todos los ítems
     */
    public static String obtenerCatalogo() {
        StringBuilder sb = new StringBuilder("=== CATÁLOGO DE ÍTEMS ===\n");
        for (Item item : itemsDisponibles.values()) {
            sb.append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}
