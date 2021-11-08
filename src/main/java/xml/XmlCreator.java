package xml;

import Csv.Lanzador;
import Mapas.EstacionesMapas;
import Mapas.MagnitudMap;
import Mapas.UdMedidaMapa;
import markDown.MDCreator;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import xpath.XpathManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class XmlCreator {

    Document dom = new Document();
    Lanzador datos = Lanzador.getInstance();
    String uriInput = System.getProperty("user.dir")+ File.separator+"target"+File.separator+"generated-sources";
    MagnitudMap mm = MagnitudMap.getInstance();
    EstacionesMapas em = EstacionesMapas.getInstance();
    UdMedidaMapa umm = UdMedidaMapa.getInstance();
    String uri = System.getProperty("user.dir")+File.separator+"target"+File.separator+"db"+File.separator+"mediciones.xml";

    /**
     * creacion del xml informativos ya filtrado y con medias mensuales
     * @param municipio al que hace referencia la medicion
     */
    public void crearXML(int municipio){
        Element root = new Element("datos");
        Element municipioElement = new Element("municipio");
        municipioElement.setAttribute("nombre",em.getCodigoMunicipio().get(municipio));
        Element rootCalidad = null;
        Element rootMeteo = null;
        Element calidad = new Element("calidad_aire");
        Element meteo = new Element("datos_meteorologicos");

        SAXBuilder sax = new SAXBuilder();
        try{
            rootCalidad=sax.build(uriInput+File.separator+"datosCalidad.xml").getRootElement();
            rootMeteo=sax.build(uriInput+File.separator+"datosMeteo.xml").getRootElement();
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }

        for (int i=0;i<432;i++){
            if(i<81 || i==431) {
                Element elemento = crearHijo(rootCalidad, municipio,i,true);
                if(elemento!=null){
                    calidad.addContent(elemento);
                }
            }
            if(i>=81 && i!=431) {
                Element elemento = crearHijo(rootMeteo, municipio,i,false);
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
     * cramos el nodo hijo que aniadiremos con los datos de media mensual de la medicion, filtrado por la medicion y el municipio deseados
     * @param rootElement de donde sacaremos la informacion (datosCalidad o datosMeteo)
     * @param municipio al que hace refrencia la medicion
     * @param magnitud a evaluar
     * @param tipo para saber si se esta viendo datos meteorologicos o calidad del aire
     * @return el elemento creado
     */
    private Element crearHijo(Element rootElement, int municipio, int magnitud, boolean tipo) {
        if(mm.getMapa().containsKey(magnitud)) {

            Element medicion = new Element("medicion");
            medicion.setAttribute("nombre",mm.getMapa().get(magnitud));
            Element maximo = new Element("maximo_mensual");
            Element minimo = new Element("minimo_mensual");
            Element media = new Element("media_mensual");
            Element fecha = new Element("fecha_medicion");

            List<Element> datos = null; //aqui esta la mierda
            if(tipo==true){
                datos = rootElement.getChildren("listaCalidad");
                fecha.setText(rootElement.getChild("listaCalidad").getChild("fecha").getText());
            }
            else{
                datos = rootElement.getChildren("listaMeteo");
                fecha.setText(rootElement.getChild("listaMeteo").getChild("fecha").getText());
            }
            List<Double> maximas = new ArrayList<>();
            List<Double> minimos = new ArrayList<>();
            List<Double> medias = new ArrayList<>();

            datos.forEach(v -> {
                if (Integer.parseInt(v.getChild("municipio").getText())==municipio && v.getChild("magnitud").getText().equalsIgnoreCase(String.valueOf(magnitud))) {
                    maximas.add(Double.parseDouble(v.getChild("max").getText()));
                    minimos.add(Double.parseDouble(v.getChild("min").getText()));
                    medias.add(Double.parseDouble(v.getChild("media").getText()));
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
            if(medias.size()!=0) {
                double med = (medias.stream().mapToDouble(v -> v).sum()) / medias.size();
                media.setText(med + " " + umm.getUdMedida().get(magnitud));
            }

            medicion.addContent(maximo);
            medicion.addContent(minimo);
            medicion.addContent(media);
            medicion.addContent(fecha);

            return medicion;
        }
        return null;
    }

    /**
     * generador de xml a partir de un dom
     * @param dom que usaremos para generar el xml
     */
    private void generateXML(Document dom){
        XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());

        String uri = System.getProperty("user.dir")+File.separator+"target"+File.separator+"db";
        File dir = new File(uri);
            if(!dir.exists()){
                dir.mkdirs();
            }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(uri+File.separator+"mediciones.xml"));
            xml.output(dom,bw);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   /* public static void main(String[] args) throws InterruptedException {
        Lanzador l = Lanzador.getInstance();
        l.empezar();
        XmlCreator xmlc = new XmlCreator();
        xmlc.crearXML("123");
        XpathManager manager = new XpathManager();
        manager.obtenerMediasMensuales().forEach(System.out::println);
        MDCreator md = new MDCreator();
        md.mdCreator("C:\\Users\\eneko\\Desktop\\prueba","leganes");
        //Desde aqui no se le puede llamar porque te hace hacerlo static y revienta el programa asique hay que llamarlo desde otra clase
        //loadData();
        //getAllNames().forEach(System.out::println);;
    }*/
}
