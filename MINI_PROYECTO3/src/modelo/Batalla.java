package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Batalla {

    // cambio de nombre de listas para mayor facilidad
    private final Heroe[] heroes; 
    private final Enemigo[] enemigos;
    //------------------------

    private int turnoActual;
    private boolean batallaTerminada;

    // Constructor
    public Batalla(Heroe[] heroes, Enemigo[] enemigos){
        this.heroes = heroes;
        this.enemigos = enemigos;
        this.turnoActual = 0;
        this.batallaTerminada = false;
    }

    // metodo para guardar la partida mediante un bufferedWriter
    public void guardarpartida(String archivo) throws IOException{
    // el bufferedWriter escribira linea por linea todo lo que se lleva de batalla entonces guarda desde el punto donde se decidio guardar los datos de los personajes
    // y el turno en el que estan. y se le pasa al bufferedWriter un archivo objeto filewriter ya que este es el intermediario

    BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

        //Guardar turno
        writer.write("TURNO," + turnoActual);
        writer.newLine();

        //Guardar heroes
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

        //Guardar enemigos
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

    // metodo para cargar la partida nuevamente con un bufferedReader al cual se le pone su intermediario FileReader
    public void cargarpartida(String archivo) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;

        // estas son variables para saber en que posicion del array guardar, cada que lee a un heroe lo va guardandi 
        int iHeroe = 0;
        int iEnemigo = 0;

        // While que permite leer linea por linea 
        while ((linea = br.readLine()) != null) {

            // este simplemente divide cada linea cada que haya una coma.


            String[] datos = linea.split(",");

            // detecta que tipo de linea es ( turno, heores o enemigos )
            switch (datos[0]) {

                case "TURNO":
                    // parseint vuelve una cadena de texto en entero 
                    this.turnoActual = Integer.parseInt(datos[1]);
                    break;

                case "HEROE":
                    // recorre cada heroe cargandolo segun como haya quedado guardado y valueof covierte un texto en valor del enum
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

    // Getters
    public Heroe[] getEquipoHeroes() { return heroes; }
    public Enemigo[] getEquipoEnemigos() { return enemigos; }
    public boolean isBatallaTerminada() { return batallaTerminada; }
    public int getTurnoActual() { return turnoActual; }

    // Setters
    public void setBatallaTerminada(boolean batallaTerminada){
         this.batallaTerminada = batallaTerminada; }
         
    public void setTurnoActual(int turnoActual) {
         this.turnoActual = turnoActual; }

         
         /**
          * Método auxiliar para iniciar una batalla desde un controlador.
          * Implementación mínima: resetea el estado de la batalla.
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
            
            public void siguienteTurno() {
                if (!batallaTerminada) {
                    turnoActual++;
                }
            }

}
