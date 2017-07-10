package LogicaNegocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by federicolizondo on 10/07/17.
 */

public class CalculoSinPerdidaDeInfo {

    private ArrayList<String> Esquemas;
    private ArrayList<DependenciaFuncional> lDependenciasFuncionales;
    private ArrayList<ComponenteEsquemaDF> paso1;
    private ArrayList<ComponenteEsquemaDF> paso2;
    private ArrayList<ComponenteEsquemaDF> paso3;
    private ArrayList<ComponenteEsquemaDF> pasoContinuar;
    private ArrayList<String> claveCandidata;
    private ArrayList<ArrayList<String>> claves;
    private ArrayList<DependenciaFuncional> DFParaClaves;
    private ArrayList<String> AtributoParaClave;

    public CalculoSinPerdidaDeInfo(ArrayList<String> esquemas, ArrayList<DependenciaFuncional> lDependenciasFuncionales, ArrayList<String> claveCandidata) {
        Esquemas = esquemas;
        this.lDependenciasFuncionales = lDependenciasFuncionales;
        this.claveCandidata = claveCandidata;
        paso1 = new ArrayList<>();
        paso2 = new ArrayList<>();
        paso3 = new ArrayList<>();
        pasoContinuar = new ArrayList<>();

        claves = new ArrayList<>();
        DFParaClaves = new ArrayList<>();
        AtributoParaClave = new ArrayList<>();

        paso1.add(new ComponenteEsquemaDF(esquemas,lDependenciasFuncionales));
        calcularPaso2();
        calcularPaso3();


    }

    @SuppressWarnings("unchecked")
    private void calcularPaso2()
    {
        if(!lDependenciasFuncionales.isEmpty() && !claveCandidata.isEmpty())
        {
            ArrayList<DependenciaFuncional> guardarFNBC = new ArrayList<>();
            for (DependenciaFuncional df : (ArrayList<DependenciaFuncional>) lDependenciasFuncionales.clone()) {
                FormaNormal fn = determinarFormaNormalDF(df, claveCandidata);
                if (fn.soyFNBC()) {
                    guardarFNBC.add(df);
                }
            }

            if (guardarFNBC.isEmpty()) {
                DependenciaFuncional dependenciaFuncional = lDependenciasFuncionales.get(0);
                ArrayList<DependenciaFuncional> listaDependencias = new ArrayList<>();
                listaDependencias.add(dependenciaFuncional);
                //XUA
                paso2.add( new ComponenteEsquemaDF(dependenciaFuncional.dameAtributos(),(ArrayList<DependenciaFuncional>)listaDependencias.clone()));
                //X-A
                listaDependencias.clear();
                ArrayList<DependenciaFuncional> listRecorridoDependencias =   (ArrayList<DependenciaFuncional>) lDependenciasFuncionales.clone();
                listRecorridoDependencias.remove(0);

                ArrayList<String> listaAtributosAuxiliar = (ArrayList<String>) Esquemas.clone();
                listaAtributosAuxiliar.removeAll(dependenciaFuncional.getDeterminado());

                for(DependenciaFuncional df : listRecorridoDependencias)
                {
                    ArrayList<String> atributos = df.dameAtributos();

                    if( listaAtributosAuxiliar.containsAll(atributos) )
                    {
                        listaDependencias.add(df);
                    }
                }
                paso2.add(new ComponenteEsquemaDF(listaAtributosAuxiliar,listaDependencias));
                pasoContinuar.add(new ComponenteEsquemaDF((ArrayList<String>) listaAtributosAuxiliar.clone(),(ArrayList<DependenciaFuncional>) listaDependencias.clone()));
            }
            else
            {
                ArrayList<String> determinados = new ArrayList<>();
                ArrayList<String> Atributos = new ArrayList<>(claveCandidata);
                for(DependenciaFuncional df :guardarFNBC)
                {
                    for( String determinado : df.getDeterminado() )
                    {
                        if(!determinados.contains(determinado))
                        {   determinados.add(determinado);
                            Atributos.add(determinado);
                        }
                    }
                }
                paso2.add(new ComponenteEsquemaDF(Atributos,guardarFNBC));

                ArrayList<DependenciaFuncional> listaGuardarPaso = new ArrayList<>();
                ArrayList<DependenciaFuncional> listaRecorridoDependencias = (ArrayList<DependenciaFuncional>) lDependenciasFuncionales.clone();
                listaRecorridoDependencias.removeAll(guardarFNBC);

                for (DependenciaFuncional df : listaRecorridoDependencias)
                {
                    ArrayList<String> atributos = df.dameAtributos();
                    if(!atributos.contains(determinados))
                    {
                        listaGuardarPaso.add(df);
                    }
                }

                ArrayList<String> atributosPaso = (ArrayList<String>) Esquemas.clone();
                atributosPaso.removeAll(determinados);
                paso2.add(new ComponenteEsquemaDF(atributosPaso,listaGuardarPaso));
                pasoContinuar.add(new ComponenteEsquemaDF((ArrayList<String>) atributosPaso.clone(),(ArrayList<DependenciaFuncional>) listaGuardarPaso.clone()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void calcularPaso3()
    {
        if(!paso2.isEmpty())
        {
            paso3.add(paso2.get(0));
            ArrayList<String> atributosRestantes;

            while(!pasoContinuar.isEmpty())
            {
                ComponenteEsquemaDF paso = pasoContinuar.get(0);
                pasoContinuar.remove(0);

                ArrayList<DependenciaFuncional> listadf = paso.getlDependenciaFuncional();
                atributosRestantes = paso.getlEsquema();
                if(listadf.isEmpty())
                {
                    paso3.add(new ComponenteEsquemaDF(atributosRestantes,new ArrayList<DependenciaFuncional>()));
                }
                else
                {
                    DependenciaFuncional df = listadf.get(0);
                    ArrayList<String> determinado = df.getDeterminado();

                    ArrayList<String> AtributosPaso = (ArrayList<String>) atributosRestantes.clone();
                    AtributosPaso.removeAll(determinado);

                    ArrayList<DependenciaFuncional> paraGuardarEnPaso = new ArrayList<>();
                    paraGuardarEnPaso.add(df);


                    if(listadf.size() == 1 )
                    {
                       paso3.add(new ComponenteEsquemaDF(df.dameAtributos(),listadf));

                        if(!df.dameAtributos().containsAll(atributosRestantes))
                            pasoContinuar.add(new ComponenteEsquemaDF(AtributosPaso,new ArrayList<DependenciaFuncional>()));
                    }
                    else
                    {
                        paso3.add(new ComponenteEsquemaDF((ArrayList<String>) df.dameAtributos().clone(),(ArrayList<DependenciaFuncional>) paraGuardarEnPaso.clone()));

                        ArrayList<DependenciaFuncional> dfRecorrido = (ArrayList<DependenciaFuncional>) listadf.clone();
                        dfRecorrido.remove(0);
                        ArrayList<DependenciaFuncional> dfPasos = new ArrayList<>();

                        for (DependenciaFuncional depfun : dfRecorrido)
                        {
                            ArrayList<String> atributos = df.dameAtributos();
                            if( AtributosPaso.containsAll(depfun.dameAtributos()))
                            {
                                dfPasos.add(depfun);
                            }
                        }
                        pasoContinuar.add(new ComponenteEsquemaDF(AtributosPaso,dfPasos));
                    }

                }
            }
        }//Cierre del IF
    }//Fin del metodo


    private FormaNormal determinarFormaNormalDF(DependenciaFuncional df, ArrayList<String> clave) {
        if (df == null || clave == null)
            return null;

        ArrayList<String> determinante = df.getDeterminante();
        ArrayList<String> determinado = df.getDeterminado();

        if (determinante.containsAll(clave))
            return new FNBC();
        else {
            int tam = determinado.size();
            int i = 0;
            while (i < tam && !clave.contains(determinado.get(i))) {
                i++;
            }
            //SI EL DETERMINADO CONTIENE UNA PARTE DE LA CLAVE ESTA EN 3FN
            if (i < tam)
                return new TerceraFormaNormal(df);

            tam = determinante.size();
            i = 0;
            while (i < tam && !clave.contains(determinante.get(i))) {
                i++;
            }
            //SI EL DETERMINANTE NO CONTIENE PARTE DE LA CLAVE CAND ESTA EN 2FN
            if (i >= tam)
                return new SegundaFormaNormal(df);
            //SI NO ESTA EN NINGUNA DE LAS FORMAS ANTERIORES SI O SI DEBE ESTAR EN 1FN
            return new PrimeraFormaNormal(df);
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ArrayList<String>> calcularClavesCandidatas() {


            ArrayList<String> lposibles;
            ArrayList<String> lprefijoClave = new ArrayList<String>(AtributoParaClave);
            ArrayList<String> determinante = new ArrayList<String>();
            ArrayList<String> determinado = new ArrayList<String>();
            claves.clear();

            if (DFParaClaves.isEmpty()) {
                claves.add(AtributoParaClave);
                return claves;
            }

            //Obtengo todos los Determinantes y todos los Determinados
            for (DependenciaFuncional df : DFParaClaves) {
                determinante.addAll(df.getDeterminante());
                determinado.addAll(df.getDeterminado());
            }

            //Eliminó Elementos Repetidos
            determinante = new ArrayList<String>(new HashSet<String>(determinante));
            determinado = new ArrayList<String>(new HashSet<String>(determinado));

            //Obtengo atributos que no esten en determinantes y determinados;
            lprefijoClave.removeAll(determinado);
            lprefijoClave.removeAll(determinante);

            //Libero Espacio de los determinados
            determinado.clear();

            //Cargo la lista de los posibles componentes de las claves
            lposibles = determinante;

            calcularClavesRecursivo(lprefijoClave, lposibles);

            //ELIMINÓ CLAVES NO CANDIDATAS
            ArrayList<ArrayList<String>> lAuxClave = new ArrayList<ArrayList<String>>(claves);
            ArrayList<ArrayList<String>> lAuxClave2 = new ArrayList<ArrayList<String>>(claves);

            for (ArrayList<String> clave : lAuxClave2) {
                for (ArrayList<String> string : lAuxClave) {
                    if (string.containsAll(clave) && clave.size() < string.size())
                    {
                        Collections.sort(string);
                        claves.remove(string);
                    }
                }
            }


        return (ArrayList<ArrayList<String>>) claves.clone();
    }
    @SuppressWarnings("unchecked")
    private void calcularClavesRecursivo(ArrayList<String> lPrefijos, ArrayList<String> lAtributosPosibles) {

        if (lAtributosPosibles != null && !lAtributosPosibles.isEmpty()) {
            ArrayList<String> lAuxPrefijos = new ArrayList<String>((ArrayList<String>) lPrefijos.clone());
            ArrayList<String> lAuxAtributosPosibles = new ArrayList<String>((ArrayList<String>) lAtributosPosibles.clone());

            for (String atributoPosible : lAtributosPosibles) {

                lAuxAtributosPosibles.remove(atributoPosible);
                lAuxPrefijos.add(atributoPosible);

                Collections.sort(lAuxPrefijos);

                if (calcularUniverso(lAuxPrefijos)) {
                    ArrayList<String> aux = new ArrayList<String>();
                    aux.addAll(lAuxPrefijos);
                    claves.add(aux);
                } else
                    calcularClavesRecursivo(lAuxPrefijos, (ArrayList<String>) lAuxAtributosPosibles.clone());

                lAuxPrefijos.remove(atributoPosible);
            }
        }
    }
    @SuppressWarnings("unchecked")
    public boolean calcularUniverso(ArrayList<String> clave) {

        if (!clave.isEmpty() && !DFParaClaves.isEmpty()) {
            ArrayList<String> lClaves = new ArrayList<String>();
            ArrayList<String> lDeterminate = new ArrayList<String>();
            ArrayList<String> lDeterminado = new ArrayList<String>();
            boolean cambios = true;
            lClaves.addAll((ArrayList<String>) clave.clone());

            while (cambios) {
                cambios = false;
                for (DependenciaFuncional df : Administradora.getInstance().darListadoDependenciasFuncional()) {
                    if (lClaves.containsAll(df.getDeterminante()) && !lClaves.containsAll(df.getDeterminado())) {
                        cambios = true;
                        lClaves.addAll(df.getDeterminado());
                        lClaves = new ArrayList<String>(new HashSet<String>(lClaves));
                    }
                }
            }
            return lClaves.containsAll(AtributoParaClave);
        }
        return false;
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
}
