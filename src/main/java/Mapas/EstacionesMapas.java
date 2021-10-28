package Mapas;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class EstacionesMapas {

    private static EstacionesMapas estaciones = null;

    private EstacionesMapas(){}

    public static EstacionesMapas getInstance(){
        if(estaciones==null){
            estaciones = new EstacionesMapas();
        }
        return estaciones;
    }

    private Map<Integer,String> codigoMunicipio = new LinkedHashMap<>();
    private Map<Integer,String> codigoNacional = new LinkedHashMap<>();

    /**
     * rellenar los mapas
     * @param codigo de la estacion/municipio
     * @param municipio /estacion linkeado al codigo
     */
    public void fillCodigoMunicipio(int codigo, String municipio){
        codigoMunicipio.put(codigo,municipio);
    }

    public void fillCodigoNacional(int codigo, String estacion){
        codigoNacional.put(codigo,estacion);
    }
}