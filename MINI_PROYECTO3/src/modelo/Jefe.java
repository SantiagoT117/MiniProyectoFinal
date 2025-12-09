package modelo;

/**
 * Interfaz que define el comportamiento especial de enemigos jefes.
 * Implementada por la clase JefeEnemigo.
 * 
 * Los jefes tienen habilidades únicas:
 * - Habilidades especiales más poderosas
 * - Ataques que requieren preparación (turnos de carga)
 * - Ataques de área que afectan a todos los héroes
 */
public interface Jefe {

    /**
     * Usa una habilidad especial única del jefe contra un objetivo.
     * Estas habilidades suelen ser más poderosas que las de enemigos normales.
     * 
     * @param objetivo Personaje a atacar con la habilidad especial
     */
    void usarHabilidadEspecial(Personaje objetivo);

    /**
     * Retorna el número de turnos que debe esperar antes de poder atacar.
     * Algunos jefes requieren "cargar" sus ataques durante varios turnos.
     * 
     * @return Número de turnos de espera
     */
    int TurnosParaAtacar();

    /**
     * Ejecuta un ataque de área que afecta a todos los héroes simultáneamente.
     * Habilidad característica de los jefes.
     */
    void AtacarATodos();

}

