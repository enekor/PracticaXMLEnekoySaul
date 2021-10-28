package Mapas;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UdMedidaMapa {

    private Map<Integer,String> udMedida = new HashMap<>();

    private static UdMedidaMapa udMedidas=null;

    private UdMedidaMapa(){
        initMapa();
    }

    public static UdMedidaMapa getInstance(){
        if (udMedidas==null){
            udMedidas = new UdMedidaMapa();
        }
        return udMedidas;
    }

    private void initMapa(){
        udMedida.put(1,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(6,"mg/m³");
        udMedida.put(7,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(8,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(9,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(10,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(12,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(14,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(20,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(22,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(30,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(42,"mg/m³");
        udMedida.put(44,"mg/m³");
        udMedida.put(431,"ug/m³ (microgramos por metro cubico)");
        udMedida.put(81,"m/s");
        udMedida.put(82,"Grd");
        udMedida.put(83,"ºc");
        udMedida.put(86,"%");
        udMedida.put(87,"mbar");
        udMedida.put(88,"W/m2");
        udMedida.put(89,"l/m2");
    }
}
