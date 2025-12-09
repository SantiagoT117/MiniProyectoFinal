import java.util.Scanner;

import controlador.ControladorBatalla;
import modelo.Batalla;
import modelo.Enemigo;
import modelo.Heroe;
import modelo.Tipo_Enemigo;
import modelo.Tipo_Heroe;
import vista.VistaGUI;
import vista.VistaJuego;
import vista.VistaTerminal;

/**
 * Clase principal del juego Dragon Quest VIII.
 * Implementa el patrón MVC (Modelo-Vista-Controlador) para gestionar el sistema de batalla.
 * 
 * Esta clase se encarga de:
 * - Solicitar al usuario el modo de juego (Terminal o GUI)
 * - Inicializar los héroes y enemigos de la batalla
 * - Crear e iniciar el controlador de batalla con la vista seleccionada
 * 
 * @author Santiago Torres Rojas - 2380301
 * @author Samuel Garcia Parra - 2459476
 * @author Juan Sebastian Navarrete Rada - 2459562
 * @author Juan David Correa Zapata - 2459431
 */
public class App {
    /**
     * Método principal que inicia la aplicación.
     * Solicita al usuario elegir entre interfaz de terminal o GUI,
     * crea los equipos de héroes y enemigos, y comienza la batalla.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     * @throws Exception Si ocurre algún error durante la ejecución
     */
    public static void main(String[] args) throws Exception {

        VistaJuego vista = null;
        
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        
        // Bucle para validar que el usuario elija una opción válida (1 o 2)
        while (opcion != 1 && opcion != 2) {
            try {
                // Preguntamos al usuario si desea jugar por GUI o Terminal 
                System.out.println("Bienvenido a Dragon Quest VIII");
                System.out.println("1. Terminal");
                System.out.println("2. GUI");
                System.out.println("En que modo desea jugar?");
                
                String entrada = sc.nextLine();
                
                // Convertimos la entrada a número
                opcion = Integer.parseInt(entrada);
                
                // Validamos que sea 1 o 2
                if(opcion != 1 && opcion !=2){
                    System.out.println("Opción inválida. Debes escoger 1 o 2.\n");
                }
                
            }catch(Exception e){
                // Capturamos errores de formato (NumberFormatException)
                System.out.println("Ingresa un número válido.");
                
            }
            
        }
        
        // Instanciamos la vista según la opción seleccionada
        if (opcion == 1) {
            vista = new VistaTerminal();
        } else if (opcion == 2) {
            vista = new VistaGUI();
        }
        
        
        // Creación del equipo de héroes con diferentes tipos y atributos
        // Parámetros: nombre, tipo, hp, mp, ataque, defensa, velocidad
        Heroe[] heroes = {
            new Heroe("Angelo", Tipo_Heroe.GUERRERO, 50, 25, 18, 30, 55),
            new Heroe("Yangus", Tipo_Heroe.GUERRERO, 40, 5, 20, 35, 25),
            new Heroe("Hero", Tipo_Heroe.GUERRERO, 40, 5, 20, 35, 25),
            new Heroe("Jessica", Tipo_Heroe.GUERRERO, 40, 5, 20, 35, 25),
        };
        
        // Creación del equipo de enemigos con diferentes tipos
        // Parámetros: nombre, hp, mp, ataque, defensa, velocidad, tipo
        Enemigo[] enemigos = {
            new Enemigo("Golem", 30, 0, 23, 0, 30, Tipo_Enemigo.GOLEM),
            new Enemigo("Esqueleto", 25, 0, 12, 0, 21, Tipo_Enemigo.NOMUERTO),
            new Enemigo("Esqueleto", 25, 0, 12, 0, 21, Tipo_Enemigo.NOMUERTO),
            new Enemigo("Gengar", 25, 0, 12, 0, 21, Tipo_Enemigo.NOMUERTO),
        };
        
        
        // Creación del objeto batalla que contiene los equipos
        Batalla batalla = new Batalla(heroes, enemigos);
        
        // Creación del controlador que gestiona la lógica de la batalla
        ControladorBatalla controlador = new ControladorBatalla(batalla, heroes, enemigos, vista);
        
        // Inicialización y comienzo de la batalla
        controlador.iniciar();
        controlador.iniciarBatalla();
    }
}