package tupa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marianne & Victor
 */
public class Tallennus {

    public static void main(String[] args) {
    }

    private String tallennusTiedosto = "TUPA_tallennus";
    private Tupa ikkuna;

    private List<Kohde> kohdetk = new ArrayList<>();

    Tallennus() {

    }

    Tallennus(Tupa ikkuna) {

        this.ikkuna = ikkuna;
    }

    public void suoritaTallennus() throws InstantiationException, SQLException, IllegalAccessException {

        kohdetk = ikkuna.annaKohteet();
        String url = "jdbc:mysql://tite.work:3306/";
        String dbName = "tupa";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "asdlol";
        Connection conn = null;
        Statement st = null;
        try {

            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
            st = conn.createStatement();
            if (!kohdetk.isEmpty()) {

                for (int i = 0; i < kohdetk.size(); i++) {

                    Kohde tiedot = kohdetk.get(i);

                    if (tiedot instanceof Sarja) {
                        Sarja sarja = (Sarja) tiedot;

                    } else if (tiedot instanceof Tuomari) {

                        Tuomari tuomari = (Tuomari) tiedot;
                        int tuomari_id = tuomari.annaJulkinenId();

                        String etunimi = tuomari.annaEtuNimi();
                        String sukunimi = tuomari.annaSukuNimi();

                    } else if (tiedot instanceof Joukkue) {

                        Joukkue joukkue = (Joukkue) tiedot;

                    } else if (tiedot instanceof Pelaaja) {

                        Pelaaja pelaaja = (Pelaaja) kohdetk.get(i);

                    } else if (tiedot instanceof Toimihenkilo) {

                        Toimihenkilo toimari = (Toimihenkilo) tiedot;

                    } else if (tiedot instanceof Turnaus) {

                        Turnaus turnaus = (Turnaus) tiedot;

                        String nimi = turnaus.toString();
                        int id = turnaus.annaID();
                    
                        int laskuri = turnaus.annaLaskuri();
                        ResultSet turnaukset = st.executeQuery("SELECT * FROM  turnaus");
                        boolean loyty = false;
                        while (turnaukset.next()) {
                           
                            int tid = turnaukset.getInt("id");

                            if (tid == id) {
                                loyty = true;
                                break;
                            }
                            
                        }
                        
                        //ei ollut kannassa ennestään
                        if (!loyty) {
                        
                              st.executeUpdate("INSERT INTO turnaus (id, nimi) VALUES('" + laskuri + "', '" + nimi + "')");
                           
                        }
                        //oli jo kannassa
                        else{
                             st.executeUpdate("UPDATE turnaus SET nimi='" + nimi + "' WHERE id='" + id + "'");
                        }
                          

                    }

                }

            }

        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (st != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //päivitetään tilanne, että tallennus on suoritettu
        ikkuna.asetaMuutos(false);
//		  try {
//		  Class.forName(driver).newInstance();
//		  Connection conn = DriverManager.getConnection(url+dbName,userName,password);
//		  Statement st = conn.createStatement();
//		  ResultSet tuomarit = st.executeQuery("SELECT * FROM  tuomarit");
//                  int laskin = 0;
//		  while (tuomarit.next()) {
//		  int tid = tuomarit.getInt("tuomari-ID");
//                                   
//		  String tEtu = tuomarit.getString("tuomariEtunimi");
//		  String tSuku = tuomarit.getString("tuomariSukunimi");
//                      
//		  System.out.println("Loop-testi");
//                  ++laskin;
//		  System.out.println(tid + "\t" + tEtu + "\t" + tSuku);
//		  }
////                  ResultSet i = st.executeQuery("SELECT COUNT(*) AS idtulostaulu FROM tulostaulu");
////                  
////                  
////                      String kysytietoja = JOptionPane.showInputDialog("Syötä rivitieto"); 
////                      int  rivinum = 1 + laskin ;
////                  
////                  
////                      
////		  int val = st.executeUpdate("INSERT INTO `tulostaulu`(idtulostaulu, tulos) VALUE ("+rivinum+",'"+kysytietoja+"')");
////		  if(val==1)
////			  System.out.print("Lisättiin onnistuneesti arvot");
////                  
//		  
//		//  System.out.println("sulku");
//		//  conn.close();
//		  } catch (Exception e) {
//		  e.printStackTrace();
//		  }

//		  ResultSet tuomarit = st.executeQuery("SELECT * FROM  `tupa`.`tuomarit`");
//                  int laskin = 4;
//                  
//                  System.out.println("Loop-testi");
//		  while (tuomarit.next()) {
//		  int tid = tuomarit.getInt("tuomari-ID");
//                                   
//		  String tEtu = tuomarit.getString("tuomariEtunimi");
//		  String tSuku = tuomarit.getString("tuomariSukunimi");
//                      
//		  System.out.println("Loop-testi");
//                  ++laskin;
//		  System.out.println(tid + "\t" + tEtu + "\t" + tSuku);
//		  }
//        	ResultSet asd = st.executeQuery("SELECT * FROM  `tupa`.`tuomarit`");
        // int v3 =  st.executeUpdate("insert into tuomarit (tuomari-ID) values ('"+laskin+"')");
    }

    public String annaTallennusTiedosto() {
        return tallennusTiedosto;
    }

    private String getCurrentTimeStamp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
