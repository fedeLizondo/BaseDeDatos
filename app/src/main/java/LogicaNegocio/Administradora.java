package LogicaNegocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;


public class Administradora implements Serializable {

    private static Administradora ourInstance = new Administradora();

    public static Administradora getInstance() {
        return ourInstance;
    }

    public static Administradora getInstanceForTesting()
    {
        return  new Administradora();
    }

    private ArrayList<String> lAtributos;
    private ArrayList<DependenciaFuncional> lDependenciasFuncionales;
    private ArrayList<ArrayList<String>> claves;
    private ArrayList<ArrayList<String>> superClaves;

    private ArrayList<DependenciaFuncional> fmin;
    private FormaNormal formaNormal;


    //----------------------------------------------
    private Tableaux tableaux;
    private Esquemas esquema;
    private CalculoEficiente calculoEficiente;
    private CalculoSinPerdidaDeInfo calculoSinPerdidaDeInfo;
    private ArrayList<String> claveCandidata;

    //Pasos FMIN
    private ArrayList<DependenciaFuncional> paso1;
    private ArrayList<DependenciaFuncional> paso2;
    private ArrayList<DependenciaFuncional> paso3;



    private Administradora() {

        Runtime garbage = Runtime.getRuntime();
        garbage.gc();

        lAtributos = new ArrayList<>();
        lDependenciasFuncionales = new ArrayList<>();
        claves = new ArrayList<>();
        superClaves = new ArrayList<>();
        fmin = new ArrayList<>();
        formaNormal = null;
        tableaux = null;

        calculoEficiente = null;
        calculoSinPerdidaDeInfo = null;

        paso1 = new ArrayList<>();
        paso2 = new ArrayList<>();
        paso3 = new ArrayList<>();

        claveCandidata = new ArrayList<>();
        esquema = new Esquemas();

    }

    //ATRIBUTOS
    public void agregarAtributos(String atributo) {

        if (!lAtributos.contains(atributo)) {
            lAtributos.add(atributo);
            hayCambios(Cambios.ATRIBUTOS);
        }

    }

    @SuppressWarnings("unchecked")
    public void modificarAtributo(String atributoViejo, String atributoNuevo) {

        if (lAtributos.contains(atributoViejo) && !lAtributos.contains((atributoNuevo))) {
            int pos = lAtributos.indexOf(atributoViejo);
            lAtributos.remove(atributoViejo);
            lAtributos.add(pos, atributoNuevo);
            hayCambios(Cambios.ATRIBUTOS);

            for(DependenciaFuncional df : (ArrayList<DependenciaFuncional>)lDependenciasFuncionales.clone())
            {
                if(df.getDeterminante().contains(atributoViejo) || df.getDeterminado().contains(atributoViejo))
                {
                   ArrayList<String> determinante = df.getDeterminante();
                   ArrayList<String> determinado = df.getDeterminado();

                   if(determinante.contains(atributoViejo) )
                   {
                       int indexDeterminante = determinante.indexOf(atributoViejo);
                       determinante.remove(atributoViejo);
                       determinante.add(indexDeterminante,atributoNuevo);
                   }
                   if(determinado.contains(atributoViejo))
                   {
                       int indexDeterminado = determinado.indexOf(atributoViejo);
                       determinado.remove(atributoViejo);
                       determinado.add(indexDeterminado,atributoNuevo);
                   }

                   modificarDependenciaFuncional(df,crearDependenciaFuncional(determinante,determinado));
                }
            }

            esquema.modificarEsquemaConAtributo(atributoViejo,atributoNuevo);
        }
    }

    public void eliminarAtributo(String atributo) {
        if (lAtributos.contains(atributo)) {
            lAtributos.remove(atributo);
            hayCambios(Cambios.ATRIBUTOS);
            ArrayList<DependenciaFuncional> eliminar = new ArrayList<>();

            for(DependenciaFuncional df : lDependenciasFuncionales) {

                ArrayList<String> determinante = df.getDeterminante();
                ArrayList<String> determinado = df.getDeterminado();
                if(determinante.contains(atributo)||determinado.contains(atributo))
                {
                    eliminar.add(df);
                }
            }
            lDependenciasFuncionales.removeAll(eliminar);

            esquema.eliminarEsquemaConAtributo(atributo);
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> darListadoAtributos() {
        //Retorna una copia de la lista
        return (ArrayList<String>) lAtributos.clone();
    }

    //DEPENDENCIAS FUNCIONAL

    public DependenciaFuncional crearDependenciaFuncional(ArrayList<String> determinate, ArrayList<String> determinado) {
        DependenciaFuncional df = null;

        if (determinate != null && determinado != null && !determinate.isEmpty() && !determinado.isEmpty()) {
            if (determinate.size() > 1) {
                if (determinado.size() > 1)
                    df = new DFCompleja(determinate, determinado);
                else
                    df = new DFDeterminanteComplejo(determinate, determinado.get(0));
            } else {
                if (determinado.size() > 1)
                    df = new DFDeterminadoComplejo(determinate.get(0), determinado);
                else
                    df = new DFSimple(determinate.get(0), determinado.get(0));
            }
        }
        return df;
    }

    public void agregarDependenciaFuncional(DependenciaFuncional dependenciaFuncional) {
        if (dependenciaFuncional != null && !lDependenciasFuncionales.contains(dependenciaFuncional)) {
            lDependenciasFuncionales.add(dependenciaFuncional);
            hayCambios(Cambios.DEPENDENCIAS);
        }
    }

    public void modificarDependenciaFuncional(DependenciaFuncional dependenciaFuncionalAntigua, DependenciaFuncional dependenciaFuncionalNueva) {
        if (lDependenciasFuncionales.contains(dependenciaFuncionalAntigua) && !lDependenciasFuncionales.contains(dependenciaFuncionalNueva)) {
            int pos = lDependenciasFuncionales.indexOf(dependenciaFuncionalAntigua);
            lDependenciasFuncionales.remove(dependenciaFuncionalAntigua);
            lDependenciasFuncionales.add(pos, dependenciaFuncionalNueva);
            hayCambios(Cambios.DEPENDENCIAS);
        }
    }

    public void eliminarDependenciaFuncional(DependenciaFuncional dependenciaFuncional) {
        if (lDependenciasFuncionales.contains(dependenciaFuncional)) {
            lDependenciasFuncionales.remove(dependenciaFuncional);
            hayCambios(Cambios.DEPENDENCIAS);
        }
    }

    public ArrayList<DependenciaFuncional> darListadoDependenciasFuncional() {
        //Retorno una copia de la lista de dependencias funcionales
        return (ArrayList<DependenciaFuncional>) lDependenciasFuncionales.clone();
    }

    //CLAVE CANDIDATAS
    public ArrayList<ArrayList<String>> calcularClavesCandidatas() {

        if( claves.size() == 0) { //NO SE CALCULO LA CLAVE
            ArrayList<String> lposibles;
            ArrayList<String> lprefijoClave = new ArrayList<String>(lAtributos);
            ArrayList<String> determinante = new ArrayList<String>();
            ArrayList<String> determinado = new ArrayList<String>();
            claves.clear();

            if (lDependenciasFuncionales.isEmpty()) {
                claves.add(lAtributos);
                return claves;
            }

            //Obtengo todos los Determinantes y todos los Determinados
            for (DependenciaFuncional df : lDependenciasFuncionales) {
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

            //Asigno por default la clave candidata como el primer valor de la lista de claves
            claveCandidata = (ArrayList<String>) claves.get(0).clone();
        }

        return (ArrayList<ArrayList<String>>) claves.clone();
    }

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

    public boolean calcularUniverso(ArrayList<String> clave) {

        if (!clave.isEmpty() && !lDependenciasFuncionales.isEmpty()) {
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
            return lClaves.containsAll(lAtributos);
        }
        return false;
    }

    public ArrayList<String> calcularClausura(ArrayList<String> AtributoACalcular) {

        if (lDependenciasFuncionales.isEmpty() || AtributoACalcular.isEmpty()) {
            return new ArrayList<String>();
        }

        ArrayList<String> lClausura = new ArrayList<String>();
        ArrayList<String> ldeterminante;
        ArrayList<String> ldeterminado;

        lClausura.addAll(AtributoACalcular);

        boolean tengoCambio = true;
        while (tengoCambio) {
            tengoCambio = false;
            for (DependenciaFuncional df : Administradora.getInstance().darListadoDependenciasFuncional()) {
                if (lClausura.containsAll(df.getDeterminante()) && !lClausura.containsAll(df.getDeterminado())) {
                    tengoCambio = true;
                    lClausura.addAll(df.getDeterminado());
                    //lClausura = new ArrayList<String>(new HashSet<String>(lClausura));
                }
            }
        }
        lClausura = new ArrayList<String>(new HashSet<String>(lClausura));
        return lClausura;
    }

    public ArrayList<String> calcularClausuraDependenciasLimitadas(ArrayList<String> AtributoACalcular, ArrayList<DependenciaFuncional> dependenciaFuncionales) {
        if (dependenciaFuncionales.isEmpty() || AtributoACalcular.isEmpty())
            return new ArrayList<String>();

        ArrayList<String> lClausura = new ArrayList<>();
        lClausura.addAll(AtributoACalcular);

        boolean hayCambios = true;
        while (hayCambios) {
            hayCambios = false;

            for (DependenciaFuncional df : dependenciaFuncionales) {
                if (lClausura.containsAll(df.getDeterminante()) && !lClausura.containsAll(df.getDeterminado())) {
                    hayCambios = true;
                    lClausura.addAll(df.getDeterminado());
                }
            }
        }

        lClausura = new ArrayList<String>(new HashSet<String>(lClausura));
        return lClausura;
    }

    public void cambiarClaveCandidata(ArrayList<String> claveCandidata) {
        if(claves.contains(claveCandidata) && !(claveCandidata.containsAll(this.claveCandidata) && claveCandidata.size() == this.claveCandidata.size()) ) {
            //this.claveCandidata.clear();
            hayCambios(Cambios.CLAVECANDIDATA);
            this.claveCandidata.addAll(claveCandidata);

        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> darClaveCandidataSeleccionada() {
        return (ArrayList<String>) claveCandidata.clone();
    }
    @SuppressWarnings("unchecked")
    public ArrayList<ArrayList<String>> darSuperClaves() {

        if(superClaves.isEmpty() && !lAtributos.isEmpty())
        {
            superClaves.addAll(calcularClavesCandidatas());

            for( ArrayList<String> clave :claves )
            {
                ArrayList<String> listadoAtributosRestantes = darListadoAtributos();
                listadoAtributosRestantes.removeAll(clave);
                calculoSuperClavesRecursivo((ArrayList<String>)clave.clone(),listadoAtributosRestantes);
            }

            Collections.sort(superClaves, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    return o1.size()-o2.size();
                }
            });
        }

        return superClaves;

    }

    @SuppressWarnings("unchecked")
    private void calculoSuperClavesRecursivo(ArrayList<String> lPrefijos,ArrayList<String> lAtributosPosibles)
    {
        if(lAtributosPosibles!=null && !lAtributosPosibles.isEmpty())
        {
            ArrayList<String> lAuxPrefijos = new ArrayList<String>((ArrayList<String>) lPrefijos.clone());
            ArrayList<String> lAuxAtributosPosibles = new ArrayList<String>((ArrayList<String>) lAtributosPosibles.clone());

            for(String atributoPosible : lAtributosPosibles)
            {
                lAuxAtributosPosibles.remove(atributoPosible);
                lAuxPrefijos.add(atributoPosible);
                Collections.sort(lAuxPrefijos);

                if(!superClaves.contains(lAuxPrefijos))
                    superClaves.add((ArrayList<String>) lAuxPrefijos.clone());
                calculoSuperClavesRecursivo(lAuxPrefijos,(ArrayList<String>) lAuxAtributosPosibles.clone());
                lAuxPrefijos.remove(atributoPosible);
            }

        }
    }

    //FORMA NORMAL
    public FormaNormal calcularFormaNormal() {

        if(formaNormal == null) {

            calcularFmin();
            calcularClavesCandidatas();

            if (lDependenciasFuncionales.isEmpty() || fmin.isEmpty() || lAtributos.size() <= 2) {
                formaNormal = new FNBC();
                return formaNormal;
            }

            ArrayList<String> clave = claveCandidata;

            int tam = lDependenciasFuncionales.size();
            int i = 0;
            FormaNormal fnAux;

            while (i < tam && (formaNormal == null || !formaNormal.soyPrimeraFN())) {
                fnAux = determinarFormaNormalDF(lDependenciasFuncionales.get(i), clave);
                //PERDÓN MUNDO POR LA SENTENCIA TAN FEA !!!!
                if (fnAux != null && (formaNormal == null ||
                        fnAux.soyPrimeraFN() || //SI fnAUX  esta en 1FN  No calculo mas y asigo a FN
                        (fnAux.soySegundaFN() && (formaNormal.soyFNBC() || formaNormal.soyTerceraFN())) || // Si FnAux es 2FN solo REEMPLAZO SI FN ESTA EN FNBC o 3FN
                        (fnAux.soyTerceraFN() && formaNormal.soyFNBC()))) //Si FNAux esta en 3FN SOLO LO REEMPLAZO SI FN ESTA EN FNBC
                    formaNormal = fnAux;
                i++;
            }
        }
        return formaNormal;
    }

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

    public boolean tieneDescomposicion3FN() {
        return !formaNormal.soyTerceraFN();
    }

    public boolean tieneDescomposicionFNBC() {
        return !formaNormal.soyFNBC();
    }

    public CalculoEficiente calculoEficiente3FN()
    {
        if(calculoEficiente == null)
            calculoEficiente = new CalculoEficiente(darListadoAtributos(),darListadoDependenciasFuncional(),darClaveCandidataSeleccionada());
        return  calculoEficiente;
    }

    public CalculoSinPerdidaDeInfo calculoSinPerdidaDeInfoFNBC()
    {
        if(calculoSinPerdidaDeInfo == null)
            calculoSinPerdidaDeInfo = new CalculoSinPerdidaDeInfo(darListadoAtributos(),darListadoDependenciasFuncional(),darClaveCandidataSeleccionada());
        return calculoSinPerdidaDeInfo;
    }

    public ArrayList<ArrayList<DependenciaFuncional>> calcularDescomposicionFNBC() {

        calcularFmin();
        calcularClavesCandidatas();

        int tam = fmin.size();
        int i = 0;
        ArrayList<ArrayList<DependenciaFuncional>> descomposicion = new ArrayList<ArrayList<DependenciaFuncional>>();
        ArrayList<DependenciaFuncional> ldfNOFNBC = new ArrayList<DependenciaFuncional>();
        ArrayList<String> atributos = new ArrayList<String>();

        ArrayList<String> clave = claves.get(0);

        /*
        for (DependenciaFuncional dfAux : fmin) {
            if (determinarFormaNormalDF(dfAux, clave).soyFNBC()) {
                ArrayList<DependenciaFuncional> aux = new ArrayList<DependenciaFuncional>();
                aux.add(dfAux);
                descomposicion.add(aux);
                atributos.addAll(dfAux.getDeterminante());
            } else {
                ldfNOFNBC.add(dfAux);
                atributos.addAll(dfAux.dameAtributos());
            }
        }
        */

        //Determino si hay alguna Dep Funcional que no cumpla con la FNBC
        for (DependenciaFuncional dfAux : fmin) {
            atributos.addAll(dfAux.dameAtributos());
            if (!determinarFormaNormalDF(dfAux, clave).soyFNBC()) {
                ldfNOFNBC.add(dfAux);
            }
        }
        if (ldfNOFNBC.isEmpty()) {
            atributos.clear();
            descomposicion.add(fmin);
        } else {
            ldfNOFNBC.clear();
            ldfNOFNBC.addAll(fmin);
        }
        //Dejo una lista de atributos No repetidos
        atributos = new ArrayList<String>(new HashSet<String>(atributos));

        //Tengo una copia de la lista
        ArrayList<DependenciaFuncional> ldf = new ArrayList<>();
        ldf.addAll(ldfNOFNBC);

        boolean cambios = true;
        //R en R1 = X U A   y  R2  = X - A
        while (cambios && !ldf.isEmpty()) {
            cambios = false;
            //Lista Atributo
            ArrayList<DependenciaFuncional> lAuxiliar = new ArrayList<>();
            ArrayList<DependenciaFuncional> lArecorrer = new ArrayList<>(ldf);

            for (DependenciaFuncional dependenciaFuncional : lArecorrer) {

                ArrayList<String> determinante = dependenciaFuncional.getDeterminante();
                ArrayList<String> determinado = dependenciaFuncional.getDeterminado();

                if (atributos.containsAll(determinante) && atributos.containsAll(determinado)) {
                    cambios = true;
                    atributos.removeAll(determinado);
                    lAuxiliar.add(dependenciaFuncional);
                    ldf.remove(dependenciaFuncional);
                    descomposicion.add(lAuxiliar);
                }
            }
        }
        //Si tengo Atributos sueltos tengo que crear una dependencia funcional
        if (!lAtributos.isEmpty()) {
            ArrayList<DependenciaFuncional> dependencias = new ArrayList<>();
            dependencias.add((atributos.size() == 1) ? new DFSimple(atributos.get(0), "") : new DFDeterminanteComplejo(atributos, ""));//Depende si es determinante es  simple o compleja
            descomposicion.add(dependencias);
        }

        return descomposicion;
    }

    //FMIN
    public ArrayList<DependenciaFuncional> calcularFmin() {

        if(fmin.size() == 0) {

            ArrayList<DependenciaFuncional> dependenciaFuncional = new ArrayList<>();
            ArrayList<DependenciaFuncional> dfEliminarRedundantes = new ArrayList<>();
            ArrayList<DependenciaFuncional> dfSinRedundanciaIzq = new ArrayList<>();

            paso1.clear();
            paso2.clear();
            paso3.clear();

            //Elimino redundancia del determinado
            for (DependenciaFuncional df : lDependenciasFuncionales) {
                dependenciaFuncional.addAll(df.convertirAFmin());
            }

            dfEliminarRedundantes = (ArrayList<DependenciaFuncional>) dependenciaFuncional.clone();
            paso1.addAll((ArrayList<DependenciaFuncional>) dependenciaFuncional.clone());

            //Elimino redundancia del determinante
            for (DependenciaFuncional df : dependenciaFuncional) {
                if (df.soyDeterminanteComplejo()) {
                    int index = dfEliminarRedundantes.indexOf(df);
                    dfEliminarRedundantes.remove(index);
                    DependenciaFuncional guardar = darDFSinRedundanteIZQ(df, dependenciaFuncional);//dfEliminarRedundantes);
                    if (!guardar.equals(df)) {
                        dfEliminarRedundantes.add(index, guardar);
                    } else {
                        dfEliminarRedundantes.add(index, df);
                    }
                    dfSinRedundanciaIzq.add(guardar);
                } else
                    dfSinRedundanciaIzq.add(df);
            }

            dfEliminarRedundantes = (ArrayList<DependenciaFuncional>) dfSinRedundanciaIzq.clone();

            paso2.addAll((ArrayList<DependenciaFuncional>) dfSinRedundanciaIzq.clone());

            //dfSinRedundanciaIzq = new ArrayList<DependenciaFuncional>(new HashSet<DependenciaFuncional>(dfSinRedundanciaIzq));

            for (DependenciaFuncional df : dfSinRedundanciaIzq) {
                if (df != null && !fmin.contains(df) && !soyDFRedundante(df, dfEliminarRedundantes)) {
                    fmin.add(df);
                } else
                    dfEliminarRedundantes.remove(df);
            }

        }
        paso3.addAll(fmin);
        return fmin;
    }


    public ArrayList<DependenciaFuncional> getPaso1() {
        return paso1;
    }

    public ArrayList<DependenciaFuncional> getPaso2() {
        return paso2;
    }

    public ArrayList<DependenciaFuncional> getPaso3() {
        return paso3;
    }

    private boolean soyDFRedundante(final DependenciaFuncional dependenciaFuncional, final ArrayList<DependenciaFuncional> lDependenciasFuncionales) {
        if (dependenciaFuncional.getDeterminante().containsAll(dependenciaFuncional.getDeterminado()))
            return true;

        ArrayList<String> ClausuraConDF = calcularClausuraDependenciasLimitadas(dependenciaFuncional.getDeterminante(), lDependenciasFuncionales);
        ArrayList<DependenciaFuncional> lSINDependeciaFuncional = (ArrayList<DependenciaFuncional>) lDependenciasFuncionales.clone();
        lSINDependeciaFuncional.remove(dependenciaFuncional);
        ArrayList<String> ClausuraSinDF = calcularClausuraDependenciasLimitadas(dependenciaFuncional.getDeterminante(), lSINDependeciaFuncional);

        return ClausuraSinDF.containsAll(ClausuraConDF);
    }

    private DependenciaFuncional darDFSinRedundanteIZQ(DependenciaFuncional original, ArrayList<DependenciaFuncional> lDependenciasFuncionales) {
        ArrayList<String> Prefijo = new ArrayList<>();
        String Determinado = original.getDeterminado().get(0);

        ArrayList<String> Determinante = calcularRedundanciaDeterminante(Prefijo, original.getDeterminante(), Determinado, lDependenciasFuncionales);

        if (!Determinante.isEmpty() && !Determinante.equals(original.getDeterminante())) {
            DependenciaFuncional df;

            if (Determinante.size() > 1)
                df = new DFDeterminanteComplejo(Determinante, Determinado);
            else
                df = new DFSimple(Determinante.get(0), Determinado);

            return df;
        }
        return original;
    }

    private ArrayList<String> calcularRedundanciaDeterminante(final ArrayList<String> Prefijo, final ArrayList<String> Determinante, final String Determinado, ArrayList<DependenciaFuncional> lDependenciasFuncionales) {
        ArrayList<String> resultado = new ArrayList<>();
        if (Determinante != null) {
            ArrayList<String> copiaDeterminante = new ArrayList<String>(Determinante);
            ArrayList<String> otroResultado = new ArrayList<>();

            for (String atributo : Determinante) {
                Prefijo.add(atributo);
                copiaDeterminante.remove(atributo);
                if (calcularClausuraDependenciasLimitadas(Prefijo, lDependenciasFuncionales).contains(Determinado) &&
                        (resultado.isEmpty() || resultado.size() > Prefijo.size())) {
                    resultado = (ArrayList<String>) Prefijo.clone();
                }

                otroResultado = calcularRedundanciaDeterminante(Prefijo, copiaDeterminante, Determinado, lDependenciasFuncionales);

                if (resultado.isEmpty() || !otroResultado.isEmpty() && otroResultado.size() < resultado.size())
                    resultado = otroResultado;

                Prefijo.remove(atributo);
            }
        }

        return resultado;
    }

    //Esquemas
    public void agregarEsquema(final ArrayList<String> Esquema){
        esquema.agregarEsquema(Esquema);
        hayCambios(Cambios.ESQUEMA);
    }

    public void modificarEsquema(final ArrayList<String> EsquemaAnterior,final ArrayList<String> EsquemaNuevo){
        esquema.modificarEsquema(EsquemaAnterior,EsquemaNuevo);
        hayCambios(Cambios.ESQUEMA);
    }

    public void eliminarEsquema(final ArrayList<String> EsquemaAEliminar){
        esquema.eliminarEsquema(EsquemaAEliminar);
        hayCambios(Cambios.ESQUEMA);
    }

    public ArrayList<ArrayList<String>> darEsquemas(){ return esquema.getEsquemas() ;}

    public Esquemas darEsquema(){return esquema; }

    //TABLEAUX
    public Tableaux calcularTableaux(){

        if(tableaux == null && esquema != null) {
            calcularFmin();
            calcularClavesCandidatas();
            tableaux = new Tableaux(esquema, lAtributos, fmin);
        }
        return tableaux;
    }

    public boolean hayPerdidaDeInformacion(){
        if(tableaux != null)
            return tableaux.tengoPerdidaDeInformacion();

        return true;
    }

    public PasoTableaux darPasoTableaux(int index){
        if(tableaux != null)
            return tableaux.getPaso(index);
        else
            return null;
    }

    public PasoTableaux darUltimoPaso(){
        if(tableaux != null)
            return tableaux.ultimoPaso();
        else
            return null;
    }

    public PasoTableaux darPrimerPaso(){
        if(tableaux != null)
            return tableaux.primerPaso();
        else
            return null;
    }

    public int darCantidadPasos(){
        if(tableaux!=null)
            return tableaux.cantidadDePasos();
        else
            return 0;
    }

    //OTRAS OPCIONES
    public void hayCambios(Cambios cambio) {
        switch (cambio)
        {
            default:
            case ATRIBUTOS:
            case DEPENDENCIAS: claves.clear();superClaves.clear();
            case CLAVECANDIDATA:formaNormal= null;fmin.clear();claveCandidata.clear();calculoEficiente = null;calculoSinPerdidaDeInfo = null;
            case ESQUEMA: tableaux = null;
        }
    }

    public enum Cambios{ATRIBUTOS, DEPENDENCIAS,CLAVECANDIDATA,ESQUEMA}

    private void clean() {
        lAtributos.clear();
        lDependenciasFuncionales.clear();
        claves.clear();
        superClaves.clear();
        fmin.clear();
        formaNormal = null;
        tableaux = null;
        esquema = new Esquemas();
        claveCandidata.clear();
    }

    //EJEMPLOS
    public void ejemploTableaux() {
        clean();
        agregarAtributos("a");
        agregarAtributos("b");
        agregarAtributos("c");
        agregarAtributos("d");
        agregarAtributos("e");

        agregarDependenciaFuncional(new DFSimple("a", "c"));
        agregarDependenciaFuncional(new DFSimple("b", "c"));
        agregarDependenciaFuncional(new DFSimple("c", "d"));

        ArrayList<String> determinante = new ArrayList<String>();
        determinante.add("d");
        determinante.add("e");

        agregarDependenciaFuncional(new DFDeterminanteComplejo((ArrayList<String>) determinante.clone(), "c"));
        determinante.clear();
        determinante.add("c");
        determinante.add("e");
        agregarDependenciaFuncional(new DFDeterminanteComplejo(determinante, "a"));

        ArrayList<String>esquema = new ArrayList<>();
        esquema.add("a");
        esquema.add("d");
        agregarEsquema(esquema);

        esquema = new ArrayList<>();
        esquema.add("a");
        esquema.add("b");
        agregarEsquema(esquema);

        esquema = new ArrayList<>();
        esquema.add("b");
        esquema.add("e");
        agregarEsquema(esquema);

        esquema = new ArrayList<>();
        esquema.add("c");
        esquema.add("d");
        esquema.add("e");
        agregarEsquema(esquema);

        esquema = new ArrayList<>();
        esquema.add("a");
        esquema.add("e");
        agregarEsquema(esquema);
    }

    @SuppressWarnings("unchecked")
    public void ejemploConPerdidaDeInformacion()
    {
        clean();
        agregarAtributos("Ciudad");
        agregarAtributos("Calle");
        agregarAtributos("CP");


        ArrayList<String> determinante = new ArrayList<String>();
        determinante.add("Ciudad");
        determinante.add("Calle");


        agregarDependenciaFuncional(new DFDeterminanteComplejo((ArrayList<String>) determinante.clone(), "CP"));
        agregarDependenciaFuncional(new DFSimple("CP", "Ciudad"));



        ArrayList<String>esquema = new ArrayList<>();
        esquema.add("Ciudad");
        esquema.add("Calle");
        agregarEsquema(esquema);

        esquema = new ArrayList<>();
        esquema.add("Calle");
        esquema.add("CP");
        agregarEsquema(esquema);

    }

    @SuppressWarnings("unchecked")
    public void ejemploClaveCandidata()
    {
        clean();
        agregarAtributos("a");
        agregarAtributos("b");
        agregarAtributos("c");
        agregarAtributos("d");
        agregarAtributos("e");

        ArrayList<String> Determinante = new ArrayList<>();
        ArrayList<String> Determinado = new ArrayList<>();

        agregarDependenciaFuncional(new DFSimple("a","b"));
        agregarDependenciaFuncional(new DFSimple("a","c"));

        Determinante.add("b");
        Determinante.add("c");

        agregarDependenciaFuncional(new DFDeterminanteComplejo((ArrayList<String>) Determinante.clone(),"a"));
        Determinante.add("d");

        agregarDependenciaFuncional(new DFDeterminanteComplejo((ArrayList<String>) Determinante.clone(),"e"));

        agregarDependenciaFuncional(new DFSimple("e","c"));

        ArrayList<String> esquema= new ArrayList<>();
        esquema.add("a");
        esquema.add("b");
        esquema.add("c");
        esquema.add("d");
        esquema.add("e");
        agregarEsquema(esquema);
    }

    @SuppressWarnings("unchecked")
    public void ejemploFMINTeoria()
    {
        clean();
        agregarAtributos("a");
        agregarAtributos("b");
        agregarAtributos("c");
        agregarAtributos("d");
        agregarAtributos("e");

        ArrayList<String> Determinante = new ArrayList<>();
        ArrayList<String> Determinado = new ArrayList<>();

        Determinado.add("b");
        Determinado.add("c");
        Determinado.add("d");
        agregarDependenciaFuncional(new DFDeterminadoComplejo("a",(ArrayList<String>) Determinado.clone()));

        Determinante.add("a");
        Determinante.add("b");
        Determinado.clear();

        Determinado.add("d");
        Determinado.add("e");
        agregarDependenciaFuncional(new DFCompleja((ArrayList<String>) Determinante.clone(),(ArrayList<String>) Determinado.clone()));

        Determinado.clear();
        Determinante.clear();

        Determinante.add("b");
        Determinante.add("e");
        Determinado.add("a");
        Determinado.add("c");

        agregarDependenciaFuncional(new DFCompleja((ArrayList<String>) Determinante.clone(),(ArrayList<String>) Determinado.clone()));


        ArrayList<String> esquema= new ArrayList<>();
        esquema.add("a");
        esquema.add("b");
        esquema.add("c");
        esquema.add("d");
        esquema.add("e");
        agregarEsquema(esquema);
    }

    @SuppressWarnings("unchecked")
    public void ejemploCCyFMIN() {
        clean();
        agregarAtributos("a");
        agregarAtributos("b");
        agregarAtributos("c");
        agregarAtributos("d");
        agregarAtributos("e");
        agregarAtributos("f");
        agregarAtributos("g");
        ArrayList<String> Determinante = new ArrayList<>();
        ArrayList<String> Determinado = new ArrayList<>();

        //1ra Dependencia Funcional
        Determinante.add("a");
        Determinante.add("b");
        Determinado.add("c");
        Determinado.add("a");
        agregarDependenciaFuncional(new DFCompleja((ArrayList<String>) Determinante.clone(), (ArrayList<String>) Determinado.clone()));
        Determinante.clear();
        Determinado.clear();

        //2da DF
        agregarDependenciaFuncional(new DFSimple("c", "a"));

        //3ra DF
        Determinante.add("d");
        Determinante.add("e");
        agregarDependenciaFuncional(new DFDeterminanteComplejo((ArrayList<String>) Determinante.clone(), "f"));
        Determinante.clear();

        //4ta DF
        Determinado.add("d");
        Determinado.add("f");
        agregarDependenciaFuncional(new DFDeterminadoComplejo("e", (ArrayList<String>) Determinado.clone()));
        Determinado.clear();

        //5ta DF
        Determinado.add("d");
        Determinado.add("e");
        agregarDependenciaFuncional(new DFDeterminadoComplejo("f", (ArrayList<String>) Determinado.clone()));
        Determinado.clear();

        ArrayList<String>esquema = new ArrayList<>();
        esquema.add("a");
        esquema.add("b");
        agregarEsquema(esquema);

        esquema = new ArrayList<>();
        esquema.add("c");
        esquema.add("d");
        agregarEsquema(esquema);

        esquema = new ArrayList<>();
        esquema.add("e");
        esquema.add("f");
        agregarEsquema(esquema);

    }

    @SuppressWarnings("unchecked")
    public void ejemploPrimerEjercicio(){
        clean();
        agregarAtributos("a");
        agregarAtributos("b");
        agregarAtributos("c");
        agregarAtributos("d");
        agregarAtributos("e");

        ArrayList<String> Determinante = new ArrayList<>();
        ArrayList<String> Determinado = new ArrayList<>();

        //1ra Dependencia Funcional
        Determinado.add("b");
        Determinado.add("c");
        Determinado.add("d");
        agregarDependenciaFuncional(new DFDeterminadoComplejo("a", (ArrayList<String>) Determinado.clone()));
        Determinante.clear();
        Determinado.clear();


        //2da DF
        Determinante.add("a");
        Determinante.add("b");
        Determinado.add("d");
        Determinado.add("e");

        agregarDependenciaFuncional(new DFCompleja((ArrayList<String>) Determinante.clone(), (ArrayList<String>)Determinado.clone()));
        Determinante.clear();
        Determinado.clear();

        //3ra DF
        Determinante.add("b");
        Determinante.add("e");
        Determinado.add("a");
        Determinado.add("c");

        agregarDependenciaFuncional(new DFCompleja((ArrayList<String>) Determinante.clone(), (ArrayList<String>)Determinado.clone()));
        Determinante.clear();
        Determinado.clear();

    }




    /*
    public void guardarArchivo(String nombreArchivo, String directorio, Context context) {
        JSONObject job = new JSONObject();

        JSONArray lAtributo = new JSONArray();
        for (String la : lAtributos) {
            lAtributo.put(la);
        }

        try {
            job.put("lAtributos", lAtributo);

            JSONArray lDependenciaFuncional = new JSONArray();

            for (DependenciaFuncional df : lDependenciasFuncionales) {
                JSONObject depFuncional = new JSONObject();
                JSONArray determinante = new JSONArray();

                for (String s : df.getDeterminante()) {
                    determinante.put(s);
                }

                JSONArray determinado = new JSONArray();
                for (String s : df.getDeterminado()) {
                    determinado.put(s);
                }

                depFuncional.put("determinantes", determinante);
                depFuncional.put("determinados", determinado);
                lDependenciaFuncional.put(depFuncional);
            }

            GestorArchivos.guardarArchivo(job, nombreArchivo, directorio, context);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void cargarArchivo(String nombreArchivo, String directorio, Context context) {

        Runtime garbage = Runtime.getRuntime();
        garbage.gc();

        lAtributos = new ArrayList<String>();
        lDependenciasFuncionales = new ArrayList<DependenciaFuncional>();
        claves = new ArrayList<ArrayList<String>>();
        fmin = new ArrayList<DependenciaFuncional>();
        formaNormal = null;
        tableauxHayPerdidaDeInformacion = true;
        cambiosTableaux = new ArrayList<ArrayList<ArrayList<Integer>>>();

        try {
            JSONObject job = new JSONObject(GestorArchivos.cargarArchivo(nombreArchivo, directorio, context));
            JSONArray jsonArray = job.getJSONArray("lAtributos");
            for (int i = 0; i < jsonArray.length(); i++) {
                lAtributos.add(jsonArray.get(i).toString());
            }

            jsonArray = job.getJSONArray("lDependenciaFuncional");
            for (int i = 0; i < jsonArray.length(); i++) {

                DependenciaFuncional df;
                ArrayList<String> determinantes = new ArrayList<>();
                ArrayList<String> determinado = new ArrayList<>();

                JSONObject aux = jsonArray.getJSONObject(i);

                JSONArray jsonDeterminantes = aux.getJSONArray("determinantes");
                for (int j = 0; j < jsonDeterminantes.length(); j++) {
                    determinantes.add(jsonDeterminantes.get(j).toString());
                }


                JSONArray jsonDeterminado = aux.getJSONArray("determinados");
                for (int j = 0; j < jsonDeterminado.length(); j++) {
                    determinado.add(jsonDeterminado.get(j).toString());
                }

                if (jsonDeterminantes.length() > 1) {
                    if (jsonDeterminado.length() > 1)
                        df = new DFCompleja(determinantes, determinado);
                    else
                        df = new DFDeterminanteComplejo(determinantes, determinado.get(0));
                } else {
                    if (jsonDeterminado.length() > 1)
                        df = new DFDeterminadoComplejo(determinantes.get(0), determinado);
                    else
                        df = new DFSimple(determinantes.get(0), determinado.get(0));
                }

                lDependenciasFuncionales.add(df);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }*/

}
