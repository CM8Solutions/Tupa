package tupa.kontrollerit;

import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
import tupa.data.Yhteys;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Luokka, joka hoitaa valitun tuomarin tietojen viennin tietokannasta tiedostoon.
 * 
 * @author Marianne
 */
public class Vie {

    private Connection con = null;
    private Statement st = null;
    private Statement st2 = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";

    public Vie() {

    }

    public void vieTiedostoon(int id, int kayttaja_id) throws ParserConfigurationException, SQLException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element results = doc.createElement("Tulokset");
        doc.appendChild(results);

        try {

            con = yhteys.annaYhteys();
            st = con.createStatement();
            st2 = con.createStatement();

            ResultSet rs = st.executeQuery("select * from tuomari where tupaid = '" + id + "'");

            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            while (rs.next()) {
                Element row = doc.createElement("Rivi");
                results.appendChild(row);
                for (int i = 1; i <= colCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object value = rs.getObject(i);
                    Element node = doc.createElement(columnName);
                    node.appendChild(doc.createTextNode(value.toString()));
                    row.appendChild(node);
                }
            }
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);
            String xmlPath = "lib/tuomarit_'" + id + "'_'" + kayttaja_id + "'.xml";
            StreamResult result = new StreamResult(new File(xmlPath).getPath());
            transformer.transform(domSource, result);

            st2.executeUpdate("UPDATE tuomari SET viety_tiedostoon = 1 WHERE tupaid = '" + id + "'");

            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaIlmoitus("Tuomarin tiedot on viety tiedostoon onnistuneesti!");

        } catch (SQLException se) {

            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaVirhe("" + se);
        } catch (Exception e) {

            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaVirhe("" + e);
        } finally {

            try {
                if (st != null) {
                    con.close();
                }
            } catch (SQLException se) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + se);
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + se);
            }
        }

    }

}
