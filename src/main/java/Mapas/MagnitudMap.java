package Mapas;

import java.util.HashMap;
import java.util.Map;

public class MagnitudMap {

    private Map<Integer,String> magnitudes = new HashMap<>();

    private MagnitudMap(){
        buildMap();
    }
    private static MagnitudMap map = null;

    public static MagnitudMap getInstance() {
        if(map==null){
            map = new MagnitudMap();
        }
        return map;
    }

    private void buildMap(){
        magnitudes.put(1,"SO2");
        magnitudes.put(6,"CO");
        magnitudes.put(7,"NO");
        magnitudes.put(8,"N02");
        magnitudes.put(9,"particulas en suspension PM2,5");
        magnitudes.put(10,"particulas en suspension PM10");
        magnitudes.put(12,"Oxidos de nitrogeno");
        magnitudes.put(14,"O3");
        magnitudes.put(20,"tolueno (C7H8)");
        magnitudes.put(22,"Black Carbon");
        magnitudes.put(30,"Benceno (C6H6)");
        magnitudes.put(42,"Hidrocarburos totales");
        magnitudes.put(44,"Hidrocarburos no metalicos");
        magnitudes.put(431,"MetaParaXileno");
        magnitudes.put(81,"Velocidad del viento");
        magnitudes.put(82,"direccion del viento");
        magnitudes.put(83,"temperatura");
        magnitudes.put(86,"Humedad relativa");
        magnitudes.put(87,"presion atmosferica");
        magnitudes.put(88,"Radiacion solar");
        magnitudes.put(89,"Precipitacion");
    }

    public Map<Integer,String> getMapa(){return magnitudes;}
}
