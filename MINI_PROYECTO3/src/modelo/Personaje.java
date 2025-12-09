package modelo;

/**
 * Clase abstracta que representa a cualquier personaje del juego (héroe o enemigo).
 * Contiene atributos y comportamientos comunes a todos los personajes.
 * 
 * Un personaje tiene:
 * - Atributos básicos: HP, MP, ataque, defensa, velocidad
 * - Estados: vivo/muerto, paralizado, durmiendo, siendo defendido, provocado
 * - Relaciones: defensor que lo protege, provocador que lo obliga a atacar
 * 
 * Esta clase implementa la lógica de:
 * - Recibir daño considerando defensa propia y del tanque protector
 * - Gestionar efectos de estado (parálisis, sueño, provocación)
 * - Determinar si puede actuar según sus estados
 * - Seleccionar objetivos para atacar
 */
public abstract class Personaje {

    // Atributos básicos del personaje
    protected String nombre;
    protected int hp;           // Puntos de vida
    protected int mp;           // Puntos de magia
    protected int ataque;       // Poder de ataque
    protected int defensa;      // Poder de defensa
    protected int velocidad;    // Determina el orden de turno
    
    // Estados del personaje
    protected boolean esta_vivo = true;
    protected boolean esta_paralizado = false;
    protected boolean esta_durmiendo = false;
    protected boolean siendo_defendido = false;  // Si un tanque lo está protegiendo
    protected boolean esta_provocado = false;     // Si está obligado a atacar a alguien

    // Duración de efectos de estado (en turnos)
    protected int turnosParalisis = 0;
    protected int turnosSueno = 0;
    
    // Referencias a otros personajes que afectan a este
    protected Personaje provocador = null;  // Quién lo está provocando
    protected Personaje defensor = null;    // Quién lo está defendiendo
    
    // Getters para acceder a los atributos del personaje
    public String getNombre() { return nombre; }
    public int getHp() { return hp; }
    public int getMp() { return mp; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getVelocidad() { return velocidad; }
    /**
     * Establece los puntos de vida del personaje.
     * Si llega a 0 o menos, el personaje muere y se elimina cualquier defensa activa.
     * 
     * @param valor Nuevos puntos de vida (no puede ser negativo)
     */
    public void setHp(int valor) {
        hp = Math.max(0, valor);
        esta_vivo = hp > 0;
        if (!esta_vivo) removerDefensa();
    }

    /**
     * Establece los puntos de magia del personaje.
     * 
     * @param valor Nuevos puntos de magia (no puede ser negativo)
     */
    public void setMp(int valor) {
        mp = Math.max(0, valor);
    }

    /**
     * Constructor de la clase Personaje.
     * 
     * @param nombre Nombre del personaje
     * @param hp Puntos de vida iniciales
     * @param mp Puntos de magia iniciales
     * @param ataque Poder de ataque
     * @param defensa Poder de defensa
     * @param velocidad Velocidad (determina el orden de turnos)
     */
    public Personaje(String nombre, int hp, int mp, int ataque, int defensa, int velocidad) {
        this.nombre = nombre;
        this.hp = hp;
        this.mp = mp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.esta_vivo = hp > 0;
    }

    // Getters de estado
    public boolean esta_vivo() { return esta_vivo; }
    public boolean estaParalizado() { return esta_paralizado; }
    public boolean estaDurmiendo() { return esta_durmiendo; }
    public boolean estaSiendoDefendido() { return siendo_defendido; }
    public boolean estaProvocado() { return esta_provocado; }

    /**
     * Calcula y aplica el daño recibido por el personaje.
     * Considera la defensa propia y, si está siendo defendido, también la del defensor.
     * El daño mínimo es siempre 1.
     * Si el personaje muere, se elimina cualquier defensa activa.
     * 
     * @param cantidad Cantidad de daño base recibido
     * @return Daño final aplicado después de considerar la defensa
     */
    public int recibir_daño(int cantidad) {

        int defensaTotal = defensa;

        // Si está siendo defendido por un tanque, sumar la defensa del tanque
        if (siendo_defendido && defensor != null && defensor.esta_vivo) {
            defensaTotal += defensor.getDefensa();
        }

        int dañoFinal = cantidad - defensaTotal;

        // Garantizar un daño mínimo de 1
        if (dañoFinal < 1) dañoFinal = 1;

        setHp(hp - dañoFinal);

        // Si muere como resultado del daño, eliminar la defensa
        if (!esta_vivo) removerDefensa();

        return dañoFinal;
    }

    /**
     * Aumenta el ataque del personaje.
     * 
     * @param aumento Cantidad a aumentar (debe ser positiva)
     */
    protected void aumentarAtaque(int aumento) {
        if (aumento > 0) ataque += aumento;
    }

    // ==================== GESTIÓN DE EFECTOS DE ESTADO ====================

    /**
     * Aplica el efecto de parálisis al personaje.
     * Si ya estaba paralizado, se toma la duración mayor.
     * 
     * @param turnos Número de turnos que durará la parálisis
     */
    public void aplicarParalisis(int turnos) {
        if (turnos > 0) {
            this.turnosParalisis = Math.max(this.turnosParalisis, turnos);
        }
    }

    /**
     * Aplica el efecto de sueño al personaje.
     * Solo funciona si el personaje está vivo.
     * 
     * @return true si se aplicó exitosamente, false si el personaje está muerto
     */
    public boolean aplicarDormir() {
        if (!esta_vivo) return false;
        esta_durmiendo = true;
        return true;
    }

    /**
     * Limpia todos los efectos de estado negativos del personaje
     * (parálisis, sueño, provocación).
     */
    public void limpiarEfectos() {
        esta_paralizado = false;
        esta_durmiendo  = false;
        esta_provocado  = false;
        provocador = null;
    }

    /**
     * Verifica si el personaje puede actuar en este turno.
     * Reduce automáticamente los contadores de efectos de estado.
     * 
     * @return false si está paralizado o dormido, true si puede actuar
     */
    public boolean puedeActuar() {

        // Si está paralizado, reducir turno y no puede actuar
        if (turnosParalisis > 0) {
            turnosParalisis--;
            return false;
        }

        // Si está dormido, reducir turno y no puede actuar
        if (turnosSueno > 0) {
            turnosSueno--;
            return false;
        }

        return true;
    }

    /**
     * Verifica si el personaje puede atacar.
     * 
     * @return true si está vivo y no tiene efectos de estado que impidan atacar
     */
    public boolean puedeAtacar() {
        return esta_vivo && (turnosParalisis == 0) && (turnosSueno == 0);
    }

    // ==================== SISTEMA DE DEFENSA ====================

    /**
     * Hace que este personaje sea defendido por un tanque.
     * La defensa del tanque se sumará a la propia al recibir daño.
     * 
     * @param tanque El personaje que defenderá a este
     * @return true si se aplicó la defensa, false si el personaje está muerto
     */
    public boolean recibirDefensa(Personaje tanque) {
        if (!esta_vivo) return false;
        siendo_defendido = true;
        defensor = tanque;
        return true;
    }

    /**
     * Elimina cualquier defensa activa sobre este personaje.
     */
    public void removerDefensa() {
        siendo_defendido = false;
        defensor = null;
    }

    /**
     * Obtiene el personaje que está defendiendo a este.
     * 
     * @return El defensor, o null si no está siendo defendido
     */
    public Personaje getDefensor() { return defensor; }

    // ==================== SISTEMA DE PROVOCACIÓN ====================

    /**
     * Hace que este personaje sea provocado por un tanque.
     * Un personaje provocado solo puede atacar a su provocador.
     * 
     * @param tanque El personaje que provoca a este
     * @return true si se aplicó la provocación, false si el personaje está muerto
     */
    public boolean serProvocado(Personaje tanque) {
        if (!esta_vivo) return false;
        esta_provocado = true;
        provocador = tanque;
        return true;
    }

    /**
     * Elimina cualquier provocación activa sobre este personaje.
     */
    public void removerProvocacion() {
        esta_provocado = false;
        provocador = null;
    }

    /**
     * Obtiene el personaje que está provocando a este.
     * 
     * @return El provocador, o null si no está provocado
     */
    public Personaje getProvocador() { return provocador; }

    /**
     * Selecciona un objetivo de ataque entre un array de posibles objetivos.
     * Si está provocado, solo puede atacar al provocador.
     * Si no, selecciona el primer personaje vivo del array.
     * 
     * @param posibles Array de posibles objetivos
     * @return El objetivo seleccionado, o null si no hay objetivos válidos
     */
    public Personaje seleccionarObjetivo(Personaje[] posibles) {
        // Si está provocado, solo puede atacar al provocador
        if (esta_provocado && provocador != null && provocador.esta_vivo) {
            return provocador;
        }
        // Selecciona el primer personaje vivo
        for (Personaje p : posibles) {
            if (p != null && p.esta_vivo()) return p;
        }
        return null;
    }

    /**
     * Ataca a un objetivo seleccionado del array de posibles objetivos.
     * Calcula el daño como: ataque - defensa del objetivo (mínimo 1).
     * 
     * @param posibles Array de posibles objetivos
     * @return El daño infligido, o -1 si no había objetivos válidos
     */
    public int atacar(Personaje[] posibles) {
        Personaje obj = seleccionarObjetivo(posibles);
        if (obj == null) return -1;

        int daño = ataque - obj.getDefensa();
        if (daño < 1) daño = 1;

        obj.recibir_daño(daño);
        return daño;
    }

    /**
     * Método abstracto que debe ser implementado por las subclases.
     * Define la lógica de decisión de acciones del personaje.
     */
    public abstract void elegirAccion();
}
