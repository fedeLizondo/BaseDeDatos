package LogicaNegocio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by federicolizondo on 21/02/17.
 */

public class Esquemas implements Serializable {

    private ArrayList<ArrayList<String>> esquemas;


    public Esquemas()
    {
        esquemas = new ArrayList<>();
    }

    public void agregarEsquema(ArrayList<String> esquema)
    {
        if(esquema!=null && !esquema.contains(esquema))
            esquemas.add(esquema);
    }

    public void modificarEsquema(ArrayList<String> esquemaAnterior,ArrayList<String> esquemaNuevo)
    {
        if(esquemaAnterior!=null && esquemaNuevo != null && esquemas.contains(esquemaAnterior) && !esquemas.contains(esquemaNuevo))
        {
            int index = esquemas.indexOf(esquemaAnterior);
            esquemas.remove(index);
            esquemas.add(index,esquemaNuevo);
        }
    }

    public void eliminarEsquema(ArrayList<String> esquema)
    {
        if(esquemas.contains(esquema))
            esquemas.remove(esquema);
    }


    public ArrayList<ArrayList<String>> getEsquemas()
    {
        return (ArrayList<ArrayList<String>>)esquemas.clone();
    }

}
