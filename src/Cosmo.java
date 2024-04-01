public class Cosmo {
    public static void main(String[] args) {
        if (args.length > 0) {
            String input_filename = args[0];
            System.out.println("[cosmo] Arquivo de entrada fornecido: " + input_filename);
        } else {
            System.out.println("[cosmo] Nenhum arquivo de entrada foi fornecido");
        }
    }
}
