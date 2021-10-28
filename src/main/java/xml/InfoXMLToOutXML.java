package xml;

import Mapas.EstacionesMapas;
import Mapas.MagnitudMap;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

public class InfoXMLToOutXML {

    Document domCalidad;
    Document domMeteo;
    EstacionesMapas em = EstacionesMapas.getInstance();
    MagnitudMap mm = MagnitudMap.getInstance();

    private void domGenerator(String nameCalidad,String nameMeteo,String newName){
        File calidadXml = new File(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+nameCalidad+".xml");
        File meteoXml = new File(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+nameMeteo+".xml");
        SAXBuilder builder = new SAXBuilder();
        try {
            domCalidad = builder.build(calidadXml);
            domMeteo = builder.build(meteoXml);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    private void leerNodos(Document dom,String name){
        Element rootElement = new Element("InfoMeteorologica");
        rootElement.setAttribute(new Attribute("municipio",name));

    }

    private void crearElementoRoot(Document dom, Element root, int municipio){
        Element rootElement = root; //elemento root

        Element rootChild;//solo muestreo
        while((rootChild=rootElement.getChild("element"))!=null){
            if(Integer.parseInt(rootChild.getChildText("punto_muestreo").substring(2,5))==municipio) {
                Element elemento = new Element("DatosClimaticos").setAttribute(new Attribute("Municipio",
                        em.getCodigoMunicipio().get(rootChild.getChildText("punto_muestreo").substring(2, 5)))); //contenedor de informacion


                Element medicion = new Element(mm.getMapa().get(getmagnitudByPuntoMuestreo(rootChild.getChildText("punto_muestreo")))); //elemento por medicion
            }
        }
    }

    private int getmagnitudByPuntoMuestreo(String muestreo){
        StringTokenizer st = new StringTokenizer(muestreo,"_");
        st.nextToken();
        return Integer.parseInt(st.nextToken());
    }

    private Element createElement(String name, String content){
        Element child = new Element(name);
        child.addContent(content);

        return child;
    }

    /*public static void main(String[] args) {
        newXmlGeneratorFromXml("calidadAire","rojo");
    }*/
}
