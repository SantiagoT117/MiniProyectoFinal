package modelo;

/**
 * Interfaz que define el comportamiento agresivo de los personajes.
 * Implementada principalmente por la clase Enemigo.
 * 
 * Los personajes agresivos pueden:
 * - Atacar a un objetivo específico
 * - Usar habilidades especiales ofensivas
 * - Atacar aleatoriamente a héroes
 */
public interface Agresivo {

    /**
     * Ataca a un personaje objetivo.
     * 
     * @param objetivo Personaje a atacar
     */
    void atacar(Personaje objetivo);

    /**
     * Usa una habilidad especial ofensiva contra un objetivo.
     * 
     * @param objetivo Personaje a atacar con la habilidad especial
     */
    void usarHabilidadEspecial(Personaje objetivo);

    /**
     * Selecciona y ataca aleatoriamente a un héroe vivo del array.
     * 
     * @param heroes Array de héroes entre los que elegir
     */
    void atacarAleatorio(Heroe[] heroes);

}
