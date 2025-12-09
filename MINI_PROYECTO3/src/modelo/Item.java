package modelo;

/**
 * Clase que representa un objeto/ítem que puede ser usado en batalla.
 * Basado en los ítems del juego original Dragon Quest VIII.
 * 
 * Cada ítem tiene:
 * - Nombre: identificador único del ítem
 * - Tipo: categoría (poción, arma, armadura, llave, etc.)
 * - Efecto: descripción de lo que hace
 * - Valor de efecto: cantidad numérica (HP restaurados, daño, etc.)
 */
public class Item {
    
    private String nombre;
    private TipoItem tipo;
    private String efecto;
    private int valorEfecto;

    /**
     * Constructor del ítem.
     * 
     * @param nombre Nombre del ítem
     * @param tipo Tipo de ítem (enum TipoItem)
     * @param efecto Descripción del efecto
     * @param valorEfecto Valor numérico del efecto
     */
    public Item(String nombre, TipoItem tipo, String efecto, int valorEfecto) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.efecto = efecto;
        this.valorEfecto = valorEfecto;
    }

    // Getters
    public String getNombre() { return nombre; }
    public TipoItem getTipo() { return tipo; }
    public String getEfecto() { return efecto; }
    public int getValorEfecto() { return valorEfecto; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ") - " + efecto;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return nombre.equals(item.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}
