package LogicaNegocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by federicolizondo on 25/01/17.
 */
public class DFCompleja extends DependenciaFuncional {

    private ArrayList<String> determinante;
    private ArrayList<String> determinado;

    public DFCompleja(ArrayList<String> determinante, ArrayList<String> determinado) {
        super();
        this.determinante=determinante;
        this.determinado=determinado;
    }

    @Override
    public boolean tengoDF(String determinante, String determinado) {
        return this.determinante.contains(determinante) && this.determinado.contains(determinado);
    }

    @Override
    public ArrayList<DependenciaFuncional> convertirAFmin() {

        ArrayList<DependenciaFuncional> df = new ArrayList<DependenciaFuncional>();
        for (String s : this.determinado) {
            df.add(new DFDeterminanteComplejo(this.determinante,s));
        }
        return df;
    }

    @Override
    public ArrayList<String> getDeterminante() {
        return this.determinante;
    }

    @Override
    public ArrayList<String> getDeterminado() {
        return this.determinado;
    }

    @Override
    public boolean soyDeterminanteComplejo() {
        return false;
    }

    @Override
    public boolean soyCompleja() {
        return true;
    }

    @Override
    public String toString() {
        return determinante.toString().replace('[',' ').replace(']',' ') + " -> " + determinado.toString().replace('[',' ').replace(']',' ');
    }

    @Override
    public int hashCode() {
        return ( determinante.toString() + determinado.toString()).hashCode() ;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this){ return true; }
        if (o != null && o instanceof DependenciaFuncional)
        {
            DependenciaFuncional DF = (DependenciaFuncional) o;
            ArrayList<String> lDeterminantes = DF.getDeterminante();
            ArrayList<String> lDeterminado = DF.getDeterminado();

            boolean contieneDeterminante = this.determinante.containsAll(lDeterminantes);
            boolean contieneDeterminado = this.determinado.containsAll(lDeterminado);
            boolean mismoTamanioDeterminante = this.determinante.size() == lDeterminantes.size();
            boolean mismoTamanioDeterminado = this.determinado.size() == lDeterminado.size();

            return  contieneDeterminante && contieneDeterminado && mismoTamanioDeterminado && mismoTamanioDeterminante ;

        }
        return false;
    }

    @Override
    public ArrayList<String> dameAtributos() {
        ArrayList<String> aux = new ArrayList<String>();
        aux.addAll(determinante);
        aux.addAll(determinado);
        aux = new ArrayList<String>(new HashSet<String>(aux));
        Collections.sort(aux);
        return (ArrayList<String>) aux.clone();
    }
}
