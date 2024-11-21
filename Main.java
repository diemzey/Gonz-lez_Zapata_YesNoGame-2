import java.util.Scanner;

/**
 * Clase principal para ejecutar el Juego del Árbol de Decisión Sí/No.
 */
public class Main {
    public static void main(String[] args) {
        DecisionTreeGame game = new DecisionTreeGame();
        Scanner scanner = new Scanner(System.in);
        String playAgain;

        System.out.println("¡Bienvenido al Juego de Adivinanza de Lenguajes de Programación Sí/No!");
        System.out.println("Intentaré adivinar el lenguaje de programación en el que estás pensando haciendo preguntas de sí/no.");
        System.out.println("Si adivino mal, ¡puedes enseñarme sobre nuevos lenguajes de programación!");

        do {
            game.playGame();
            System.out.println("¿Quieres jugar de nuevo? (s/n)");
            playAgain = getValidResponse(scanner);
        } while (playAgain.equalsIgnoreCase("s"));

        scanner.close();
        System.out.println("¡Gracias por jugar! ¡Espero que te hayas divertido y me hayas enseñado algo nuevo!");
    }

    /**
     * Obtiene una respuesta válida sí/no del usuario.
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @return Una respuesta válida (s o n).
     */
    private static String getValidResponse(Scanner scanner) {
        String response;
        do {
            response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("s") && !response.equals("n")) {
                System.out.println("Por favor, ingrese 's' para sí o 'n' para no.");
            }
        } while (!response.equals("s") && !response.equals("n"));
        return response;
    }
}