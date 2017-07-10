package LogicaNegocio;

import java.util.ArrayList;

import java.util.Collections;

/**
 * Created by federicolizondo on 10/07/17.
 */

public class CalculoEficiente {

    private ArrayList<ComponenteEsquemaDF> paso1;
    private ArrayList<ComponenteEsquemaDF> paso2;
    private ArrayList<ComponenteEsquemaDF> paso3;
    private ArrayList<ComponenteEsquemaDF> paso4;
    private ArrayList<String> claveCandidata;

    private ArrayList<String> lAtributos;
    private ArrayList<DependenciaFuncional> lDependeciasFuncionales;

    private boolean existeClaveCandidata;

    public CalculoEficiente(ArrayList<String> lAtributos, ArrayList<DependenciaFuncional> lDependeciasFuncionales,ArrayList<String> claveCandidata) {
        this.lAtributos = lAtributos;
        this.lDependeciasFuncionales = lDependeciasFuncionales;
        this.claveCandidata = claveCandidata;

        existeClaveCandidata = false;
        paso1 = new ArrayList<>();
        paso2 = new ArrayList<>();
        paso3 = new ArrayList<>();
        paso4 = new ArrayList<>();

        paso1.add(new ComponenteEsquemaDF(lAtributos,lDependeciasFuncionales));
        calcularPaso2();
        calcularPaso3();
        calcularPaso4();
    }

    private void calcularPaso2()
    {
        for (DependenciaFuncional df : lDependeciasFuncionales)
        {
            ArrayList<DependenciaFuncional> lDepFuncionales = new ArrayList<>();
            lDepFuncionales.add(df);

            ArrayList<String> AtributosEsquema = df.dameAtributos();
            Collections.sort(AtributosEsquema);
            if(!existeClaveCandidata && df.getDeterminante().containsAll(claveCandidata)  )
                existeClaveCandidata = true;
            paso2.add(new ComponenteEsquemaDF( AtributosEsquema,lDepFuncionales));
        }
    }

    private void calcularPaso3()
    {
        paso3 = new ArrayList<>(paso2);
        if(!existeClaveCandidata)
            paso3.add(new ComponenteEsquemaDF(claveCandidata,new ArrayList<DependenciaFuncional>()));
    }

    @SuppressWarnings("unchecked")
    private void calcularPaso4()
    {

        ArrayList<ComponenteEsquemaDF> recorrido = (ArrayList<ComponenteEsquemaDF>) paso3.clone();

        while(!recorrido.isEmpty())
        {
            ComponenteEsquemaDF cedf = recorrido.get(0);
            recorrido.remove(cedf);
            ArrayList<ComponenteEsquemaDF> elementosABorrar = new ArrayList<>();
            int i = 0;

            while(i < recorrido.size())
            {
                boolean borrarDF = false;
                ComponenteEsquemaDF comparar = recorrido.get(i);


                if( comparar.tieneElDeterminante(cedf.getlDependenciaFuncional()) )
                {
                    cedf.agregarDependenciasFuncionales(comparar.getlDependenciaFuncional());
                    borrarDF = true;
                }
                /*else {
                    //if (cedf.tengoEsquema(comparar.getlEsquema())) {
                    //    cedf.agregarDependenciasFuncionales(comparar.getlDependenciaFuncional());
                    //    borrarDF = true;
                    //}
                }*/
                if(borrarDF)
                {
                    elementosABorrar.add( comparar );
                }
                i++;
            }

            recorrido.removeAll(elementosABorrar);
               paso4.add(cedf);
        }

    }

    public ArrayList<ComponenteEsquemaDF> getPaso1() {
        return paso1;
    }

    public ArrayList<ComponenteEsquemaDF> getPaso2() {
        return paso2;
    }

    public ArrayList<ComponenteEsquemaDF> getPaso3() {
        return paso3;
    }

    public ArrayList<ComponenteEsquemaDF> getPaso4() { return paso4; }
}
