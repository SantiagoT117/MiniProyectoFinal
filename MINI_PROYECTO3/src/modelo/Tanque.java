package modelo;

/**
 * Interfaz que define las habilidades defensivas y de protección.
 * Implementada por la clase Heroe (tipos Guerrero y Paladín).
 * 
 * Los tanques pueden:
 * - Defender a aliados, sumando su defensa a la del protegido
 * - Provocar enemigos para que solo los ataquen a ellos
 * - Aumentar su propia defensa
 */
public interface Tanque {

    /**
     * Defiende a un aliado, haciendo que el tanque absorba parte del daño.
     * La defensa del tanque se suma a la del aliado al calcular el daño recibido.
     * 
     * @param aliado Personaje a defender
     * @return true si la defensa fue exitosa, false si falló
     */
    boolean defender(Personaje aliado);

    /**
     * Provoca a un enemigo, obligándolo a atacar solo al tanque.
     * 
     * @param enemigo Enemigo a provocar
     * @return true si la provocación fue exitosa, false si falló
     */
    boolean provocarEnemigo(Personaje enemigo);

    /**
     * Aumenta la defensa del tanque.
     * 
     * @param cantidad Cantidad de defensa a aumentar
     * @return true si el aumento fue exitoso, false si falló
     */
    boolean aumentarDefensa(int cantidad);

}
