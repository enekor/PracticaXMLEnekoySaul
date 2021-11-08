package markDown;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import xpath.*;

public class MDCreator {

    /**
     * crea un archivo md a partir de la lista de resultados de xpath
     * @param uri donde se guardara el archivo .md
     * @param municipio al que hace referencia la medicion
     */
    public void mdCreator(String uri, int municipio, String fecha){
        File archivo = new File(uri);
        if(!archivo.exists()){
            archivo.mkdirs();
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(uri+File.separator+"datos.md"));) {
            XpathManager manager = new XpathManager();
            bw.write("## Datos de "+municipio+" del dia "+fecha+"\n");
            manager.obtenerMediasMensuales().forEach(v->{
                try {
                    bw.write("* "+v+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
