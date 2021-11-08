package Csv;

import Objetos.POJODatos;
import lombok.Data;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class CalidadReader implements Runnable {

    private String calidadCsv = System.getProperty("user.dir")+File.separator+"Datos"+File.separator+"calidad_aire_datos_mes.csv";
    List<POJODatos> listaCalidad = new ArrayList<>();

    private static CalidadReader calidadReader = null;
    private CalidadReader(){}

    public static CalidadReader getInstance(){
        if(calidadReader ==null){
            calidadReader = new CalidadReader();
        }
        return calidadReader;
    }

    /**
     * creamos la lista de POJODatos de calidad
     */
    private void objectGenerator(){

        List<POJODatos> datosCalidad = getStringList().stream().skip(1).map(this::getDatos).collect(Collectors.toList());
        //System.out.println("datos calidad: "+datosCalidad.size());
        listaCalidad = datosCalidad;
    }

    /**
     * obtenemos la lista de Strings del archivo csv de los datos de calidad aire
     * @return la lista de String
     */
    private List<String> getStringList() {
        try {
            return Files.readAllLines(Path.of(calidadCsv));
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
        POJODatos calidad = new POJODatos();
        Scanner sc = new Scanner(linea);
        sc.useDelimiter(";");
        sc.next();
        sc.next();
        sc.next();
        sc.next();
        String largo = sc.next();
        calidad.setEstacion(largo);
        calidad.setFecha(sc.next()+"/"+sc.next()+"/"+sc.next());
        calidad.setMunicipio(largo.substring(2,5));
        StringTokenizer st = new StringTokenizer(largo,"_");
        st.nextToken();
        calidad.setMagnitud(st.nextToken());

        calidad.setTemperaturas(getTemperaturas(linea));

        Optional<Double> max = calidad.getTemperaturas().stream().max(Comparator.comparing(t->t));
        calidad.setMax(max.get());
        Optional<Double> min = calidad.getTemperaturas().stream().min(Comparator.comparing(t->t));
        calidad.setMin(min.get());
        Double media = getMedia(calidad.getTemperaturas());
        calidad.setMedia(media);

        return calidad;
    }

    /**
     * crea la media diaria a partir de una lista de valores double
     * @param temperaturas de todo el dia
     * @return la media
     */
    private Double getMedia(List<Double> temperaturas) {
        double suma= temperaturas.stream().mapToDouble(v -> v).sum();
        //System.out.println("suma: "+suma);
        return suma/ temperaturas.size();
    }

    /**
     * creamos la lista de valores por hora a partir de una String equivalente a una linea del csv
     * @param linea de donde se leeran los datos
     * @return la lista de valores por hora
     */
    private List<Double> getTemperaturas(String linea) {
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
                returner.add(Double.parseDouble(temp));
            }
        }
        //System.out.println("temp: "+returner.size());
        return returner;
    }

    @Override
    public void run() {
        objectGenerator();
    }
}
