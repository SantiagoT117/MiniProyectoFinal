package vista;

import java.util.ArrayList;
import java.util.Scanner;

import controlador.ControladorBatalla;
import modelo.*;

/**
 * Implementación de la vista para la interfaz de terminal/consola.
 * Muestra toda la información mediante System.out.println
 * y recibe la entrada del usuario mediante Scanner.
 * 
 * Esta vista es simple y basada en texto, ideal para:
 * - Depuración rápida
 * - Entornos sin interfaz gráfica
 * - Usuarios que prefieren la línea de comandos
 */
public class VistaTerminal implements VistaJuego {
    
    private Scanner sc = new Scanner(System.in);

    // ==================== MÉTODOS DE VISUALIZACIÓN ====================
    /**
     * Muestra un mensaje genérico en la consola.
     * 
     * @param mensaje Texto a mostrar
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Muestra el estado actual de un héroe y un enemigo en formato texto.
     * Incluye HP, MP y ataque de ambos personajes.
     * 
     * @param heroe Héroe cuyo estado se mostrará
     * @param enemigo Enemigo cuyo estado se mostrará
     */
    @Override
    public void mostrarEstado(Heroe heroe, Enemigo enemigo) {
        System.out.println("-------------- HEROES -----------\n");
        System.out.println(heroe.getNombre() + " //HP " + heroe.getHp() + " //MP " + heroe.getMp() + " //Ataque " + heroe.getAtaque());

        System.out.println("-------- ENEMGIOS ---------\n");
        System.out.println(enemigo.getNombre() + " //HP " + enemigo.getHp() + " //MP " + enemigo.getMp() + " //Ataque " + enemigo.getAtaque());
    }

    /**
     * Muestra el mensaje de victoria en la consola.
     */
    @Override
    public void mostrarVictoria() {
        System.out.println("Has derrotado a todos los enemigos, FELICIDADES HAS GANADO");
    }

    /**
     * Muestra el mensaje de derrota en la consola.
     */
    @Override
    public void mostrarDerrota() {
        System.out.println("Han derrotado a todos los heroes, NOS CONDENASTE A TODOS");
    }

    /**
     * Pausa la ejecución hasta que el usuario presione ENTER.
     */
    @Override
    public void esperarEnter() {
        System.out.println("Presiona ENTER para continuar");
        sc.nextLine();
    }

    // ==================== MÉTODOS DE ENTRADA DEL USUARIO ====================

    /**
     * Presenta las opciones de acción disponibles para un héroe.
     * Incluye las opciones de guardar y cargar partida.
     * 
     * @param heroe Héroe que realizará la acción
     * @return Número de la opción elegida
     */
    @Override
    public int elegirAccion(Heroe heroe) {
        System.out.println("Elige una accion para " + heroe.getNombre() +"\n");
        System.out.println("1. Atacar\n");
        System.out.println("2. Habilidad");
        System.out.println("3.guardar partida");
        System.out.println("4.cargar partida");
        return leerEntero();
    }

    /**
     * Permite al usuario seleccionar un enemigo de los que están vivos.
     * Muestra solo los enemigos con HP > 0 y sus puntos de vida.
     * Filtra automáticamente los enemigos muertos.
     * 
     * @param enemigos Array de todos los enemigos
     * @return Índice del enemigo seleccionado en el array original
     */
    @Override
    public int seleccionarEnemigo(Enemigo[] enemigos) {

        System.out.println("\nElige un enemigo:");

        ArrayList<Integer> indicesVivos = new ArrayList<>();

        // Mostrar solo enemigos vivos con su número de lista
        for (int i = 0; i < enemigos.length; i++) {
            if (enemigos[i].getHp() > 0) {
                indicesVivos.add(i);
                System.out.println(indicesVivos.size() + ". " + enemigos[i].getNombre() +
                    " (HP: " + enemigos[i].getHp() + ")");
            }
        }

        int opcion = leerEntero();

        // Validar que la opción esté en rango
        if (opcion < 1 || opcion > indicesVivos.size()) {
            System.out.println("Opción inválida.");
            return seleccionarEnemigo(enemigos);
        }

        // Retornar el índice real en el array original
        return indicesVivos.get(opcion - 1);
    }

    /**
     * Permite al usuario seleccionar un héroe vivo.
     * 
     * @param heroes Array de héroes
     * @return Número del héroe seleccionado
     */
    @Override
    public int seleccionarHeroe(Heroe[] heroes) {

        System.out.println("\nElige un héroe:");
        for (int i = 0; i < heroes.length; i++) {
            if (heroes[i].getHp() > 0) {
                System.out.println((i + 1) + ". " + heroes[i].getNombre() + " (HP: " + heroes[i].getHp() + ")");
            }
        }
        return leerEntero();
    }

    /**
     * Permite al usuario seleccionar un héroe muerto (HP <= 0).
     * Útil para habilidades de resurrección.
     * 
     * @param heroes Array de héroes
     * @return Número del héroe muerto seleccionado
     */
    @Override
    public int seleccionarHeroeMuerto(Heroe[] heroes) {

        System.out.println("\nElige un héroe muerto:");
        for (int i = 0; i < heroes.length; i++) {
            if (heroes[i].getHp() <= 0) {
                System.out.println((i + 1) + ". " + heroes[i].getNombre() + " (HP: 0)");
            }
        }
        return leerEntero();
    }

    /**
     * Lee un número entero de la entrada del usuario con validación.
     * Utiliza try-catch para manejar entradas inválidas.
     * 
     * Validaciones:
     * - No permite entradas vacías
     * - Verifica que sea un número válido
     * - Repite la solicitud si hay error
     * 
     * @return Número entero válido ingresado por el usuario
     */
    @Override
    public int leerEntero() {
        while (true) {
            try {
                System.out.print("Ingresa una opcion: ");
                String entrada = sc.nextLine().trim();
                
                // Validar que no esté vacío
                if(entrada.isEmpty()){
                    System.out.println("No puedes dejar la entrada vacía.");
                    continue;
                }
                
                return Integer.parseInt(entrada);
                
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido.");
            }
        }
    }

    /**
     * Muestra la lista de todos los héroes con su nombre y tipo.
     * 
     * @param heroe Array de héroes a mostrar
     */
    @Override
    public void mostrarHeroes(Heroe[] heroe) {
        System.out.println(" --------- Heroes -------");
        for ( Heroe h : heroe){
            System.out.println(h.getNombre() + " - " + h.getTipo() + "\n");
        }   
    }

    /**
     * Muestra la lista de todos los enemigos con su nombre y tipo.
     * 
     * @param enemigo Array de enemigos a mostrar
     */
    @Override
    public void mostrarEnemigos(Enemigo[] enemigo) {
        System.out.println(" --------- Enemigos -------");
        for ( Enemigo e : enemigo){
            System.out.println(e.getNombre() + " - " + e.getTipo() + "\n");
        }  
    }

    /**
     * Método de inicialización (no requerido en vista de terminal).
     * 
     * @param controlador Controlador de batalla
     */
    @Override
    public void iniciar(ControladorBatalla controlador) {
        // No requiere inicialización especial en terminal
    }

    /**
     * Método para actualizar barras (no aplicable en terminal).
     * Las barras son un concepto visual de GUI.
     */
    @Override
    public void actualizarBarras() {
        // No aplicable en vista de terminal
    }
    
    }
    


    


