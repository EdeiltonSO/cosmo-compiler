import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import cosmo.Token;
import cosmo.Lexer;
import cosmo.Parser;
import cosmo.AST.*;

public class Cosmo {
    public static void main(String[] args) {
        if (args.length > 0) {
            String input_filename = args[0];
            String file_content = read_file(input_filename);

            // Análise léxica
            System.out.println("=> Analise lexica:");
            Lexer lexer = new Lexer(file_content);
            List<Token> tokens = lexer.scan();

            // add flag pra definir se roda esse for ou não
            for (Token token : tokens) {
                System.out.println(token.line + ":" + token.column + " " + Token.spellings[token.kind] + " " + token.spelling);
            }

            // Análise sintática
            System.out.println("\n=> Analise sintatica:");
            Parser parser = new Parser(tokens);
            NodeProgram ASTRoot = parser.parse();

            if (parser.error_count == 0) {
                System.out.println("Analise sintatica finalizada com exito.");
            } else {
                System.exit(parser.error_count);
            }

            Printer printer = new Printer();
            printer.print(ASTRoot);

        } else {
            System.out.println("cosmo: nenhum arquivo de entrada foi fornecido");
        }

    }

    public static String read_file(String filename) {
        String file_content = "";

        try {
            file_content = new String(Files.readString(Path.of(filename)));
        } catch (IOException e) {
            System.out.println("cosmo: arquivo ou diretorio nao encontrado");
            System.exit(1);
        }

        return file_content;
    }
}