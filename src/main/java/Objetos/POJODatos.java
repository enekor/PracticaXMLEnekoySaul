package Objetos;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class POJODatos {
    String municipio,estacion,magnitud,fecha;
    List<Double> temperaturas;
    double max,min,media;
}
