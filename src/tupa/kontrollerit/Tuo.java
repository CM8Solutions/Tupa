package tupa.kontrollerit;

import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Marianne
 */
public class Tuo {

    private Connection con = null;
    private Statement st = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";
    private String xmlPath;

    public Tuo() {

    }

    public void tuoTiedostosta(int id) throws ParserConfigurationException, SQLException, TransformerConfigurationException, TransformerException {
        xmlPath = "src/tiedostot/tuomarit_'" + id + "'.xml";
        try {

            File inputFile = new File(xmlPath);

            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Tuomarit :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Rivi");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("Tuomari: "
                        + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    System.out.println("tuomari_id: "
                            + eElement
                            .getElementsByTagName("tuomari_id")
                            .item(0)
                            .getTextContent());
                    System.out.println("etunimi: "
                            + eElement
                            .getElementsByTagName("etunimi")
                            .item(0)
                            .getTextContent());
                    System.out.println("sukunimi: "
                            + eElement
                            .getElementsByTagName("sukunimi")
                            .item(0)
                            .getTextContent());
                    System.out.println("id: "
                            + eElement
                            .getElementsByTagName("id")
                            .item(0)
                            .getTextContent());
                    System.out.println("turnaus_id: "
                            + eElement
                            .getElementsByTagName("turnaus_id")
                            .item(0)
                            .getTextContent());
                    System.out.println("tupaid: "
                            + eElement
                            .getElementsByTagName("tupaid")
                            .item(0)
                            .getTextContent());
                    System.out.println("sukunimi: "
                            + eElement
                            .getElementsByTagName("sukunimi")
                            .item(0)
                            .getTextContent());
                    System.out.println("viety_tiedostoon: "
                            + eElement
                            .getElementsByTagName("viety_tiedostoon")
                            .item(0)
                            .getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
