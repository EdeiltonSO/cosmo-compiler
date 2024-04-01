import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Cosmo {
    public static void main(String[] args) {
        if (args.length > 0) {
            String input_filename = args[0];
            // Só para debug
            System.out.println("cosmo: arquivo de entrada fornecido: " + input_filename);

            String file_content = read_file(input_filename);
            // Só para debug
            System.out.println("cosmo: conteudo do arquivo de entrada: " + file_content);
        } else {
            System.out.println("cosmo: nenhum arquivo de entrada foi fornecido");
        }
    }

    public static String read_file(String filename) {
        StringBuilder file_content = new StringBuilder();

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                file_content.append(scanner.nextLine());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("cosmo: arquivo ou diretorio nao encontrado");
            System.exit(1);
        }

        return file_content.toString();
    }
}
