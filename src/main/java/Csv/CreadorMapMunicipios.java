package Csv;

import Mapas.EstacionesMapas;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringTokenizer;

public class CreadorMapMunicipios implements Runnable {

    String uri = System.getProperty("user.dir")+File.separator+"Datos"+File.separator+"calidad_aire_estaciones.csv";

    private void mapMunicipios() throws IOException {
        EstacionesMapas em = EstacionesMapas.getInstance();

        List<String> estacionesList = Files.readAllLines(Path.of(uri), Charset.forName("windows-1252"));

        for(String a : estacionesList){

            StringTokenizer st = new StringTokenizer(a,";");

            String codigo = st.nextToken();
            int codigoMunicipio = Integer.parseInt(codigo.substring(2,5));
            st.nextElement();
            String nombre = st.nextToken();
            em.fillCodigoMunicipio(codigoMunicipio,nombre);
        }
    }

    @Override
    public void run() {
        try {
            mapMunicipios();
        } catch (IOException e) {
            System.err.println("no se pudieron leer los municipios disponibles");
        }
    }
}