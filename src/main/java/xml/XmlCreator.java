package xml;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XmlCreator {

    /**
     *creacion del xml a partir de un csv y la lista de atributos
     * @param attribute lista de atributos
     * @param csvUri uri del csv a leer
     * @param xmlName nombre del xml a crear
     * @throws IOException
     */
    public void createXML(List<String> attribute,String csvUri, String xmlName) throws IOException {
        Element rootElement = new Element(xmlName);
        Document dom = new Document();

        Long maxLines = Files.lines(Path.of(csvUri), Charset.forName("windows-1252")).count();
        /*System.out.println(maxLines);
        System.out.println(attribute.size());*/
        BufferedReader reader = new BufferedReader(new FileReader(new File(csvUri)));


        reader.readLine();
        String line;
        while((line=reader.readLine()) != null) {
            List<String> values = Stream.of(line.split(";")).collect(Collectors.toList());
            rootElement = elementsCreator(attribute, values,"elemento", rootElement);
        }

        /*System.out.println(rootElement);*/
        dom.setRootElement(rootElement);

        xmlGenerator(dom,xmlName);
    }

    /**
     * creamos elementos a partir de una lista de valores obtenido por cada linea del csv
     * @param attributes de cada valor
     * @param values valores a añadir
     * @param parent nombre del elemento
     * @param root elemento root donde almacenaremos los nodos hijo
     * @return el elemento root con todos los nodos
     */
    private Element elementsCreator(List<String> attributes, List<String> values, String parent, Element root){
        Element rootElement = root;
        Element rootChild = new Element(parent);

        for(int i=0;i<attributes.size();i++){
            if(i>3) {
                 rootChild.addContent(createElement(attributes.get(i), values.get(i)));
            }
        }
        rootElement.addContent(rootChild);
        return rootElement;
    }

    /**
     * creamos los nodos con los valores
     * @param name nombre del nodo
     * @param content contenido del nodo
     * @return el elemento nodo
     */
    private Element createElement(String name, String content){
        Element child = new Element(name);
        child.addContent(content);

        return child;
    }

    /**
     * generamos el xml a partir del dom generado anteriormente
     * @param dom del que sacaremos el xml
     * @param name nombre del xml
     */
    private void xmlGenerator(Document dom, String name){
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        String xmlPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources";
        File xml = new File(xmlPath);
        if(!xml.exists()){
            xml.mkdirs();
        }
        xml= new File(xmlPath+File.separator+name+".xml");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(xml));
            xmlOutput.output(dom,bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
