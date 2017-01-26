package LogicaNegocio;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 25/01/17.
 */

public class DFSimpleTest {

    public DFSimple dfSimple = new DFSimple("A","B");

    @Test
    public void tengoDF() throws Exception {
        //NO EXISTE LA DEPENDENCIA FUNCIONAL
        assertEquals(false,dfSimple.tengoDF("","C"));
        assertEquals(false,dfSimple.tengoDF("A",""));
        assertEquals(false,dfSimple.tengoDF("A","C"));
        assertEquals(false,dfSimple.tengoDF("B","A"));

        //EXISTE LA DEPENDENCIA FUNCIONAL
        assertEquals(true,dfSimple.tengoDF("A","B"));
    }

    @Test
    public void convertirAFmin() throws Exception {
        ArrayList<DependenciaFuncional> AUX = new ArrayList<DependenciaFuncional>();
        //EL FMIN NO ES VACIO
        assertNotEquals(AUX,dfSimple.convertirAFmin());
        AUX.add(dfSimple);

        //PARA CONVERTIR A FMIN DEBE SER DEPENDECIA SIMPLE
        assertEquals(AUX,dfSimple.convertirAFmin());
    }

    @Test
    public void soyDeterminanteComplejo() throws Exception {
        //NUNCA DEBE SER COMPLEJO
        assertNotEquals(true,dfSimple.soyDeterminanteComplejo());

        //SIEMPRE DEBE SER FALSO ; POR QUE ES SIMPLE
        assertEquals(false,dfSimple.soyDeterminanteComplejo());
    }

    @Test
    public void soyCompleja() throws Exception {

        //NUNCA DEBE SER COMPLEJO
        assertNotEquals(true,dfSimple.soyDeterminanteComplejo());

        //SIEMPRE DEBE SER FALSO ; POR QUE ES SIMPLE
        assertEquals(false,dfSimple.soyDeterminanteComplejo());
    }

    @Test
    public void toStringS() throws Exception {
        assertEquals("A -> B",dfSimple.toString());
    }

    @Test
    public void hashCodeS() throws Exception {
        //DEBE SER DISTINTO
        assertNotEquals(("A"+"B").hashCode() , dfSimple.hashCode());
        assertNotEquals(("b"+"a").hashCode() , dfSimple.hashCode());

        //El orden es determinado y determinante
        assertEquals( ("B"+"A").hashCode() , dfSimple.hashCode() );
    }

    @Test
    public void equals() throws Exception {

        DFSimple dfSimpleAux;
        //DEBE FALLAR
        assertEquals(false,dfSimple.equals(null));
        assertEquals(false,dfSimple.equals(""));
        dfSimpleAux = new DFSimple("B","A");
        assertEquals(false,dfSimple.equals(dfSimpleAux));

        //DEBE SER CIERTO
        dfSimpleAux = new DFSimple("A","B");
        assertEquals(true,dfSimple.equals(dfSimpleAux));

    }

    @Test
    public void getDeterminante() throws Exception {

        ArrayList<String> Determinante = new ArrayList<String>();

        assertNotEquals( "" , dfSimple.getDeterminante() );
        assertNotEquals( null , dfSimple.getDeterminante() );
        assertNotEquals( Determinante , dfSimple.getDeterminante() );
        Determinante.add("B");
        assertNotEquals( Determinante , dfSimple.getDeterminante() );
        Determinante.remove(0);
        Determinante.add("a");
        assertNotEquals( Determinante , dfSimple.getDeterminante() );
        Determinante.remove(0);
        //DEBE SER CIERTO
        Determinante.add("A");
        assertEquals(Determinante,dfSimple.getDeterminante());

    }

    @Test
    public void getDeterminado() throws Exception {

        ArrayList<String> Determinado = new ArrayList<String>();

        assertNotEquals( "" , dfSimple.getDeterminado() );
        assertNotEquals( null , dfSimple.getDeterminado() );
        assertNotEquals( Determinado , dfSimple.getDeterminado() );
        Determinado.add("A");
        assertNotEquals( Determinado , dfSimple.getDeterminado() );
        Determinado.remove(0);
        Determinado.add("b");
        assertNotEquals( Determinado , dfSimple.getDeterminado() );
        Determinado.remove(0);

        //DEBE SER CIERTO
        Determinado.add("B");
        assertEquals(Determinado,dfSimple.getDeterminado());

    }

    @Test
    public void dameAtributos() throws Exception {

        ArrayList<String> Atributo = new ArrayList<String>();

        assertNotEquals( "" , dfSimple.dameAtributos() );
        assertNotEquals( null , dfSimple.dameAtributos() );
        assertNotEquals( Atributo , dfSimple.dameAtributos() );
        Atributo.add("A");
        assertNotEquals( Atributo , dfSimple.dameAtributos() );
        Atributo.remove(0);
        Atributo.add("b");
        assertNotEquals( Atributo , dfSimple.dameAtributos() );
        Atributo.remove(0);

        //DEBE SER CIERTO
        Atributo.add("A");
        Atributo.add("B");
        assertEquals(Atributo,dfSimple.dameAtributos());
    }


}