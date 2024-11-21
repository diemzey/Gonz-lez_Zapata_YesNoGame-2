import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class TreeNode {
    String data;
    TreeNode yesNode;
    TreeNode noNode;

    TreeNode(String data) {
        this.data = data;
        yesNode = null;
        noNode = null;
    }

    // Serialize the node and its children
    public void serialize(BufferedWriter writer) throws IOException {
        writer.write(data);
        writer.newLine();
        
        if (yesNode != null) {
            writer.write("Y");
            writer.newLine();
            yesNode.serialize(writer);
        }
        
        if (noNode != null) {
            writer.write("N");
            writer.newLine();
            noNode.serialize(writer);
        }
        
        writer.write("E");
        writer.newLine();
    }

    // Deserialize a node and its children
    public static TreeNode deserialize(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        
        // Si no hay más líneas, retornamos null
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        // Si encontramos una marca de fin, retornamos null
        if (line.trim().equals("E")) {
            return null;
        }

        TreeNode node = new TreeNode(line.trim());
        
        // Leer la siguiente línea
        String nextLine = reader.readLine();
        while (nextLine != null && !nextLine.equals("E")) {
            if (nextLine.equals("Y")) {
                node.yesNode = deserialize(reader);
            } else if (nextLine.equals("N")) {
                node.noNode = deserialize(reader);
            }
            nextLine = reader.readLine();
        }
        
        return node;
    }
}