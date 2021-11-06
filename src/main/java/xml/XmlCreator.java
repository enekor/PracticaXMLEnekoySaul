package xml;

import Csv.Lanzador;
import Mapas.EstacionesMapas;
import Mapas.MagnitudMap;
import Mapas.UdMedidaMapa;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class XmlCreator {

    Document dom = new Document();
    Lanzador datos = new Lanzador();
    String uriInput = System.getProperty("user.dir")+ File.separator+"target"+File.separator+"generated-sources";
    MagnitudMap mm = MagnitudMap.getInstance();
    EstacionesMapas em = EstacionesMapas.getInstance();
    UdMedidaMapa umm = UdMedidaMapa.getInstance();
    String uri = System.getProperty("user.dir")+File.separator+"target"+File.separator+"db"+File.separator+"mediciones.xml";

    /**
     * creacion del xml filtrado por el municipio
     * @param municipio por el que se filtrara
     */
    public void crearXML(String municipio){
        Element root = new Element("datos");
        Element municipioElement = new Element("municipio");
        municipioElement.setAttribute("nombre",em.getCodigoMunicipio().get(Integer.parseInt(municipio)));
        Element rootCalidad = null;
        Element rootMeteo = null;
        Element calidad = new Element("calidad_aire");
        Element meteo = new Element("datos_meteorologicos");

        SAXBuilder sax = new SAXBuilder();
        try{
            rootCalidad=sax.build(uriInput+File.separator+"datosCalidad.xml").getRootElement();
            rootMeteo=sax.build(uriInput+File.separator+"datosMeteo.xml").getRootElement();
            //System.out.println(rootCalidad.getChild("datos").getChild("municipio").getText());
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }

        for (int i=0;i<432;i++){
            if(i<81 || i==431) {
                Element elemento = crearHijo(rootCalidad, municipio,i);
                if(elemento!=null){
                    calidad.addContent(elemento);
                }
            }
            if(i>=81 && i!=431) {
                Element elemento = crearHijo(rootMeteo, municipio,i);
                if(elemento!=null){
                    meteo.addContent(elemento);
                }
            }
        }

        municipioElement.addContent(calidad);
        municipioElement.addContent(meteo);
        root.addContent(municipioElement);

        dom.setRootElement(root);
        generateXML(dom);
    }

    /**
     * creacion de los nodos hijos ya filtrados y sacadas medias mensuales de todos los datos aportados por el xml de datos
     * @param rootElement root del xml de donde sacaremos los datos
     * @param municipio por el que filtrar
     * @param magnitud a ver
     * @return elemento filtrado
     */
    private Element crearHijo(Element rootElement, String municipio, int magnitud) {
        if(mm.getMapa().containsKey(magnitud)) {

            Element medicion = new Element(mm.getMapa().get(magnitud));
            Element maximo = new Element("maximo_mensual");
            Element minimo = new Element("minimo_mensual");
            Element media = new Element("media_mensual");
            Element fecha = new Element("fecha");

            List<Element> datos = rootElement.getChildren("datos");
            List<Double> maximas = new ArrayList<>();
            List<Double> minimos = new ArrayList<>();
            List<Double> medias = new ArrayList<>();
            String date = rootElement.getChild("datos").getChild("fecha").getText();
            fecha.addContent(date);

            datos.forEach(v -> {
                if (v.getChild("municipio").getText().equalsIgnoreCase(municipio) && v.getChild("magnitud").getText().equalsIgnoreCase(String.valueOf(magnitud))) {
                    maximas.add(Double.parseDouble(v.getChild("temp_max").getText()));
                    minimos.add(Double.parseDouble(v.getChild("temp_min").getText()));
                    medias.add(Double.parseDouble(v.getChild("temp_media").getText()));
                }
            });
            if(maximas.size()!=0) {
                Optional<Double> max = maximas.stream().max(Comparator.comparing(v -> v));
                maximo.setText(max.get()+" "+umm.getUdMedida().get(magnitud));
            }
            if(minimos.size()!=0) {
                Optional<Double> min = minimos.stream().min(Comparator.comparing(v -> v));
                minimo.setText(min.get()+" "+umm.getUdMedida().get(magnitud));
            }
            double med = (medias.stream().mapToDouble(v->v).sum())/ medias.size();
            media.setText(med+" "+umm.getUdMedida().get(magnitud));

            medicion.addContent(maximo);
            medicion.addContent(minimo);
            medicion.addContent(media);


            return medicion;
        }
        return null;
    }

    /**
     * creacion del xml creado
     * @param dom del xml a crear
     */
    private void generateXML(Document dom){
        XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());

        String uri = System.getProperty("user.dir")+File.separator+"target"+File.separator+"db";
        File dir = new File(uri);
            if(!dir.exists()){
                dir.mkdirs();
            }
        uri = uri+File.separator+"mediciones.xml";
        File archivo = new File(uri);

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
                xml.output(dom,bw);

                System.out.println("xml creado en la uri "+uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     *
     * @throws IOException
     * @throws JDOMException
     */
    public void loadData() throws IOException, JDOMException {

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(this.uri);
        this.dom = (Document) builder.build(xmlFile);
    }

    /**
     *
     * @return
     */
    public List<String> obtenerMediasMensuales() {
        XPathFactory xpath = XPathFactory.instance();
        XPathExpression<Element> expr = xpath.compile("//media_mensual", Filters.element());
        List<Element> medias = expr.evaluate(this.dom);
        List<String> listaMedias = new ArrayList<String>();
        medias.forEach(m -> listaMedias.add(m.getValue().trim()));
        return listaMedias;
    }
}
