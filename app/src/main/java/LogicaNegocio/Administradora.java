package LogicaNegocio;

import java.io.Serializable;
import java.util.ArrayList;
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
    private FormaNormal formaNormal;


    //----------------------------------------------
    private Tableaux tableaux;
    private Esquemas esquema;
    private ArrayList<String> claveCandidata;

    private Administradora() {

        Runtime garbage = Runtime.getRuntime();
        garbage.gc();

        lAtributos = new ArrayList<String>();
        lDependenciasFuncionales = new ArrayList<DependenciaFuncional>();
        claves = new ArrayList<ArrayList<String>>();
        fmin = new ArrayList<DependenciaFuncional>();
        formaNormal = null;
        tableaux = null;
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

    public void modificarAtributo(String atributoViejo, String atributoNuevo) {

        if (lAtributos.contains(atributoViejo) && !lAtributos.contains((atributoNuevo))) {
            int pos = lAtributos.indexOf(atributoViejo);
            lAtributos.remove(atributoViejo);
            lAtributos.add(pos, atributoNuevo);
            hayCambios(Cambios.ATRIBUTOS);
        }
    }

    public void eliminarAtributo(String atributo) {
        if (lAtributos.contains(atributo)) {
            lAtributos.remove(atributo);
            hayCambios(Cambios.ATRIBUTOS);
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
            hayCambios(Cambios.DEPENDECIAS);
        }
    }

    public void modificarDependenciaFuncional(DependenciaFuncional dependenciaFuncionalAntigua, DependenciaFuncional dependenciaFuncionalNueva) {
        if (lDependenciasFuncionales.contains(dependenciaFuncionalAntigua) && !lDependenciasFuncionales.contains(dependenciaFuncionalNueva)) {
            int pos = lDependenciasFuncionales.indexOf(dependenciaFuncionalAntigua);
            lDependenciasFuncionales.remove(dependenciaFuncionalAntigua);
            lDependenciasFuncionales.add(pos, dependenciaFuncionalNueva);
            hayCambios(Cambios.DEPENDECIAS);
        }
    }

    public void eliminarDependenciaFuncional(DependenciaFuncional dependenciaFuncional) {
        if (lDependenciasFuncionales.contains(dependenciaFuncional)) {
            lDependenciasFuncionales.remove(dependenciaFuncional);
            hayCambios(Cambios.DEPENDECIAS);
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
                        claves.remove(string);
                }
            }
        }

        //Asigno por default la clave candidata como el primer valor de la lista de claves
        claveCandidata = claves.get(0);

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
        hayCambios(Cambios.CLAVECANDIDATA);
        if(claves.contains(claveCandidata))
            this.claveCandidata = claveCandidata;
    }

    public ArrayList<String> darClaveCandidataSeleccionada()
    {
        return (ArrayList<String>) claveCandidata.clone();
    }

    public ArrayList<ArrayList<String>> darSuperClaves() {
        return claves;
    }

    //FORMA NORMAL
    public FormaNormal calcularFormaNormal() {

        if(formaNormal == null) {
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

        if(fmin.size() == 0) {
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

        if(tableaux == null && esquema != null)
            tableaux = new Tableaux(esquema,lAtributos,fmin);

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
            case DEPENDECIAS: claveCandidata.clear();
            case CLAVECANDIDATA:formaNormal= null;fmin.clear();claves.clear();
            case ESQUEMA: tableaux = null;
        }

    }

    public enum Cambios{ATRIBUTOS,DEPENDECIAS,CLAVECANDIDATA,ESQUEMA}

    private void clean() {
        lAtributos.clear();
        lDependenciasFuncionales.clear();
        claves.clear();
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
