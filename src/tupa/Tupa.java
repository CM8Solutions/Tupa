/*
Pääohjelmaluokka
 */
package tupa;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;
import static javafx.application.Application.launch;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import tupa.data.Kohde;
import tupa.data.Turnaus;
import tupa.data.Sarja;
import tupa.data.Tuomari;
import tupa.data.Pelaaja;
import tupa.data.Toimihenkilo;
import tupa.data.Joukkue;
import tupa.kontrollerit.Valikko;
import tupa.kontrollerit.Aloitus;
import tupa.kontrollerit.KayttajanKirjautuminen;
import tupa.kontrollerit.Puurakenne;
import tupa.kontrollerit.PuuSoluTehdas;
import tupa.kontrollerit.Varmistaja;
import tupa.nakymat.PaaNakyma;
import tupa.nakymat.Pysyvat;

/**
 *
 * @author Marianne
 */
public class Tupa extends Application {

    //käyttöoikeudet 0 = vain turnauksen tietojen katselu, 1 = joukkueen ylläpitäjä/oikeudet uusien turnauksien lisäämiseen, 2=lisenssin ostanut käyttäjä, joka voi luoda max 5 turnausta ja hallita niitä, 3=yleinen ylläpitäjä
    private int taso = 0;
   
    //käyttäjän id muistiin
    private int kayttaja_id = 0;
    
    //käyttäjän hallinnoiman joukkueen id muistiin
    private int joukkue_id = 0;
    
    private boolean alotus = true;
   
    // turnaus, johon liittyy, tallennettu!
    private Kohde turnaus = new Turnaus();

    //kaikki kohteet tallennetaan tähän
    private List<Kohde> kohdetk = new ArrayList<>();

    //jotka siirretään ohjelman alkaessa omiin listoihin
    private List<Sarja> sarjatk = new ArrayList<>();
    private List<Tuomari> tuomaritk = new ArrayList<>();
    private List<Joukkue> joukkuetk = new ArrayList<>();
    private List<Pelaaja> pelaajatk = new ArrayList<>();
    private List<Toimihenkilo> toimaritk = new ArrayList<>();

    //yläosaan valikko
    private Valikko valikko;

    //alaosan loki
    private ListView loki = new ListView();
    private List<String> lokilista = new ArrayList<>();
    //sivulle puurakenne, jossa valikko
    private TreeView<Kohde> sivuPuu = new TreeView<>();

    //keskiosaan ylös logo ja keskelle varsinainen näyttö
    private VBox keski = new VBox();

    //nayttö on stackpane että asettaa uudet näkymät päällekkäin siten, ettei vanhat enää näy
    private StackPane naytto = new StackPane();

    //puurakenteen "juuret"
    private TreeItem<Kohde> rootSarjat;
    private TreeItem<Kohde> rootTuomarit;
    private PaaNakyma nakyma;
    //pitää kirjaa, onko muutoksia tehty
    private boolean muutettu;

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage primaryStage) {
     
                nakyma = new PaaNakyma(this);
        Stage kirjautuja = new KayttajanKirjautuminen(primaryStage, this);
  
      
       kirjautuja.sizeToScene();
        kirjautuja.showAndWait();
        //luodaan turnaus, kun ohjelma käynnistyy
        Aloitus aloitus = new Aloitus();
        turnaus = aloitus.luoAlkuTurnaus();
        kohdetk.add(turnaus);

        BorderPane border = new BorderPane();

     
        //keskinäytön tyylittely
        naytto.setStyle("-fx-background-color: white;");

        //aloitusnäkymä
 
nakyma.luoEtusivuTyhja();
        //muodostaan ylävalikko
        MenuBar menuBar = new MenuBar();
        valikko = new Valikko(menuBar, this);
        menuBar = valikko.rakennaValikko();

        menuBar.setPrefWidth(600);
        border.setTop(menuBar);

        //alaosaan tulee toimintaloki
        Pysyvat osiot = new Pysyvat(this);

        border.setBottom(osiot.rakennaAlaosa());

        //muodostetaan ensin sivupuu
        Puurakenne puu = new Puurakenne(this);
        sivuPuu = puu.rakennaPuu();

        PuuSoluTehdas tehdas = new PuuSoluTehdas(this);
        sivuPuu.setCellFactory(tehdas);
        // juuri näkymättömäksi
        sivuPuu.setShowRoot(false);

        //jonka jälkeen rakennetaan ikkunan vasen puoli
     
        border.setLeft(osiot.rakennaVasensivu(sivuPuu));
   
            
        //ylaosan "logo"
        keski.getChildren().add(osiot.rakennaYlaosa());
        keski.getChildren().add(naytto);

        border.setCenter(keski);

      
        
         Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
     Scene scene = new Scene(border, screenBounds.getWidth(), screenBounds.getHeight());
        
        primaryStage.setTitle("TUPA \t - \t Tulospalvelu");
        scene.getStylesheets().add("css/tyylit.css");

        primaryStage.getIcons().add(new Image("kuvat/icon.png"));
        primaryStage.setScene(scene);
        Platform.setImplicitExit(false);
       
        primaryStage.show();
        Varmistaja varmista = new Varmistaja(kohdetk, this);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent we) {

                if (muutettu()) {
                    we.consume();

                    varmista.annaVarmistus();
                } else {
                    Platform.exit();
                }

            }
        });
      
   
  
    }

    public PaaNakyma annaPaaNakyma() {
        return nakyma;
    }

    public boolean muutettu() {

        if (muutettu == true) {
            return true;
        } else {
            return false;
        }

    }

    public List<Sarja> annaSarjatk() {
        return sarjatk;
    }

    public void asetaRootSarjat(TreeItem<Kohde> rs) {
        rootSarjat = rs;
    }

    public List<Tuomari> annaTuomaritk() {
        return tuomaritk;
    }

    public void asetaRootTuomarit(TreeItem<Kohde> rt) {
        rootTuomarit = rt;
    }

    public TreeItem<Kohde> annaRootSarjat() {
        return rootSarjat;
    }

    public TreeItem<Kohde> annaRootTuomarit() {
        return rootTuomarit;
    }

    public List<Joukkue> annaJoukkuetk() {
        return joukkuetk;
    }

    public List<Pelaaja> annaPelaajatk() {
        return pelaajatk;
    }

    public List<Toimihenkilo> annaToimaritk() {
        return toimaritk;
    }

    public List<Kohde> annaKohteet() {
        return kohdetk;
    }

    public void asetaKohteet(List<Kohde> kohdetk) {
        this.kohdetk = kohdetk;
    }

    public ListView annaLoki() {
        return loki;
    }

    public StackPane annaNaytto() {
        return naytto;
    }

    public TreeView<Kohde> annaSivuPuu() {
        return sivuPuu;
    }

    public void asetaTurnaus(Kohde turnaus) {
        this.turnaus = turnaus;
    }

    public Kohde annaTurnaus() {
        return turnaus;
    }

    public void asetaMuutos(boolean muutettu) {
        this.muutettu = muutettu;
    }

    public List<String> annaLokilista() {
        return lokilista;
    }
    
    public void asetaTaso(int taso){
        this.taso = taso;
    }
    
    public int annaTaso(){
        return taso;
    }
    
    public void asetaAloitus(boolean arvo){
        this.alotus = arvo;
    }
    
    public boolean annaAloitus(){
        return alotus;
    }
    
    public void asetaKayttajaID(int id){
        this.kayttaja_id = id;
    }
    
    public int annaKayttajaID(){
        return kayttaja_id;
    }
    
    public void asetaJoukkueID(int id){
        this.joukkue_id = id;
    }
    
    public int annaJoukkueID(){
        return joukkue_id;
    }
    
}
