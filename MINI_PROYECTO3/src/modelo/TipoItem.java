package modelo;

/**
 * Enumeración que define los tipos de ítems disponibles en el juego.
 * Basado en los tipos de ítems de Dragon Quest VIII.
 */
public enum TipoItem {
    POCION("Poción", "Restaura HP"),
    POCION_FUERTE("Poción Fuerte", "Restaura mucho HP"),
    ELIXIR("Elixir", "Restaura todo HP y MP"),
    ANTIDOTO("Antídoto", "Cura efectos negativos"),
    BOMBA("Bomba", "Causa daño a enemigos"),
    ESPADA("Espada", "Aumenta ataque"),
    ESCUDO("Escudo", "Aumenta defensa"),
    ARMADURA("Armadura", "Aumenta defensa"),
    HIELO("Bola de Hielo", "Hechizo de daño hielo");

    private String nombre;
    private String descripcion;

    TipoItem(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
}
