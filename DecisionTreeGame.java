import java.util.Scanner;
import java.io.*;

/**
 * Implementa la lógica principal para el Juego del Árbol de Decisión Sí/No.
 */
public class DecisionTreeGame {
    private TreeNode root;
    private Scanner scanner;
    private static final String TREE_FILE = "decision_tree.txt";

    /**
     * Construye un nuevo DecisionTreeGame, cargando el árbol desde un archivo si existe,
     * o creando un árbol inicial si no.
     */
    public DecisionTreeGame() {
        scanner = new Scanner(System.in);
        
        if (loadTree()) {
            System.out.println("Árbol de decisión cargado desde el archivo.");
        } else {
            createInitialTree();
            System.out.println("Nuevo árbol de decisión creado.");
        }
    }

    /**
     * Crea el árbol de decisión inicial con varios animales.
     */
    private void createInitialTree() {
        root = new TreeNode("¿Es un lenguaje compilado?");
        
        // Rama de lenguajes compilados
        TreeNode compiled = new TreeNode("¿Es orientado a objetos?");
        root.yesNode = compiled;
        
        TreeNode objectOriented = new TreeNode("¿Fue creado por Microsoft?");
        compiled.yesNode = objectOriented;
        objectOriented.yesNode = new TreeNode("C#");
        
        TreeNode nonMicrosoft = new TreeNode("¿Fue creado por Oracle/Sun?");
        objectOriented.noNode = nonMicrosoft;
        nonMicrosoft.yesNode = new TreeNode("Java");
        nonMicrosoft.noNode = new TreeNode("C++");
        
        TreeNode nonOO = new TreeNode("¿Es usado principalmente para sistemas operativos?");
        compiled.noNode = nonOO;
        nonOO.yesNode = new TreeNode("C");
        nonOO.noNode = new TreeNode("Rust");
        
        // Rama de lenguajes interpretados
        TreeNode interpreted = new TreeNode("¿Es principalmente para web?");
        root.noNode = interpreted;
        
        TreeNode webLang = new TreeNode("¿Es del lado del cliente?");
        interpreted.yesNode = webLang;
        webLang.yesNode = new TreeNode("JavaScript");
        webLang.noNode = new TreeNode("Python");
        
        TreeNode nonWeb = new TreeNode("¿Es usado para análisis de datos?");
        interpreted.noNode = nonWeb;
        nonWeb.yesNode = new TreeNode("R");
        
        TreeNode nonData = new TreeNode("¿Es un lenguaje funcional?");
        nonWeb.noNode = nonData;
        nonData.yesNode = new TreeNode("Haskell");
        nonData.noNode = new TreeNode("Go");

        saveTree();
    }

    /**
     * Guarda el árbol de decisión en un archivo.
     */
    private void saveTree() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TREE_FILE))) {
            root.serialize(writer);
            System.out.println("Árbol de decisión guardado en el archivo.");
        } catch (IOException e) {
            System.out.println("Error al guardar el árbol de decisión: " + e.getMessage());
        }
    }

    /**
     * Carga el árbol de decisión desde un archivo.
     * @return true si el árbol se cargó correctamente, false en caso contrario.
     */
    private boolean loadTree() {
        File file = new File(TREE_FILE);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(TREE_FILE))) {
            root = TreeNode.deserialize(reader);
            if (root == null) {
                System.out.println("Error: El árbol está vacío, creando uno nuevo...");
                createInitialTree();
                return false;
            }
            return true;
        } catch (IOException | NullPointerException e) {
            System.out.println("Error al cargar el árbol de decisión: " + e.getMessage());
            System.out.println("Creando un nuevo árbol...");
            createInitialTree();
            return false;
        }
    }

    /**
     * Inicia una nueva sesión de juego.
     */
    public void playGame() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  ¡Bienvenido al Adivinador de Lenguajes!   ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        printTree(); // Mostrar el árbol al inicio
        
        System.out.println("┌────────────────────────────────────────────┐");
        System.out.println("│ Piensa en un lenguaje y trataré de        │");
        System.out.println("│ adivinarlo haciendo algunas preguntas.    │");
        System.out.println("└────────────────────────────────────────────┘\n");
        
        playGame(root);
    }

    /**
     * Método recursivo para recorrer el árbol de decisión y jugar el juego.
     * @param node El nodo actual en el árbol de decisión.
     */
    private void playGame(TreeNode node) {
        if (node.yesNode == null && node.noNode == null) {
            System.out.println("╭────────────────────────╮");
            System.out.println("│ ¿Es " + node.data + "? (s/n)");
            System.out.println("╰────────────────────────╯");
            String response = getValidResponse();
            if (response.equalsIgnoreCase("s")) {
                System.out.println("\n┌────────────────────────┐");
                System.out.println("│     ¡He ganado! 🎉     │");
                System.out.println("└────────────────────────┘\n");
            } else {
                learnNewAnimal(node);
            }
        } else {
            System.out.println("┌─────────────────────────────────┐");
            System.out.println("│ " + node.data + " (s/n)");
            System.out.println("└─────────────────────────────────┘");
            String response = getValidResponse();
            if (response.equalsIgnoreCase("s")) {
                playGame(node.yesNode);
            } else {
                playGame(node.noNode);
            }
        }
    }

    /**
     * Maneja el proceso de aprendizaje cuando la IA falla en adivinar correctamente.
     * @param node El nodo hoja actual en el árbol de decisión.
     */
    private void learnNewAnimal(TreeNode node) {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║  ¡Me rindo! Ayúdame a aprender...  ║");
        System.out.println("╚════════════════════════════════════╝");
        
        System.out.println("\n¿En qué lenguaje estabas pensando?");
        String correctLanguage = scanner.nextLine();
        
        System.out.println("\n¿Qué pregunta distinguiría a " + correctLanguage + " de " + node.data + "?");
        String newQuestion = scanner.nextLine();
        
        System.out.println("\nPara " + correctLanguage + ", ¿cuál es la respuesta a esta pregunta? (s/n)");
        String answer = getValidResponse();

        // Actualizar el árbol
        TreeNode correctNode = new TreeNode(correctLanguage);
        TreeNode guessNode = new TreeNode(node.data);
        node.data = newQuestion;
        if (answer.equalsIgnoreCase("s")) {
            node.yesNode = correctNode;
            node.noNode = guessNode;
        } else {
            node.yesNode = guessNode;
            node.noNode = correctNode;
        }
        
        System.out.println("\n┌────────────────────────────────┐");
        System.out.println("│ ¡Gracias! He aprendido algo    │");
        System.out.println("│ nuevo sobre programación! 🎓   │");
        System.out.println("└────────────────────────────────┘\n");
        
        saveTree(); // Guardar el árbol actualizado
    }

    /**
     * Obtiene una respuesta válida sí/no del usuario.
     * @return Una respuesta válida (s o n).
     */
    private String getValidResponse() {
        String response;
        do {
            response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("s") && !response.equals("n")) {
                System.out.println("Por favor, ingrese 's' para sí o 'n' para no.");
            }
        } while (!response.equals("s") && !response.equals("n"));
        return response;
    }

    /**
     * Imprime el árbol de decisión en la consola.
     */
    private void printTree() {
        System.out.println("\n=== Árbol de Decisión Actual ===");
        printNode(root, "", true);
        System.out.println("=============================\n");
    }

    /**
     * Método recursivo para imprimir el árbol de decisión.
     * @param node El nodo actual en el árbol de decisión.
     * @param prefix El prefijo para el nodo actual.
     * @param isTail Indica si el nodo actual es el último hijo de su padre.
     */
    private void printNode(TreeNode node, String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.data);
        
        if (node.yesNode != null) {
            printNode(node.yesNode, prefix + (isTail ? "    " : "│   "), node.noNode == null);
        }
        if (node.noNode != null) {
            printNode(node.noNode, prefix + (isTail ? "    " : "│   "), true);
        }
    }
}