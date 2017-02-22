package LogicaNegocio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by federicolizondo on 21/02/17.
 */

public class Esquemas implements Serializable {

    private ArrayList<ArrayList<String>> esquemas;

    public Esquemas(ArrayList<ArrayList<String>> esquemas)
    {
        this.esquemas = esquemas;
    }

    public ArrayList<ArrayList<String>> getEsquemas()
    {
        return esquemas;
    }

}
