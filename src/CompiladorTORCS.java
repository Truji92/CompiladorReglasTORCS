import ast.structs.Controller;
import parser.TORCSParser;

import java.io.*;

/**
 * Created by Truji on 30/06/2015.
 */
public class CompiladorTORCS {

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Debes especificar el archivo que deseas compilar");
            return;
        }

        String file = args[0];

        TORCSParser parser = new TORCSParser();

        if (parser.parse(file)) {
            System.out.println("Código correcto, generando Java");
            Controller ASA = parser.getASA();

            File outFile = new File(ASA.genJavaFilename(file));

            try {
                FileWriter writer = new FileWriter(outFile);

                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                PrintWriter wr = new PrintWriter(bufferedWriter);

                wr.write(ASA.genJavaCode(file));

                wr.close();
                bufferedWriter.close();
                System.out.println("Proceso de compilación terminado sin errores.");

            } catch (IOException ex) {
                System.out.println("Error al escribir el archivo generado en disco");
                ex.toString();
            }


        } else {
            System.out.println("Compilación abortada");
        }
    }
}
