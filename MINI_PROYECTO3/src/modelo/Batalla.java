package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase que gestiona el estado de una batalla entre héroes y enemigos.
 * 
 * Responsabilidades:
 * - Almacenar los equipos de héroes y enemigos
 * - Controlar el turno actual de la batalla
 * - Guardar y cargar partidas usando archivos de texto
 * - Mantener el estado de si la batalla ha terminado
 * 
 * El sistema de guardado utiliza BufferedWriter/BufferedReader
 * para serializar el estado de la batalla en formato CSV.
 */
public class Batalla {

    // Arrays que contienen los equipos participantes
    private final Heroe[] heroes; 
    private final Enemigo[] enemigos;

    // Control del flujo de la batalla
    private int turnoActual;
    private boolean batallaTerminada;

    /**
     * Constructor de la batalla.
     * 
     * @param heroes Array de héroes que participan en la batalla
     * @param enemigos Array de enemigos que participan en la batalla
     */
    public Batalla(Heroe[] heroes, Enemigo[] enemigos){
        this.heroes = heroes;
        this.enemigos = enemigos;
        this.turnoActual = 0;
        this.batallaTerminada = false;
    }

    /**
     * Guarda el estado actual de la batalla en un archivo de texto.
     * 
     * Formato del archivo:
     * - Primera línea: "TURNO,<número>"
     * - Líneas de héroes: "HEROE,nombre,hp,mp,ataque,defensa,velocidad,tipo"
     * - Líneas de enemigos: "ENEMIGO,nombre,hp,mp,ataque,defensa,velocidad,tipo"
     * 
     * Usa BufferedWriter con FileWriter como intermediario para escribir
     * línea por línea el estado de cada personaje.
     * 
     * @param archivo Ruta del archivo donde se guardará la partida
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void guardarpartida(String archivo) throws IOException{
    // El BufferedWriter escribe línea por línea todos los datos de la batalla
    // Guarda desde el punto donde se decidió guardar: datos de personajes y turno actual

    BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

        // Guardar el turno actual como primera línea
        writer.write("TURNO," + turnoActual);
        writer.newLine();

        // Guardar todos los héroes con sus atributos
        for (Heroe h : heroes) {
            writer.write(
                "HEROE," +
                h.getNombre() + "," +
                h.getHp() + "," +
                h.getMp() + "," +
                h.getAtaque() + "," +
                h.getDefensa() + "," +
                h.getVelocidad() + "," +
                h.getTipo()
            );
            writer.newLine();
        }

        // Guardar todos los enemigos con sus atributos
        for (Enemigo e : enemigos) {
            writer.write(
                "ENEMIGO," +
                e.getNombre() + "," +
                e.getHp() + "," +
                e.getMp() + "," +
                e.getAtaque() + "," +
                e.getDefensa() + "," +
                e.getVelocidad() + "," +
                e.getTipo()
            );
            writer.newLine();
        }

        writer.close();
    }

    /**
     * Carga una partida guardada desde un archivo de texto.
     * 
     * Lee línea por línea el archivo y reconstruye el estado de la batalla:
     * - Restaura el turno actual
     * - Recrea todos los héroes con sus atributos guardados
     * - Recrea todos los enemigos con sus atributos guardados
     * 
     * Usa BufferedReader con FileReader como intermediario.
     * Procesa cada línea dividiéndola por comas y usando switch para
     * determinar el tipo de dato (TURNO, HEROE, ENEMIGO).
     * 
     * @param archivo Ruta del archivo desde donde se cargará la partida
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public void cargarpartida(String archivo) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;

        // Variables para rastrear la posición en los arrays al restaurar
        int iHeroe = 0;
        int iEnemigo = 0;

        // Leer el archivo línea por línea 
        while ((linea = br.readLine()) != null) {

            // Dividir cada línea por comas para obtener los campos
            String[] datos = linea.split(",");

            // Detectar el tipo de línea (TURNO, HEROE o ENEMIGO)
            switch (datos[0]) {

                case "TURNO":
                    // parseInt convierte una cadena de texto en entero 
                    this.turnoActual = Integer.parseInt(datos[1]);
                    break;

                case "HEROE":
                    // Reconstruir cada héroe con sus datos guardados
                    // valueOf convierte un texto en valor del enum correspondiente
                    heroes[iHeroe] = new Heroe(
                        datos[1],                          // nombre
                        Tipo_Heroe.valueOf(datos[7]),      // tipo
                        Integer.parseInt(datos[2]),        // hp
                        Integer.parseInt(datos[3]),        // mp
                        Integer.parseInt(datos[4]),        // ataque
                        Integer.parseInt(datos[5]),        // defensa
                        Integer.parseInt(datos[6])         // velocidad
                    );
                    iHeroe++;
                    break;

                case "ENEMIGO":
                    // Reconstruir cada enemigo con sus datos guardados
                    enemigos[iEnemigo] = new Enemigo(
                        datos[1],                          // nombre
                        Integer.parseInt(datos[2]),        // HP
                        Integer.parseInt(datos[3]),        // MP
                        Integer.parseInt(datos[4]),        // ataque
                        Integer.parseInt(datos[5]),        // defensa
                        Integer.parseInt(datos[6]),        // velocidad
                        Tipo_Enemigo.valueOf(datos[7])     // tipo
                    );
                    iEnemigo++;
                    break;
            }
        }

        br.close();
    }

    // ==================== GETTERS Y SETTERS ====================
    
    public Heroe[] getEquipoHeroes() { return heroes; }
    public Enemigo[] getEquipoEnemigos() { return enemigos; }
    public boolean isBatallaTerminada() { return batallaTerminada; }
    public int getTurnoActual() { return turnoActual; }

    public void setBatallaTerminada(boolean batallaTerminada){
         this.batallaTerminada = batallaTerminada; }
         
    public void setTurnoActual(int turnoActual) {
         this.turnoActual = turnoActual; }

    // ==================== MÉTODOS AUXILIARES ====================
         
    /**
     * Inicializa la batalla a su estado inicial.
     * Resetea el turno a 1 y marca la batalla como no terminada.
     */
    public void iniciar() {
        this.turnoActual = 1;
        this.batallaTerminada = false;
    }
            
    /**
     * Marca la batalla como finalizada.
     */
    public void finalizar() {
        this.batallaTerminada = true;
    }
    
    /**
     * Avanza al siguiente turno si la batalla no ha terminado.
     */
    public void siguienteTurno() {
        if (!batallaTerminada) {
            turnoActual++;
        }
    }

}
