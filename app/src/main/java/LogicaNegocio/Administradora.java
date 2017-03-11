package LogicaNegocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Administradora implements Serializable {

    private static Administradora ourInstance = new Administradora();

    public static Administradora getInstance() {
        return ourInstance;
    }

    private ArrayList<String> lAtributos;
    private ArrayList<DependenciaFuncional> lDependenciasFuncionales;
    private ArrayList<ArrayList<String>> claves;
    private ArrayList<DependenciaFuncional> fmin;
    private ArrayList<String[][]> tableaux;
    private ArrayList<ArrayList<ArrayList<Integer>>> cambiosTableaux;
    private FormaNormal formaNormal;
    private boolean tableauxHayPerdidaDeInformacion;

    private Administradora() {

        Runtime garbage = Runtime.getRuntime();
        garbage.gc();

        lAtributos = new ArrayList<String>();
        lDependenciasFuncionales = new ArrayList<DependenciaFuncional>();
        claves = new ArrayList<ArrayList<String>>();
        fmin = new ArrayList<DependenciaFuncional>();
        formaNormal = null;
        tableauxHayPerdidaDeInformacion = true;
        cambiosTableaux = new ArrayList<ArrayList<ArrayList<Integer>>>();
    }

    //ATRIBUTOS
    public void agregarAtributos(String atributo) {

        if (!lAtributos.contains(atributo)) {
            lAtributos.add(atributo);
        }

    }

    public void modificarAtributo(String atributoViejo, String atributoNuevo) {

        if (lAtributos.contains(atributoViejo) && !lAtributos.contains((atributoNuevo))) {
            int pos = lAtributos.indexOf(atributoViejo);
            lAtributos.remove(atributoViejo);
            lAtributos.add(pos, atributoNuevo);
        }
    }

    public void eliminarAtributo(String atributo) {
        if (lAtributos.contains(atributo)) {
            lAtributos.remove(atributo);
        }
    }

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
        }
    }

    public void modificarDependenciaFuncional(DependenciaFuncional dependenciaFuncionalAntigua, DependenciaFuncional dependenciaFuncionalNueva) {
        if (lDependenciasFuncionales.contains(dependenciaFuncionalAntigua) && !lDependenciasFuncionales.contains(dependenciaFuncionalNueva)) {
            int pos = lDependenciasFuncionales.indexOf(dependenciaFuncionalAntigua);
            lDependenciasFuncionales.remove(dependenciaFuncionalAntigua);
            lDependenciasFuncionales.add(pos, dependenciaFuncionalNueva);
        }
    }

    public void eliminarDependenciaFuncional(DependenciaFuncional dependenciaFuncional) {
        if (lDependenciasFuncionales.contains(dependenciaFuncional)) {
            lDependenciasFuncionales.remove(dependenciaFuncional);
        }
    }

    public ArrayList<DependenciaFuncional> darListadoDependenciasFuncional() {
        //Retorno una copia de la lista de dependencias funcionales
        return (ArrayList<DependenciaFuncional>) lDependenciasFuncionales.clone();
    }

    //CLAVE CANDIDATAS
    public ArrayList<ArrayList<String>> calcularClavesCandidatas() {

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
                    claves.remove(string);
            }
        }

        return claves;
    }

    private void calcularClavesRecursivo(ArrayList<String> lPrefijos, ArrayList<String> lAtributosPosibles) {

        if (lAtributosPosibles != null && !lAtributosPosibles.isEmpty()) {
            ArrayList<String> lAuxPrefijos = new ArrayList<String>((ArrayList<String>) lPrefijos.clone());
            ArrayList<String> lAuxAtributosPosibles = new ArrayList<String>((ArrayList<String>) lAtributosPosibles.clone());

            for (String atributoPosible : lAtributosPosibles) {

                lAuxAtributosPosibles.remove(atributoPosible);
                lAuxPrefijos.add(atributoPosible);

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
        //TODO AL CAMBIAR TODO LO POSTERIOR DEBE CAMBIAR
    }

    public ArrayList<ArrayList<String>> darSuperClaves() {
        return claves;
    }

    //FORMA NORMAL
    public FormaNormal calcularFormaNormal() {

        if (lDependenciasFuncionales.isEmpty() || fmin.isEmpty() || lAtributos.size() <= 2) {
            formaNormal = new FNBC();
            return formaNormal;
        }

        ArrayList<String> clave = claves.get(0);

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

    public ArrayList<ArrayList<DependenciaFuncional>> calcularDescomposicion3FN() {
        if (fmin == null || claves == null || claves.isEmpty())
            return null;

        ArrayList<ArrayList<DependenciaFuncional>> descomposicion = new ArrayList<ArrayList<DependenciaFuncional>>();
        ArrayList<String> clave = claves.get(0);

        int tam = fmin.size();
        int i = 0;
        int tamDescomposicion = descomposicion.size();
        int x = 0;
        boolean TengoLaCLave = false;
        while (i < tam) {

            DependenciaFuncional dfAux = fmin.get(i);
            if (dfAux.getDeterminante().containsAll(clave))
                TengoLaCLave = true;
            x = 0;
            while (x < tamDescomposicion && !(descomposicion.get(x)).get(0).getDeterminante().containsAll(dfAux.getDeterminante())) {
                x++;
            }
            if (x < tamDescomposicion) {
                descomposicion.get(x).add(dfAux);
            } else {
                ArrayList<DependenciaFuncional> lDFAux = new ArrayList<DependenciaFuncional>();
                lDFAux.add(dfAux);
                descomposicion.add(lDFAux);
            }
            i++;
        }

        if (!TengoLaCLave) {
            ArrayList<DependenciaFuncional> lDFAux = new ArrayList<DependenciaFuncional>();
            DependenciaFuncional DFAux;
            if (clave.size() > 1)
                DFAux = new DFDeterminanteComplejo(clave, "");
            else
                DFAux = new DFSimple(clave.get(0), "");

            lDFAux.add(DFAux);
            descomposicion.add(lDFAux);
        }

        return descomposicion;
    }

    public ArrayList<ArrayList<DependenciaFuncional>> calcularDescomposicionFNBC() {

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

        fmin.clear();

        ArrayList<DependenciaFuncional> dependenciaFuncional = new ArrayList<>();
        ArrayList<DependenciaFuncional> dfEliminarRedundantes = new ArrayList<>();
        ArrayList<DependenciaFuncional> dfSinRedundanciaIzq = new ArrayList<>();


        //Elimino redundancia del determinado
        for (DependenciaFuncional df : lDependenciasFuncionales) {
            dependenciaFuncional.addAll(df.convertirAFmin());
        }

        dfEliminarRedundantes = (ArrayList<DependenciaFuncional>) dependenciaFuncional.clone();

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

        //dfSinRedundanciaIzq = new ArrayList<DependenciaFuncional>(new HashSet<DependenciaFuncional>(dfSinRedundanciaIzq));

        for (DependenciaFuncional df : dfSinRedundanciaIzq) {
            if (df != null && !fmin.contains(df) && !soyDFRedundante(df, dfEliminarRedundantes)) {
                fmin.add(df);
            } else
                dfEliminarRedundantes.remove(df);
        }


        return fmin;
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

    //TABLEAUX
    public ArrayList<String[][]> calcularTableaux(ArrayList<ArrayList<String>> subEsquemas) {

        if (tableaux != null && !hayCambios()) {
            return tableaux;
        }
        tableaux = new ArrayList<String[][]>();

        ArrayList<ArrayList<Integer>> cambiosxDf;

        cambiosTableaux.clear();

        boolean lineaCompleta = false;
        boolean hayCambios = true;

        int tamSubEsquema = subEsquemas.size();
        int tamAtributos = lAtributos.size();
        int indexAtributo = 0;
        int indexEsquema = 0;

        HashMap<String, Integer> posicionAtributo = new HashMap<String, Integer>();
        HashMap<String, ArrayList<Integer>> lugaresAxAtributo = new HashMap<String, ArrayList<Integer>>();

        int[] cantidadAxFila = new int[tamAtributos];

        String[][] tabla = new String[tamSubEsquema][tamAtributos];
        //---------------Inicializo el Hash de posicion de atibutos
        for (int i = 0; i < tamAtributos; i++) {
            posicionAtributo.put(lAtributos.get(i), i);
            lugaresAxAtributo.put(lAtributos.get(i), new ArrayList<Integer>());
        }

        //---------------------Cargo la primera tabla de Tableaux-----------------
        for (ArrayList<String> se : subEsquemas) {

            for (indexAtributo = 0; indexAtributo < tamAtributos; indexAtributo++) {
                //Si el subEsquema contiene un atributo se carga con A
                if (se.contains(lAtributos.get(indexAtributo))) {
                    tabla[indexEsquema][indexAtributo] = new String(" A" + (indexAtributo + 1) + " ");
                    cantidadAxFila[indexEsquema] += 1;
                    //Verifico si tengo una Fila Compuestas Por A
                    if (cantidadAxFila[indexEsquema] == tamSubEsquema)
                        lineaCompleta = true;
                } else
                    tabla[indexEsquema][indexAtributo] = new String("b" + (indexEsquema + 1) + "," + (indexAtributo + 1));

            }
            indexEsquema++;
        }


        //Cargo la primera tabla Al ArrayContenedor de Tablas
        tableaux.add(tabla.clone());


        //---------------------Compruebo por dependecia funcional-------------------
        while (hayCambios && !lineaCompleta) {
            hayCambios = false;

            for (DependenciaFuncional df : fmin) {
                //HashMap<AtributoDeterminante,valorDeterminado> valoresReemplazo

                cambiosxDf = new ArrayList<ArrayList<Integer>>();
                ArrayList<Integer> FilaXColumaCambios = new ArrayList<Integer>();
                String determinadoDeDF = df.getDeterminado().get(0);
                Integer indexPosicion = posicionAtributo.get(determinadoDeDF);


                HashMap<String, String> valoresReemplazo = new HashMap<String, String>();

                ArrayList<Integer> posAtributo = new ArrayList<>();

                for (String atributoDeterminante : df.getDeterminante()) {
                    posAtributo.add(posicionAtributo.get(atributoDeterminante));
                }

                for (indexEsquema = 0; indexEsquema < tamSubEsquema; indexEsquema++) {

                    int ints = 0;
                    int tamposAtributo = posAtributo.size();

                    String valorAux = tabla[indexEsquema][posAtributo.get(ints)];//OBTENGO EL ATRIBUTO

                    String valorDeterminado = tabla[indexEsquema][indexPosicion];

                    while (ints < tamposAtributo && tabla[indexEsquema][posAtributo.get(ints)].contains("A")) {
                        ints++;
                    }

                    //Si no existe el valor para cambiar ,
                    if (!valoresReemplazo.containsKey(valorAux)) {
                        valoresReemplazo.put(valorAux, valorDeterminado);
                    }

                    if (ints == tamposAtributo) {
                        //T.odo mi Determinante esta compuesto por A
                        if (valorDeterminado.contains("A")) {    //Hubo un cambio
                            if (valoresReemplazo.get(valorAux).contains("b")) {
                                //Guardo el valor para cambios POSTERIORES
                                valoresReemplazo.put(valorAux, valorDeterminado);

                                //Verifico los anteriores
                                for (int i = 0; i < (indexEsquema); i++) {

                                    //Reviso si los Determinantes estan compuestos por A
                                    ints = 0;
                                    while (ints < tamposAtributo && tabla[i][posAtributo.get(ints)].contains("A")) {
                                        ints++;
                                    }

                                    if (ints == tamposAtributo) {    //TODOS MIS DETERMINANTES TIENE A ;

                                        //Verifico Cambios
                                        if (tabla[i][indexPosicion].contains("b") &&
                                                tabla[i][indexPosicion].equals(valorDeterminado)) {
                                            FilaXColumaCambios.add(indexPosicion);//Fila
                                            FilaXColumaCambios.add(i);//Columna
                                            //cambiosxDf.add(FilaXColumaCambios);
                                            if (valorDeterminado.contains("A")) {
                                                cantidadAxFila[i] = cantidadAxFila[i] + 1;
                                            }
                                            hayCambios = true;
                                        }
                                        //Reemplazo el valor
                                        tabla[i][indexPosicion] = valorDeterminado;
                                    }
                                }
                            }
                        }
                    }

                    String valorAnterior = tabla[indexEsquema][indexPosicion];
                    String valorCambio = valoresReemplazo.get(valorAux);
                    //Verifico Cambios
                    if (valorAnterior.contains("b") && !valorAnterior.equals(valorCambio)) {
                        if (valorCambio.contains("A")) {
                            cantidadAxFila[indexEsquema] = cantidadAxFila[indexEsquema] + 1;
                        }
                        hayCambios = true;
                        FilaXColumaCambios.add(indexPosicion);//Fila
                        FilaXColumaCambios.add(indexEsquema);//Columna

                        tabla[indexEsquema][indexPosicion] = valorCambio;
                    }

                }//Cierra Esquemas
                tableaux.add(tabla.clone());
                //Compruebo si tengo una fila t.odo con A
                int indexFila = 0;

                while (indexFila < tamSubEsquema && !lineaCompleta) {
                    if (cantidadAxFila[indexFila] == tamAtributos) {
                        tableauxHayPerdidaDeInformacion = false;
                        lineaCompleta = true;
                    }
                    indexFila++;
                }
                cambiosxDf.add(FilaXColumaCambios);
                cambiosTableaux.add(cambiosxDf);
            }//Cierra el For DE DEPENDENCIAS FUNCIONALES
        }
        return tableaux;
    }

    public boolean isTableauxHayPerdidaDeInformacion() {
        return tableauxHayPerdidaDeInformacion;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> darCambiosTableaux() {
        //POR CADA CAMBIO EN EL TABLAUX      EN CADA DF         EL PRIMER DATO FILA en el 2do COLUMNA
        //ARRAYLIST<                        ARRAYLIST<          ArrayList<Integer>>>
        ArrayList<ArrayList<ArrayList<Integer>>> cambios = new ArrayList<ArrayList<ArrayList<Integer>>>();
        if (!cambiosTableaux.isEmpty())
            cambios.addAll(cambiosTableaux);
        return cambios;
    }


    //OTRAS OPCIONES
    public boolean hayCambios() {
        return false;//TODO IMPLEMENTAR CAMBIOS
    }

    private void clean() {
        lAtributos.clear();
        lDependenciasFuncionales.clear();
        cambiosTableaux.clear();
        fmin.clear();
        formaNormal = null;
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

        //TODO AGREGAR SUB ESQUEMAS

    }

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

    }

    public void ejemploPrimerEjercicio()
    {
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
