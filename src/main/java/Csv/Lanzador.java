package Csv;

import Objetos.POJODatos;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
public class Lanzador {
    List<POJODatos> listaCalidad = new ArrayList<>();
    List<POJODatos> listaMeteo = new ArrayList<>();
    ThreadGroup tg = new ThreadGroup("lectoresCSV");

    private static Lanzador launcher = null;
    private Lanzador(){}
    public static Lanzador getInstance(){
        if(launcher==null){
            launcher = new Lanzador();
        }
        return launcher;
    }

    /**
     * cramos hilos independientes que lean los csv a la vez y consigan los datos nev¡cesarios para el programa
     * @throws InterruptedException
     */
    public void empezar() throws InterruptedException {
        CalidadReader car = CalidadReader.getInstance();
        MeteoReader dmr = MeteoReader.getInstance();
        CreadorMapMunicipios cmm = CreadorMapMunicipios.getInstance();

        Thread hilo1 = new Thread(tg,car);
        Thread hilo2 = new Thread(tg,dmr);
        Thread hilo3 = new Thread(tg,cmm);


        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo1.join();
        hilo2.join();
        hilo3.join();

        listaCalidad = car.getListaCalidad();
        listaMeteo = dmr.getListaMeteo();

        // System.out.println(calidadList.size()+" "+calidadZonasList.size()+" "+datosList.size());
    }
}
