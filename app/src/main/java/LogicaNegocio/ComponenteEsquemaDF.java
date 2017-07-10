package LogicaNegocio;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by federicolizondo on 10/07/17.
 */

public class ComponenteEsquemaDF {

    private ArrayList<String> lEsquema;
    private ArrayList<DependenciaFuncional> lDependenciaFuncional;

    public ComponenteEsquemaDF(ArrayList<String> lEsquema,ArrayList<DependenciaFuncional> lDependenciaFuncionales) {
        this.lEsquema = lEsquema;
        Collections.sort(this.lEsquema);
        this.lDependenciaFuncional = lDependenciaFuncionales;
    }

    public ComponenteEsquemaDF()
    {
        this.lEsquema = new ArrayList<>();
        this.lDependenciaFuncional = new ArrayList<>();
    }

    public void agregarAtributosEsquema(ArrayList<String> esquema)
    {
        if(esquema!=null && !esquema.isEmpty())
        {
            for (String atributo :esquema) {
            if(!lEsquema.contains(atributo))
                lEsquema.add(atributo);
            }
            Collections.sort(lEsquema);
        }
    }

    public void agregarAtributosEsquema(String esquema) {

        if (esquema != null && !esquema.isEmpty() && !lEsquema.contains(esquema)) {
            lEsquema.add(esquema);
            Collections.sort(lEsquema);
        }
    }

    public boolean tengoEsquema(ArrayList<String> esquema )
    {
        if(esquema == null || esquema.isEmpty())
            return false;
        else
        {
            return  lEsquema.containsAll(esquema);
        }
    }

    public void agregarDependenciasFuncionales(ArrayList<DependenciaFuncional> lDependenciaFuncional)
    {
        if(lDependenciaFuncional != null && !lDependenciaFuncional.isEmpty() )
        {
            for (DependenciaFuncional df :lDependenciaFuncional) {
                if(!this.lDependenciaFuncional.contains(df))
                {
                    this.lDependenciaFuncional.add(df);
                    agregarAtributosEsquema(df.dameAtributos());
                }
            }
        }
    }

    public ArrayList<String> getlEsquema() {
        return lEsquema;
    }

    public ArrayList<DependenciaFuncional> getlDependenciaFuncional() {
        return lDependenciaFuncional;
    }

    public boolean tieneElDeterminante(ArrayList<DependenciaFuncional> dependencias)
    {
        boolean resultado = false;

        if(!lDependenciaFuncional.isEmpty() && !dependencias.isEmpty())
        {
           ArrayList<String> determinante = lDependenciaFuncional.get(0).getDeterminante();
           ArrayList<String> determinanteAComparar = dependencias.get(0).getDeterminante();

           return determinante.containsAll(determinanteAComparar) && determinante.size() == determinanteAComparar.size();
        }

        return resultado;
    }

    public ArrayList<DependenciaFuncional> tengoDeterminante(ArrayList<String> determinante)
    {
        if(determinante==null)
            return new ArrayList<>();

        ArrayList<DependenciaFuncional> resultado = new ArrayList<>();

        for(DependenciaFuncional dependenciaFuncional: (ArrayList<DependenciaFuncional>)lDependenciaFuncional.clone())
        {
            ArrayList<String> determinanteDF = dependenciaFuncional.getDeterminante();
            if(determinante.containsAll(determinanteDF) && determinante.size() == determinanteDF.size() && !resultado.contains(dependenciaFuncional))
            {
              resultado.add(dependenciaFuncional);
            }
        }
        return resultado;
    }

    public ArrayList<ArrayList<String>> darListadoDeterminantes()
    {
       ArrayList<ArrayList<String>> resultado = new ArrayList<>();

        for (DependenciaFuncional df: lDependenciaFuncional) {
            ArrayList<String> determinantes = df.getDeterminante();
            Collections.sort(determinantes);
            if(!resultado.contains(determinantes))
            {
                resultado.add(determinantes);
            }

        }

       return resultado;
    }

    @Override
    public String toString() {
        return  "(" +  lEsquema.toString().replace("[","").replace("]","") +
                "){" + lDependenciaFuncional.toString().substring(1,lDependenciaFuncional.toString().lastIndexOf("]")) +
                '}';
    }
}
