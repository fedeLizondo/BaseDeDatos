package LogicaNegocio;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 26/01/17.
 */
public class DFComplejaTest {

    public static ArrayList<String> determinante = new ArrayList<String>(){{
        add("A");
        add("B");
    }};
    public static ArrayList<String> determinado = new ArrayList<String>(){{
        add("C");
        add("D");
    }};

    public static DFCompleja dfCompleja = new DFCompleja(determinante,determinado);


    @Test
    public void tengoDF() throws Exception {

        assertEquals(false,dfCompleja.tengoDF("A",null));
        assertEquals(false,dfCompleja.tengoDF(null,"C"));
        assertEquals(false,dfCompleja.tengoDF("","C"));
        assertEquals(false,dfCompleja.tengoDF("A",""));
        assertEquals(false,dfCompleja.tengoDF("C","A"));
        assertEquals(false,dfCompleja.tengoDF("B","A"));

        //DEBE SER CIERTO
        assertEquals(true,dfCompleja.tengoDF( "A","C") );
        assertEquals(true,dfCompleja.tengoDF( "A","D") );
        assertEquals(true,dfCompleja.tengoDF( "B","C") );
        assertEquals(true,dfCompleja.tengoDF( "B","D") );
    }

    @Test
    public void convertirAFmin() throws Exception {
        ArrayList<DependenciaFuncional> AUX = new ArrayList<DependenciaFuncional>();
        //EL FMIN NO ES VACIO
        assertNotEquals(AUX,dfCompleja.convertirAFmin());

        AUX.add(new DFSimple("A","B"));
        assertNotEquals(AUX,dfCompleja.convertirAFmin());
        AUX.remove(0);

        AUX.add(dfCompleja);
        assertNotEquals(AUX,dfCompleja.convertirAFmin());
        AUX.remove(0);

        AUX.add(new DFDeterminanteComplejo(determinante,"C"));
        AUX.add(new DFDeterminanteComplejo(determinante,"D"));
        assertEquals(AUX,dfCompleja.convertirAFmin());

    }

    @Test
    public void getDeterminante() throws Exception {
        ArrayList<String> Determinante = new ArrayList<String>();

        assertNotEquals( "" , dfCompleja.getDeterminante() );
        assertNotEquals( null , dfCompleja.getDeterminante() );
        assertNotEquals( Determinante , dfCompleja.getDeterminante() );
        Determinante.add("B");
        assertNotEquals( Determinante , dfCompleja.getDeterminante() );
        Determinante.remove(0);
        Determinante.add("a");
        assertNotEquals( Determinante , dfCompleja.getDeterminante() );
        Determinante.remove(0);

        //DEBE SER CIERTO
        Determinante.add("A");
        Determinante.add("B");
        assertEquals(Determinante,dfCompleja.getDeterminante());

    }

    @Test
    public void getDeterminado() throws Exception {
        ArrayList<String> Determinado = new ArrayList<String>();

        assertNotEquals( "" , dfCompleja.getDeterminado() );
        assertNotEquals( null , dfCompleja.getDeterminado() );
        assertNotEquals( Determinado , dfCompleja.getDeterminado() );
        Determinado.add("A");
        assertNotEquals( Determinado , dfCompleja.getDeterminado() );
        Determinado.remove(0);
        Determinado.add("b");
        assertNotEquals( Determinado , dfCompleja.getDeterminado() );
        Determinado.remove(0);

        //DEBE SER CIERTO
        Determinado.add("C");
        Determinado.add("D");
        assertEquals(Determinado,dfCompleja.getDeterminado());
    }

    @Test
    public void soyDeterminanteComplejo() throws Exception {
        assertEquals(false,dfCompleja.soyDeterminanteComplejo());
    }

    @Test
    public void soyCompleja() throws Exception {
        assertEquals(true,dfCompleja.soyCompleja());
    }

    @Test
    public void toStrings() throws Exception {
        assertEquals("[A, B] -> [C, D]",dfCompleja.toString());
    }

    @Test
    public void hashCodes() throws Exception {

    }

    @Test
    public void equals() throws Exception {
        DFCompleja dfSimpleAux;
        //DEBE FALLAR
        assertEquals(false,dfCompleja.equals(null));
        assertEquals(false,dfCompleja.equals(""));
        dfSimpleAux = new DFCompleja(determinado,determinante);
        assertEquals(false,dfCompleja.equals(dfSimpleAux));

        //DEBE SER CIERTO
        dfSimpleAux = new DFCompleja(determinante,determinado );
        assertEquals(true,dfCompleja.equals(dfSimpleAux));
    }

    @Test
    public void dameAtributos() throws Exception {
        ArrayList<String> Atributo = new ArrayList<String>();

        assertNotEquals( "" , dfCompleja.dameAtributos() );
        assertNotEquals( null , dfCompleja.dameAtributos() );
        assertNotEquals( Atributo , dfCompleja.dameAtributos() );
        Atributo.add("A");
        assertNotEquals( Atributo , dfCompleja.dameAtributos() );
        Atributo.remove(0);
        Atributo.add("b");
        assertNotEquals( Atributo , dfCompleja.dameAtributos() );
        Atributo.remove(0);

        //DEBE SER CIERTO
        Atributo.add("A");
        Atributo.add("B");
        Atributo.add("C");
        Atributo.add("D");
        assertEquals(Atributo,dfCompleja.dameAtributos());
    }

}