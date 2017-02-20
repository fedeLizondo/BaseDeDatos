package LogicaNegocio;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by federicolizondo on 25/01/17.
 */
public class DFDeterminadoComplejo extends DependenciaFuncional {

    private String determinante;
    private ArrayList<String> determinado ;

    public DFDeterminadoComplejo(String Determinante, ArrayList<String> Determinado) {
        super();
        this.determinante = Determinante;
        this.determinado = Determinado;
    }

    @Override
    public boolean tengoDF(String determinante, String determinado) {

        return this.determinante.equals(determinante) && this.determinado.contains(determinado);
    }

    @Override
    public ArrayList<DependenciaFuncional> convertirAFmin() {

        ArrayList<DependenciaFuncional> DF = new ArrayList<DependenciaFuncional>();
        for (String s : determinado) {
          DF.add(new DFSimple(this.determinante,s));
        }
        return DF;
    }

    @Override
    public ArrayList<String> getDeterminante() {
        ArrayList<String> ld = new ArrayList<String>();
        ld.add(this.determinante);
        return ld;
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
        return false;
    }

    @Override
    public String toString() {

        return determinante+" -> "+determinado;
    }

    @Override
    public int hashCode() {
        return (this.determinante+this.determinado.toString()).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this){ return true; }
        if (o != null && o instanceof DependenciaFuncional)
        {
            DependenciaFuncional DF = (DependenciaFuncional) o;
            ArrayList<String> lDeterminantes = DF.getDeterminante();
            ArrayList<String> lDeterminado = DF.getDeterminado();

            boolean contieneDeterminante = lDeterminantes.contains(determinante);
            boolean contieneDeterminado = this.determinado.containsAll(lDeterminado);
            boolean mismoTamanioDeterminante = lDeterminantes.size() == 1;
            boolean mismoTamanioDeterminado = this.determinado.size() == lDeterminado.size();

            return  contieneDeterminante && contieneDeterminado && mismoTamanioDeterminado && mismoTamanioDeterminante ;
        }
        return false;
    }

    @Override
    public ArrayList<String> dameAtributos() {
        ArrayList<String> aux = new ArrayList<String>();
        aux.add(determinante);
        aux.addAll(determinado);
        aux = new ArrayList<String>(new HashSet<String>(aux));

        return aux;
    }
}
