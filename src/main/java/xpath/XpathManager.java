package xpath;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XpathManager {

    private Document dom = new Document();
    private String uri = System.getProperty("user.dir")+File.separator+"target"+File.separator+"db"+File.separator+"mediciones.xml";
    private List<String> listaMedias;
    private String fecha="";

    /**
     * inicializa el dom a usar
     * @throws IOException
     * @throws JDOMException
     */
    private void loadData() throws IOException, JDOMException {

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(this.uri);
        this.dom = (Document) builder.build(xmlFile);
    }

    /**
     * obtiene las media mensuales a paritr del xml de datos, para ello crea strings personalizadas con la magnitud a la que hace referencia y el valor de la media mensual
     * @return la lista de valores medios a utilizar en md y a sacara por pantalla
     */
    public List<String> obtenerMediasMensuales() {
        try {
            loadData();
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        XPathFactory xpath = XPathFactory.instance();
        XPathExpression<Element> expr = xpath.compile("//medicion", Filters.element());
        List<Element> medias = expr.evaluate(this.dom);
        List<String> listaMedias = new ArrayList<String>();
        medias.forEach(m -> {
            fecha = m.getChildText("fecha_medicion");
            String valor =  m.getChild("media_mensual").getText();
            if (valor.equalsIgnoreCase("")){
                valor = "no hay valores sobre esta medicion";
            }
            String aniadir = m.getAttributeValue("nombre") + ": " + valor;
            listaMedias.add(aniadir);

        });
        return listaMedias;
    }

    public String getFecha() {
        return fecha;
    }

    /*public static void main(String[] args) {
        XpathManager manager = new XpathManager();
        manager.obtenerMediasMensuales().forEach(System.out::println);
    }*/
}
