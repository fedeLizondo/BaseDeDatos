package LogicaNegocio;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 27/01/17.
 */
public class TerceraFormaNormalTest {
    private static DFSimple dfSimple = new DFSimple("A","B");
    private static TerceraFormaNormal terceraFormaNormal = new TerceraFormaNormal(dfSimple);

    @Test
    public void justificaMiFN() throws Exception {
        assertEquals("Esta en 3ra Forma Normal :\n" +
                "Por la dependencia Funcional "+dfSimple.toString()+". ",terceraFormaNormal.JustificaMiFN() );
    }

    @Test
    public void toStrings() throws Exception {
        assertEquals("TERCERA FORMA NORMAL",terceraFormaNormal.toString());
    }

    @Test
    public void soyprimeraFormaNormal() throws Exception {
        assertEquals(false,terceraFormaNormal.soyFNBC());
    }

    @Test
    public void soyTerceraFN() throws Exception {
        assertEquals(true,terceraFormaNormal.soyTerceraFN());
    }

    @Test
    public void soySegundaFN() throws Exception {
        assertEquals(false,terceraFormaNormal.soySegundaFN());
    }

    @Test
    public void soyPrimeraFN() throws Exception {
        assertEquals(false,terceraFormaNormal.soyPrimeraFN());
    }

}