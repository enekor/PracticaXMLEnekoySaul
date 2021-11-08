package xml;

import Csv.Lanzador;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.File;

public class GenerarXmlDatos {
    JaxbCalidad jc = new JaxbCalidad();
    JaxbMeteo jx = new JaxbMeteo();

    /**
     * genera el xml de datos de calidad aire
     * @throws InterruptedException
     * @throws JAXBException
     */
    private void generarXmlCalidad() throws InterruptedException, JAXBException {
        jc.cargarDatos();
        JAXBContext jaxbContext = JAXBContext.newInstance(JaxbCalidad.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String jaxbxml = System.getProperty("user.dir")+ File.separator+"target"+File.separator+"generated-sources"+File.separator+"datosCalidad.xml";
        jaxbMarshaller.marshal(jc, new File(jaxbxml));
    }

    /**
     * genera el xml de datos de datos meteorologicos
     * @throws InterruptedException
     * @throws JAXBException
     */
    private void generarXmlMeteo() throws InterruptedException, JAXBException{
        jx.cargarDatos();
        JAXBContext jaxbContext = JAXBContext.newInstance(JaxbMeteo.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String jaxbxml = System.getProperty("user.dir")+ File.separator+"target"+File.separator+"generated-sources"+File.separator+"datosMeteo.xml";
        jaxbMarshaller.marshal(jx, new File(jaxbxml));
    }

    public void crearXMLData() throws JAXBException, InterruptedException {
        generarXmlCalidad();
        generarXmlMeteo();
    }
}
