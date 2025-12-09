package modelo;

/**
 * Interfaz que define las habilidades de curación y soporte.
 * Implementada por la clase Heroe (tipos Druida y Paladín).
 * 
 * Los sanadores pueden:
 * - Curar a aliados restaurando HP
 * - Revivir personajes caídos
 * - Restaurar mana (MP) de otros personajes
 * - Eliminar efectos negativos de estado
 */
public interface Sanador {

    /**
     * Cura a un personaje objetivo, restaurando sus puntos de vida.
     * 
     * @param objetivo Personaje a curar
     * @return true si la curación fue exitosa, false si falló
     */
    boolean curar(Personaje objetivo);

    /**
     * Revive a un personaje muerto.
     * 
     * @param objetivo Personaje a revivir
     * @return true si la resurrección fue exitosa, false si falló
     */
    boolean revivir(Personaje objetivo);

    /**
     * Restaura los puntos de mana de un personaje.
     * 
     * @param objetivo Personaje al que restaurar MP
     * @return true si la restauración fue exitosa, false si falló
     */
    boolean restaurarMana(Personaje objetivo);

    /**
     * Elimina todos los efectos negativos de un personaje
     * (parálisis, sueño, provocación, etc.).
     * 
     * @param objetivo Personaje a limpiar
     * @return true si se eliminaron los efectos, false si falló
     */
    boolean eliminarEfectoNegativo(Personaje objetivo);

}
