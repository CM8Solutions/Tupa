package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javafx.scene.control.TreeItem;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tupa.Tupa;
import tupa.data.Henkilo;
import tupa.data.Kohde;
import tupa.data.Tuomari;
import tupa.data.Turnaus;
import tupa.data.Yhteys;

/**
 * Luokka, joka hoitaa valitun tuomarin tietojen tuonnin tiedosta.
 * 
 * @author Marianne
 */
public class Tuo {

    private Connection con = null;
    private Statement st = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";
    private String xmlPath;
    private Tupa ikkuna;

    public Tuo() {

    }

    public Tuo(Tupa ikkuna) {
        this.ikkuna = ikkuna;
    }

    public void tuoTiedostosta(int id, int kayttaja_id) throws ParserConfigurationException, SQLException, TransformerConfigurationException, TransformerException {
        xmlPath = "lib/tuomarit_'" + id + "'_'" + kayttaja_id + "'.xml";
        List<Kohde> kohdetk = ikkuna.annaKohteet();
        Turnaus turnaus = (Turnaus) ikkuna.annaTurnaus();
        try {

            File inputFile = new File(xmlPath);

            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Rivi");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String etunimi = eElement.getElementsByTagName("etunimi").item(0).getTextContent();
                    String sukunimi = eElement.getElementsByTagName("sukunimi").item(0).getTextContent();

                    int tupaid = Integer.parseInt(eElement.getElementsByTagName("tupaid").item(0).getTextContent());

                    int julkinen_id = Integer.parseInt(eElement.getElementsByTagName("tuomari_id").item(0).getTextContent());

                    Tuomari tuomari = new Tuomari(etunimi, sukunimi);
                    tuomari.asetaID(tupaid);
                    tuomari.asetaTurnaus(turnaus);

                    tuomari.asetaVienti(1);
                    tuomari.asetaJulkinenId(julkinen_id);

                    turnaus.annaTuomarit().add(tuomari);

                    kohdetk.add((Kohde) tuomari);
                    ikkuna.annaTuomaritk().add(tuomari);

                }

                //tuomarit puuhun aakkosjärjestykseen
                Vector<Henkilo> tuomaritV = new Vector<>();

                for (int i = 0; i < ikkuna.annaTuomaritk().size(); i++) {
                    tuomaritV.add((Henkilo) ikkuna.annaTuomaritk().get(i));

                }

                Collections.sort(tuomaritV);

                TreeItem<Kohde> parentT = new TreeItem<>();
                parentT = ikkuna.annaRootTuomarit();
                parentT.getChildren().clear();

                for (Henkilo tuomari : tuomaritV) {

                    TreeItem<Kohde> uusiKohde = new TreeItem<Kohde>((Kohde) tuomari);
                    parentT.getChildren().add(uusiKohde);

                }

                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaIlmoitus("Tuomarin tiedot on tuotu turnaukseen onnistuneesti!");
                ikkuna.asetaMuutos(true);
            }
        } catch (Exception e) {
            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaVirhe("" + e);
        }
    }
}
