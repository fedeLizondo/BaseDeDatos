package LogicaNegocio;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 25/01/17.
 */
public class DFDeterminadoComplejoTest {

    public static ArrayList<String> determinado = new ArrayList<String>(){{
        add("B");
        add("C");
    }};

    public static DFDeterminadoComplejo dfDeterminadoComplejo = new DFDeterminadoComplejo("A",determinado);

    @Test
    public void tengoDF() throws Exception {

        assertEquals(false,dfDeterminadoComplejo.tengoDF("A",null));
        assertEquals(false,dfDeterminadoComplejo.tengoDF(null,"C"));
        assertEquals(false,dfDeterminadoComplejo.tengoDF("","C"));
        assertEquals(false,dfDeterminadoComplejo.tengoDF("A",""));
        assertEquals(false,dfDeterminadoComplejo.tengoDF("C","A"));
        assertEquals(false,dfDeterminadoComplejo.tengoDF("B","A"));

        //DEBE SER CIERTO
        assertEquals(true,dfDeterminadoComplejo.tengoDF( "A","C") );
        assertEquals(true,dfDeterminadoComplejo.tengoDF( "A","B") );
    }

    @Test
    public void convertirAFmin() throws Exception {

        ArrayList<DependenciaFuncional> AUX = new ArrayList<DependenciaFuncional>();
        //EL FMIN NO ES VACIO
        assertNotEquals(AUX,dfDeterminadoComplejo.convertirAFmin());

        AUX.add(new DFSimple("A","B"));
        assertNotEquals(AUX,dfDeterminadoComplejo.convertirAFmin());
        AUX.remove(0);

        //PARA CONVERTIR A FMIN DEBE SER DEPENDECIA SIMPLE
        AUX.add(dfDeterminadoComplejo);
        assertNotEquals(AUX,dfDeterminadoComplejo.convertirAFmin());
        AUX.remove(0);

        AUX.add(new DFSimple("A","B"));
        AUX.add(new DFSimple("A","C"));
        assertEquals(AUX,dfDeterminadoComplejo.convertirAFmin());

    }

    @Test
    public void getDeterminante() throws Exception {

        ArrayList<String> Determinante = new ArrayList<String>();

        assertNotEquals( "" , dfDeterminadoComplejo.getDeterminante() );
        assertNotEquals( null , dfDeterminadoComplejo.getDeterminante() );
        assertNotEquals( Determinante , dfDeterminadoComplejo.getDeterminante() );
        Determinante.add("B");
        assertNotEquals( Determinante , dfDeterminadoComplejo.getDeterminante() );
        Determinante.remove(0);
        Determinante.add("a");
        assertNotEquals( Determinante , dfDeterminadoComplejo.getDeterminante() );
        Determinante.remove(0);

        //DEBE SER CIERTO
        Determinante.add("A");
        assertEquals(Determinante,dfDeterminadoComplejo.getDeterminante());

    }

    @Test
    public void getDeterminado() throws Exception {
        ArrayList<String> Determinado = new ArrayList<String>();

        assertNotEquals( "" , dfDeterminadoComplejo.getDeterminado() );
        assertNotEquals( null , dfDeterminadoComplejo.getDeterminado() );
        assertNotEquals( Determinado , dfDeterminadoComplejo.getDeterminado() );
        Determinado.add("A");
        assertNotEquals( Determinado , dfDeterminadoComplejo.getDeterminado() );
        Determinado.remove(0);
        Determinado.add("b");
        assertNotEquals( Determinado , dfDeterminadoComplejo.getDeterminado() );
        Determinado.remove(0);

        //DEBE SER CIERTO
        Determinado.add("B");
        Determinado.add("C");
        assertEquals(Determinado,dfDeterminadoComplejo.getDeterminado());
    }

    @Test
    public void soyDeterminanteComplejo() throws Exception {
        assertEquals(false,dfDeterminadoComplejo.soyDeterminanteComplejo());
    }

    @Test
    public void soyCompleja() throws Exception {
        assertEquals(false,dfDeterminadoComplejo.soyDeterminanteComplejo());
    }

    @Test
    public void toStrings() throws Exception {
        assertEquals("A -> [B, C]",dfDeterminadoComplejo.toString());
    }

    @Test
    public void hashCodes() throws Exception {

    }

    @Test
    public void equals() throws Exception {
        DFDeterminadoComplejo dfSimpleAux;
        //DEBE FALLAR
        assertEquals(false,dfDeterminadoComplejo.equals(null));
        assertEquals(false,dfDeterminadoComplejo.equals(""));
        dfSimpleAux = new DFDeterminadoComplejo("B",determinado);
        assertEquals(false,dfDeterminadoComplejo.equals(dfSimpleAux));

        //DEBE SER CIERTO
        dfSimpleAux = new DFDeterminadoComplejo("A",determinado );
        assertEquals(true,dfDeterminadoComplejo.equals(dfSimpleAux));
    }

    @Test
    public void dameAtributos() throws Exception {
        ArrayList<String> Atributo = new ArrayList<String>();

        assertNotEquals( "" , dfDeterminadoComplejo.dameAtributos() );
        assertNotEquals( null , dfDeterminadoComplejo.dameAtributos() );
        assertNotEquals( Atributo , dfDeterminadoComplejo.dameAtributos() );
        Atributo.add("A");
        assertNotEquals( Atributo , dfDeterminadoComplejo.dameAtributos() );
        Atributo.remove(0);
        Atributo.add("b");
        assertNotEquals( Atributo , dfDeterminadoComplejo.dameAtributos() );
        Atributo.remove(0);

        //DEBE SER CIERTO
        Atributo.add("A");
        Atributo.add("B");
        Atributo.add("C");

        assertEquals(Atributo,dfDeterminadoComplejo.dameAtributos());
    }

}