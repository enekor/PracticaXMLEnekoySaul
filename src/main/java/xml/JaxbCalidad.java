package xml;


import Csv.Lanzador;
import Objetos.POJODatos;
import lombok.Data;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "DatosCalidad")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbCalidad {
    List<POJODatos> listaCalidad = new ArrayList<>();

    /**
     * creacion del xml calidad aire
     * @throws InterruptedException
     */
    public void cargarDatos() throws InterruptedException{
        Lanzador l = Lanzador.getInstance();
        l.empezar();
        listaCalidad= l.getListaCalidad();
    }
}
