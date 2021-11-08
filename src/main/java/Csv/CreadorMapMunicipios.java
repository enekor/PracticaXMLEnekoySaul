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

    private static CreadorMapMunicipios cmm = null;
    private CreadorMapMunicipios(){}
    public static CreadorMapMunicipios getInstance(){
        if(cmm==null){
            cmm= new CreadorMapMunicipios();
        }
        return cmm;
    }

    /**
     * creamos el mapa de municipios y estaciones con sus codigos correspondientes
     * @throws IOException
     */
    private void mapMunicipios() throws IOException {
        EstacionesMapas em = EstacionesMapas.getInstance();

        List<String> estacionesList = Files.readAllLines(Path.of(uri), Charset.forName("windows-1252"));

        estacionesList.stream().skip(1).forEach(a->{

            StringTokenizer st = new StringTokenizer(a,";");

            String codigo = st.nextToken();
            int codigoMunicipio = Integer.parseInt(codigo.substring(2,5));
            st.nextElement();
            String nombre = st.nextToken();
            nombre = nombre.replace(" ","_");
            em.fillCodigoMunicipio(codigoMunicipio,nombre);
            em.fillCodigoestaciones(Integer.parseInt(codigo),nombre);

        });
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
