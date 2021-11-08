package Csv;

import Objetos.POJODatos;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class MeteoReader implements Runnable {
    private String meteoCsv = System.getProperty("user.dir")+ File.separator+"Datos"+File.separator+"calidad_aire_datos_meteo_mes.csv";
    List<POJODatos> listaMeteo = new ArrayList<>();

    private static MeteoReader meteoReader = null;
    private MeteoReader(){}

    public static MeteoReader getInstance(){
        if(meteoReader ==null){
            meteoReader = new MeteoReader();
        }
        return meteoReader;
    }

    /**
     * creamos la lista de POJODatos de calidad
     */
    private void objectGenerator(){

        List<POJODatos> datosMeteo = getStringList().stream().skip(1).map(this::getDatos).collect(Collectors.toList());
        //System.out.println("datos calidad: "+datosCalidad.size());
        listaMeteo = datosMeteo;
    }

    /**
     * obtenemos la lista de Strings del archivo csv de los datos de calidad aire
     * @return la lista de String
     */
    private List<String> getStringList() {
        try {
            return Files.readAllLines(Path.of(meteoCsv));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * a traves de una String con datos creamos el objeto POJODatos
     * @param linea de donde se leera la informacion
     * @return el objeto POJODatos
     */
    private POJODatos getDatos(String linea){
        POJODatos meteo = new POJODatos();
        Scanner sc = new Scanner(linea);
        sc.useDelimiter(";");
        sc.next();
        sc.next();
        meteo.setEstacion(sc.next());
        sc.next();
        String largo = sc.next();
        meteo.setFecha(sc.next()+"/"+sc.next()+"/"+sc.next());
        meteo.setMunicipio(largo.substring(2,5));
        StringTokenizer st = new StringTokenizer(largo,"_");
        st.nextToken();
        meteo.setMagnitud(st.nextToken());

        meteo.setTemperaturas(getTemperaturas(linea));

        if(meteo.getTemperaturas().size()!=0){
            Optional<Double> max = meteo.getTemperaturas().stream().max(Comparator.comparing(t->t));
            meteo.setMax(max.get());
            Optional<Double> min = meteo.getTemperaturas().stream().min(Comparator.comparing(t->t));
            meteo.setMin(min.get());
            Double media = getMedia(meteo.getTemperaturas());
            meteo.setMedia(media);
        }

        return meteo;
    }

    /**
     * crea la media diaria a partir de una lista de valores double
     * @param temperaturas de todo el dia
     * @return la media
     */
    private Double getMedia(List<Double> temperaturas) {
        double suma= temperaturas.stream().mapToDouble(v ->v).sum();
        //System.out.println("suma: "+suma);
        return suma/ temperaturas.size();
    }

    /**
     * creamos la lista de valores por hora a partir de una String equivalente a una linea del csv
     * @param linea de donde se leeran los datos
     * @return la lista de valores por hora
     */
    private List<Double> getTemperaturas(String linea) {
        List<String> temperaturas = new ArrayList<>();
        List<Double> returner = new ArrayList<>();
        //System.out.println(linea);
        Scanner sc = new Scanner(linea);
        sc.useDelimiter(";");

        sc.next();
        sc.next();
        sc.next();
        sc.next();
        sc.next();
        sc.next();
        sc.next();
        sc.next();

        String temp;
        for (int i=0;i<24;i++){
            temp = sc.next();
            if(sc.next().equalsIgnoreCase("V")){
                temperaturas.add(temp.replace(",","."));
            }
        }

        temperaturas.forEach(v->{
            returner.add(Double.parseDouble(v));
        });

        return returner;
    }

    @Override
    public void run() {
        objectGenerator();
    }
}
