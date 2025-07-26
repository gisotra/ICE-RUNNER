package utilz;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SpriteLoader { /*Classe auxiliar que terá um único método estático "spriteDataLoader", que vai
    retornar um hashmap do tipo <String, SpriteData>*/

        /*Crio um mapa em que as chaves e valores sao: string e uma instancia do tipo SpriteData*/
        public static HashMap<String, SpriteData> spriteMap = new HashMap<>();
    
        /*Esse método cria o meu mapa*/
        public static HashMap<String, SpriteData> spriteDataLoader(){
            if (!spriteMap.isEmpty()) return spriteMap; // já carregado, retorna
            try {
            // Abrindo o arquivo XML
            InputStream is = SpriteLoader.class.getResourceAsStream("/assets/sprites.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            // Pegando todos os elementos <sprite>
            NodeList nodeList = doc.getElementsByTagName("sprite");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Pegando os dados do sprite
                    String name = element.getAttribute("name");
                    String path = element.getElementsByTagName("path").item(0).getTextContent();

                    // Criando o objeto SpriteData e colocando no mapa
                    SpriteData spriteData = new SpriteData(name, path);
                    spriteMap.put(name, spriteData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return spriteMap;
    }
}

