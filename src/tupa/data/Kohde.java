package tupa;

import java.io.Serializable;

/**
 *
 * @author Marianne
 */
public class Kohde implements Serializable {

    private String nimi;
    private int id;
    

   Kohde (){
    
   }
    
    Kohde (String nimi) {
        this.nimi = nimi;


    }

    public String toString() {
        return nimi;
    } 
    
    public void asetaNimi(String nimi){
        
        this.nimi = nimi;
    }
 
   public int annaID(){
       return id;
   }
   
   public void asetaID(int id){
       this.id = id;
   }
   
}
