package biblio;
public class CExecutor {

    public void executerC(String csvFile) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
            "../c/traitement2.exe", 
            csvFile
        );

        pb.inheritIO();   // Pour afficher la sortie du C dans la console Java
        Process p = pb.start();//lancer c 
        p.waitFor();
    }
}
