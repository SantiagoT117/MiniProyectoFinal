package modelo;

/**
 * Clase que representa a un héroe jugable en el juego.
 * Extiende de Personaje e implementa las interfaces Sanador, Tanque y Hechicero,
 * permitiendo que los héroes tengan diferentes roles según su tipo.
 * 
 * Los héroes pueden:
 * - Atacar enemigos
 * - Defender aliados (Guerrero/Paladín)
 * - Curar y revivir (Druida/Paladín)
 * - Lanzar hechizos (Mago/Druida)
 * - Provocar enemigos (Guerrero/Paladín)
 * 
 * Cada habilidad tiene un costo en MP y está restringida a ciertos tipos de héroe.
 */
public class Heroe extends Personaje implements Sanador, Tanque, Hechicero {

    private final Tipo_Heroe tipo;  // Tipo de héroe (define sus habilidades disponibles)
    private int hpMax;              // HP máximo para cálculos de curación y barras de progreso
    private Inventario inventario;  // Inventario de objetos que puede usar en batalla

    /**
     * Constructor del héroe.
     * 
     * @param nombre Nombre del héroe
     * @param tipo Tipo de héroe (GUERRERO, MAGO, DRUIDA, PALADIN)
     * @param hp Puntos de vida iniciales
     * @param mp Puntos de magia iniciales
     * @param ataque Poder de ataque
     * @param defensa Poder de defensa
     * @param velocidad Velocidad (determina orden de turnos)
     */
    public Heroe(String nombre, Tipo_Heroe tipo, int hp, int mp, int ataque, int defensa, int velocidad) {
        super(nombre, hp, mp, ataque, defensa, velocidad);
        this.tipo = tipo;
        this.hpMax = hp;
        this.inventario = new Inventario(); // Inicializar inventario vacío
    }

    public Tipo_Heroe getTipo() {
        return tipo;
    }

    public int getHpMax(){
        return hpMax;
    }

    public void setHpMax(int hpMax) {
        this.hpMax = hpMax; 
    }

    /**
     * Obtiene el inventario del héroe.
     * 
     * @return Inventario del héroe
     */
    public Inventario getInventario() {
        return inventario;
    }


    // ==================== HABILIDADES DE TANQUE (Guerrero/Paladín) ====================

    /**
     * Aumenta la defensa del héroe.
     * Solo disponible para Guerreros y Paladines.
     * Costo: 10 MP
     * 
     * @param extra Cantidad de defensa a añadir
     * @return true si se ejecutó exitosamente, false si no tiene MP o no es del tipo correcto
     */
    @Override
    public boolean aumentarDefensa(int extra) {
        if (!(tipo == Tipo_Heroe.GUERRERO || tipo == Tipo_Heroe.PALADIN)) return false;
        if (mp < 10) return false;

        mp -= 10;
        defensa += extra;
        return true;
    }

    /**
     * Defiende a un aliado, sumando la defensa del héroe a la del aliado.
     * Solo disponible para Guerreros y Paladines.
     * Costo: 10 MP
     * 
     * @param aliado Personaje a defender
     * @return true si se ejecutó exitosamente, false si no puede usar esta habilidad
     */
    @Override
    public boolean defender(Personaje aliado) {
        if (!(tipo == Tipo_Heroe.GUERRERO || tipo == Tipo_Heroe.PALADIN)) return false;
        if (mp < 10) return false;

        mp -= 10;

        // Remover defensa anterior si existía
        if (aliado.estaSiendoDefendido()) aliado.removerDefensa();

        aliado.recibirDefensa(this);
        return true;
    }

    /**
     * Provoca a un enemigo, obligándolo a atacar solo a este héroe.
     * Solo disponible para Guerreros y Paladines.
     * Costo: 5 MP
     * 
     * @param enemigo Enemigo a provocar
     * @return true si se ejecutó exitosamente
     */
    @Override
    public boolean provocarEnemigo(Personaje enemigo) {
        if (!(tipo == Tipo_Heroe.GUERRERO || tipo == Tipo_Heroe.PALADIN)) return false;
        if (mp < 5) return false;

        mp -= 5;

        // Remover provocación anterior si existía
        if (enemigo.estaProvocado()) enemigo.removerProvocacion();
        enemigo.serProvocado(this);
        return true;
    }

    /**
     * Provoca a todos los enemigos vivos.
     * Solo disponible para Guerreros y Paladines.
     * Costo: 3 MP por cada enemigo vivo
     * 
     * @param enemigos Array de enemigos a provocar
     * @return true si se ejecutó exitosamente
     */
    public boolean provocarTodos(Personaje[] enemigos) {
        if (!(tipo == Tipo_Heroe.GUERRERO || tipo == Tipo_Heroe.PALADIN)) return false;

        // Calcular cuántos enemigos están vivos
        int vivos = 0;
        for (Personaje e : enemigos) {
            if (e != null && e.esta_vivo()) vivos++;
        }

        // Verificar si tiene suficiente MP
        int costo = vivos * 3;
        if (mp < costo) return false;

        mp -= costo;

        // Aplicar provocación a todos los enemigos vivos
        for (Personaje e : enemigos) {
            if (e != null && e.esta_vivo()) {
                if (e.estaProvocado()) e.removerProvocacion();
                e.serProvocado(this);
            }
        }

        return true;
    }

    // ==================== HABILIDADES DE SANADOR (Druida/Paladín) ====================

    /**
     * Cura a un objetivo, restaurando 30 HP.
     * Solo disponible para Druidas y Paladines.
     * Costo: 15 MP
     * 
     * @param objetivo Personaje a curar
     * @return true si se ejecutó exitosamente
     */
    @Override
    public boolean curar(Personaje objetivo) {
        if (!(tipo == Tipo_Heroe.DRUIDA || tipo == Tipo_Heroe.PALADIN)) return false;
        if (mp < 15) return false;

        mp -= 15;
        objetivo.setHp(objetivo.getHp() + 30);
        return true;
    }

    /**
     * Revive a un personaje muerto con 50 HP.
     * Solo disponible para Paladines.
     * Costo: 25 MP
     * 
     * @param objetivo Personaje a revivir
     * @return true si se ejecutó exitosamente, false si el objetivo está vivo o no es Paladín
     */
    @Override
    public boolean revivir(Personaje objetivo) {
        if (tipo != Tipo_Heroe.PALADIN) return false;
        if (objetivo.esta_vivo()) return false;
        if (mp < 25) return false;

        mp -= 25;
        objetivo.setHp(50);
        return true;
    }

    /**
     * Restaura 25 MP a un objetivo.
     * Solo disponible para Druidas.
     * Costo: 20 MP
     * 
     * @param objetivo Personaje a restaurar MP
     * @return true si se ejecutó exitosamente
     */
    @Override
    public boolean restaurarMana(Personaje objetivo) {
        if (tipo != Tipo_Heroe.DRUIDA) return false;
        if (mp < 20) return false;

        mp -= 20;
        objetivo.setMp(objetivo.getMp() + 25);
        return true;
    }

    /**
     * Elimina todos los efectos negativos de un objetivo.
     * Solo disponible para Druidas y Paladines.
     * 
     * @param objetivo Personaje a limpiar efectos
     * @return true si se ejecutó exitosamente
     */
    @Override
    public boolean eliminarEfectoNegativo(Personaje objetivo) {
        if (!(tipo == Tipo_Heroe.DRUIDA || tipo == Tipo_Heroe.PALADIN)) return false;

        objetivo.limpiarEfectos();
        return true;
    }

    // ==================== HABILIDADES DE HECHICERO (Mago/Druida) ====================

    /**
     * Lanza un hechizo que causa 40 de daño al objetivo.
     * Solo disponible para Magos y Druidas.
     * Costo: 20 MP
     * 
     * @param objetivo Personaje a atacar
     * @return true si se ejecutó exitosamente
     */
    @Override
    public boolean LanzaHechizoSueño(Personaje objetivo) {
        if (!(tipo == Tipo_Heroe.MAGO || tipo == Tipo_Heroe.DRUIDA)) return false;
        if (mp < 20) return false;

        mp -= 20;
        objetivo.recibir_daño(40);
        return true;
    }

    /**
     * Lanza un hechizo que aumenta el ataque del objetivo en 60.
     * Solo disponible para Magos y Druidas.
     * Costo: 20 MP
     * 
     * @param objetivo Personaje a reforzar
     * @return true si se ejecutó exitosamente
     */
    @Override
    public boolean LanzaHechizoRefuerzo(Personaje objetivo) {
        if (!(tipo == Tipo_Heroe.MAGO || tipo == Tipo_Heroe.DRUIDA)) return false;
        if (mp < 20) return false;

        mp -= 20;
        objetivo.aumentarAtaque(60);
        return true;
    }

    /**
     * Lanza un hechizo que paraliza al objetivo por 1 turno.
     * Solo disponible para Magos y Druidas.
     * Costo: 25 MP
     * 
     * @param objetivo Personaje a paralizar
     * @return true si se ejecutó exitosamente
     */
    @Override
    public boolean LanzaHechizoParalisis(Personaje objetivo) {
        if (!(tipo == Tipo_Heroe.MAGO || tipo == Tipo_Heroe.DRUIDA)) return false;
        if (mp < 25) return false;

        mp -= 25;
        objetivo.aplicarParalisis(1);
        return true;
    }

    // ==================== MÉTODOS DE ATAQUE BÁSICO ====================

    /**
     * Ataque básico del héroe a un objetivo.
     * Calcula el daño como: ataque - defensa del objetivo (mínimo 1).
     * 
     * @param objetivo Personaje a atacar
     * @return Daño infligido, o -1 si el objetivo es inválido
     */
    public int atacar(Personaje objetivo) {
        if (objetivo == null || !objetivo.esta_vivo()) return -1;

        int daño = ataque - objetivo.getDefensa();
        if (daño < 1) daño = 1;

        objetivo.recibir_daño(daño);
        return daño;
    }

    /**
     * Representación en String del héroe con todos sus atributos.
     * 
     * @return String formateado con la información del héroe
     */
    @Override
    public String toString() {
        return "Heroe{" + nombre +
                ", tipo=" + tipo +
                ", HP=" + hp +
                ", MP=" + mp +
                ", ATK=" + ataque +
                ", DEF=" + defensa +
                ", VEL=" + velocidad + "}";
    }

    /**
     * Método abstracto heredado de Personaje.
     * No implementado ya que los héroes son controlados por el jugador.
     */
    @Override
    public void elegirAccion() {
        throw new UnsupportedOperationException("Los héroes son controlados por el jugador");
    }
}
