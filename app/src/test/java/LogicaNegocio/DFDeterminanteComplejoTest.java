package LogicaNegocio;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 25/01/17.
 */
public class DFDeterminanteComplejoTest {


    public static ArrayList<String> determinantes = new ArrayList<String>(){{
        add("A");
        add("B");
    }};

    public static DFDeterminanteComplejo  dfDeterminanteComplejo = new DFDeterminanteComplejo(determinantes,"C");

    @Test
    public void tengoDF() throws Exception {

        assertEquals(false,dfDeterminanteComplejo.tengoDF("A",null));
        assertEquals(false,dfDeterminanteComplejo.tengoDF(null,"C"));
        assertEquals(false,dfDeterminanteComplejo.tengoDF("","C"));
        assertEquals(false,dfDeterminanteComplejo.tengoDF("A",""));
        assertEquals(false,dfDeterminanteComplejo.tengoDF("C","A"));
        assertEquals(false,dfDeterminanteComplejo.tengoDF("B","A"));

        //DEBE SER CIERTO
        assertEquals(true,dfDeterminanteComplejo.tengoDF( "A","C") );
        assertEquals(true,dfDeterminanteComplejo.tengoDF( "B","C") );
    }

    @Test
    public void convertirAFmin() throws Exception {

        ArrayList<DependenciaFuncional> AUX = new ArrayList<DependenciaFuncional>();
        //EL FMIN NO ES VACIO
        assertNotEquals(AUX,dfDeterminanteComplejo.convertirAFmin());

        AUX.add(new DFSimple("A","B"));
        assertNotEquals(AUX,dfDeterminanteComplejo.convertirAFmin());
        AUX.remove(0);

        //PARA CONVERTIR A FMIN DEBE SER DEPENDECIA SIMPLE
        AUX.add(dfDeterminanteComplejo);
        assertEquals(AUX,dfDeterminanteComplejo.convertirAFmin());
        AUX.remove(0);


        ArrayList<String> determinantes = new ArrayList<String>(){{
            add("A");
            add("B");
        }};
        DFDeterminanteComplejo dfdComplejo = new DFDeterminanteComplejo(determinantes,"C");
        AUX.add(dfdComplejo);
        assertEquals(AUX,dfDeterminanteComplejo.convertirAFmin());

    }

    @Test
    public void getDeterminante() throws Exception {
        ArrayList<String> Determinante = new ArrayList<String>();

        assertNotEquals( "" , dfDeterminanteComplejo.getDeterminante() );
        assertNotEquals( null , dfDeterminanteComplejo.getDeterminante() );
        assertNotEquals( Determinante , dfDeterminanteComplejo.getDeterminante() );
        Determinante.add("B");
        assertNotEquals( Determinante , dfDeterminanteComplejo.getDeterminante() );
        Determinante.remove(0);
        Determinante.add("a");
        assertNotEquals( Determinante , dfDeterminanteComplejo.getDeterminante() );
        Determinante.remove(0);

        //DEBE SER CIERTO
        Determinante.add("A");
        Determinante.add("B");
        assertEquals(Determinante,dfDeterminanteComplejo.getDeterminante());

    }

    @Test
    public void getDeterminado() throws Exception {
        ArrayList<String> Determinado = new ArrayList<String>();

        assertNotEquals( "" , dfDeterminanteComplejo.getDeterminado() );
        assertNotEquals( null , dfDeterminanteComplejo.getDeterminado() );
        assertNotEquals( Determinado , dfDeterminanteComplejo.getDeterminado() );
        Determinado.add("A");
        assertNotEquals( Determinado , dfDeterminanteComplejo.getDeterminado() );
        Determinado.remove(0);
        Determinado.add("b");
        assertNotEquals( Determinado , dfDeterminanteComplejo.getDeterminado() );
        Determinado.remove(0);

        //DEBE SER CIERTO
        Determinado.add("C");
        assertEquals(Determinado,dfDeterminanteComplejo.getDeterminado());
    }

    @Test
    public void soyDeterminanteComplejo() throws Exception {

        assertNotEquals(false,dfDeterminanteComplejo.soyDeterminanteComplejo());

        assertEquals(true,dfDeterminanteComplejo.soyDeterminanteComplejo());
    }

    @Test
    public void soyCompleja() throws Exception {

        assertNotEquals(true,dfDeterminanteComplejo.soyCompleja());
        assertEquals(false,dfDeterminanteComplejo.soyCompleja());

    }

    @Test
    public void toStrings() throws Exception {

        assertEquals("[A, B] -> C",dfDeterminanteComplejo.toString());
    }

    @Test
    public void equals() throws Exception {
        DFDeterminanteComplejo dfSimpleAux;
        //DEBE FALLAR
        assertEquals(false,dfDeterminanteComplejo.equals(null));
        assertEquals(false,dfDeterminanteComplejo.equals(""));
        dfSimpleAux = new DFDeterminanteComplejo(determinantes,"A");
        assertEquals(false,dfDeterminanteComplejo.equals(dfSimpleAux));

        //DEBE SER CIERTO
        dfSimpleAux = new DFDeterminanteComplejo(determinantes,"C");
        assertEquals(true,dfDeterminanteComplejo.equals(dfSimpleAux));
    }

    @Test
    public void dameAtributos() throws Exception {

        ArrayList<String> Atributo = new ArrayList<String>();

        assertNotEquals( "" , dfDeterminanteComplejo.dameAtributos() );
        assertNotEquals( null , dfDeterminanteComplejo.dameAtributos() );
        assertNotEquals( Atributo , dfDeterminanteComplejo.dameAtributos() );
        Atributo.add("A");
        assertNotEquals( Atributo , dfDeterminanteComplejo.dameAtributos() );
        Atributo.remove(0);
        Atributo.add("b");
        assertNotEquals( Atributo , dfDeterminanteComplejo.dameAtributos() );
        Atributo.remove(0);

        //DEBE SER CIERTO
        Atributo.add("A");
        Atributo.add("B");
        Atributo.add("C");

        assertEquals(Atributo,dfDeterminanteComplejo.dameAtributos());
    }

}