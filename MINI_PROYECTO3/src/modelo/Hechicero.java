package modelo;

/**
 * Interfaz que define las habilidades mágicas ofensivas y de soporte.
 * Implementada por la clase Heroe (tipos Mago y Druida).
 * 
 * Los hechiceros pueden lanzar diferentes tipos de hechizos:
 * - Hechizos de daño directo
 * - Hechizos de refuerzo para aliados
 * - Hechizos de control de masas (parálisis)
 */
public interface Hechicero {

    /**
     * Lanza un hechizo de daño mágico al objetivo.
     * 
     * @param objetivo Personaje a atacar con el hechizo
     * @return true si el hechizo fue lanzado exitosamente, false si falló
     */
    boolean LanzaHechizoSueño(Personaje objetivo);
    
    /**
     * Lanza un hechizo que aumenta el ataque del objetivo.
     * 
     * @param objetivo Personaje a reforzar
     * @return true si el hechizo fue lanzado exitosamente, false si falló
     */
    boolean LanzaHechizoRefuerzo(Personaje objetivo);
    
    /**
     * Lanza un hechizo que paraliza al objetivo por varios turnos.
     * 
     * @param objetivo Personaje a paralizar
     * @return true si el hechizo fue lanzado exitosamente, false si falló
     */
    boolean LanzaHechizoParalisis(Personaje objetivo);

}
