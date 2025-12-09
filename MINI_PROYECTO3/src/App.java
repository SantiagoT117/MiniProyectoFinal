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

public class App {
    public static void main(String[] args) throws Exception {

        VistaJuego vista = null;
        
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        
        while (opcion != 1 && opcion != 2) {
            try {
                // preguntamos al usuario si desea verlo por GUI o terminal 
                System.out.println("Bienvenido a Dragon Quest VIII");
                System.out.println("1. Terminal");
                System.out.println("2. GUI");
                System.out.println("En que modo desea jugar?");
                
                String entrada = sc.nextLine();
                
                opcion = Integer.parseInt(entrada);
                
                if(opcion != 1 && opcion !=2){
                    System.out.println("Opción inválida. Debes escoger 1 o 2.\n");
                }
                
            }catch(Exception e){
                System.out.println("Ingresa un número válido.");
                
            }
            
        }
        
        if (opcion == 1) {
            vista = new VistaTerminal();
        } else if (opcion == 2) {
            vista = new VistaGUI();
        }
        
        
        // creacion de heroes y enemigos
        Heroe[] heroes = {
            new Heroe("Angelo", Tipo_Heroe.GUERRERO, 50, 25, 18, 30, 55),
            new Heroe("Yangus", Tipo_Heroe.GUERRERO, 40, 5, 20, 35, 25),
            new Heroe("Hero", Tipo_Heroe.GUERRERO, 40, 5, 20, 35, 25),
            new Heroe("Jessica", Tipo_Heroe.GUERRERO, 40, 5, 20, 35, 25),
        };
        
        Enemigo[] enemigos = {
            new Enemigo("Golem", 30, 0, 23, 0, 30, Tipo_Enemigo.GOLEM),
            new Enemigo("Esqueleto", 25, 0, 12, 0, 21, Tipo_Enemigo.NOMUERTO),
            new Enemigo("Esqueleto", 25, 0, 12, 0, 21, Tipo_Enemigo.NOMUERTO),
            new Enemigo("Gengar", 25, 0, 12, 0, 21, Tipo_Enemigo.NOMUERTO),
        };
        
        
        // objeto controlador que permitira llamar el iniciarBatalla para inciar nuestro juego
        Batalla batalla = new Batalla(heroes, enemigos);
        ControladorBatalla controlador = new ControladorBatalla(batalla, heroes, enemigos, vista);
        
<<<<<<< Updated upstream
=======
        // Inicialización y comienzo de la batalla
>>>>>>> Stashed changes
        controlador.iniciar();
        controlador.iniciarBatalla();
    }
}