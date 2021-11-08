import org.jdom2.JDOMException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, JDOMException, JAXBException {
            if(args.length==2){
                Funcional funcional = Funcional.getInstance();
                funcional.start(args[0],args[1]);
            }
            else{
                System.out.println("valores aportados no validos, se busca: municipio uri");
            }
        }
    }