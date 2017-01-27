package LogicaNegocio;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 27/01/17.
 */
public class SegundaFormaNormalTest {
    private static DFSimple dfSimple = new DFSimple("A","B");
    private static SegundaFormaNormal segundaFormaNormal = new SegundaFormaNormal(dfSimple);

    @Test
    public void justificaMiFN() throws Exception {
        assertEquals("Esta en 2da Forma Normal :\n" +
                "Por la dependencia Funcional "+dfSimple.toString()+". ",segundaFormaNormal.JustificaMiFN() );
    }

    @Test
    public void toStrings() throws Exception {
        assertEquals("SEGUNDA FORMA NORMAL",segundaFormaNormal.toString());
    }

    @Test
    public void soyprimeraFormaNormal() throws Exception {
        assertEquals(false,segundaFormaNormal.soyFNBC());
    }

    @Test
    public void soyTerceraFN() throws Exception {
        assertEquals(false,segundaFormaNormal.soyTerceraFN());
    }

    @Test
    public void soySegundaFN() throws Exception {
        assertEquals(true,segundaFormaNormal.soySegundaFN());
    }

    @Test
    public void soyPrimeraFN() throws Exception {
        assertEquals(false,segundaFormaNormal.soyPrimeraFN());
    }

}