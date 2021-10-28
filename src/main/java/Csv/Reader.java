package Csv;

import Mapas.EstacionesMapas;
import lombok.Data;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Reader {

    private String calidadCsv = System.getProperty("user.dir")+File.separator+"Datos"+File.separator+"calidad_aire_datos_mes.csv";
    //String meteoCsv = System.getProperty("user.dir")+File.separator+"Datos"+File.separator+"calidad_aire_datos_meteo_mes.csv";

    private static Reader reader = null;
    private Reader(){}

    public static Reader getInstance(){
        if(reader==null){
            reader = new Reader();
        }
        return reader;
    }

    /**
     * sacamos todos los elementos de la primera linea del xml, que son los nombres de cada posicion
     * @return la lista de nombres de futuros elementos
     */
    private List<String> getNombres() {
        List<String> returner =  null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(calidadCsv)));
            returner = Stream.of(br.readLine().split(";")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returner;
    }

    /**
     * cracion de los mapas de estaciones
     * @throws IOException
     */
    private void createmap() throws IOException {
        EstacionesMapas em = EstacionesMapas.getInstance();
        String path = System.getProperty("user.dir")+File.separator+"Datos"+File.separator+"calidad_aire_estaciones.csv";

        List<String> estacionesList = Files.readAllLines(Path.of(path));

        for(String a : estacionesList){

            StringTokenizer st = new StringTokenizer(a,";");

            String codigo = st.nextToken();
            int codigoMunicipio = Integer.parseInt(codigo.substring(2,5));
            st.nextElement();
            String nombre = st.nextToken();
            em.fillCodigoMunicipio(codigoMunicipio,nombre);
            em.fillCodigoNacional(Integer.parseInt(codigo),nombre);
        }
    }

    public void start() throws IOException {
        getNombres();
        createmap();
    }

    /*public static void main(String[] args) {
        Reader r = new Reader();
        System.out.println(r.getAttributes());
    }*/
}
