package LogicaNegocio;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 27/01/17.
 */
public class PrimeraFormaNormalTest {

    private static DFSimple dfSimple = new DFSimple("A","B");
    private static PrimeraFormaNormal primeraFormaNormal = new PrimeraFormaNormal(dfSimple);

    @Test
    public void justificaMiFN() throws Exception {
        assertEquals("Esta en 1ra Forma Normal :\n" +
                "Por la dependencia Funcional "+dfSimple.toString()+". ",primeraFormaNormal.JustificaMiFN() );
    }

    @Test
    public void toStrings() throws Exception {
        assertEquals("PRIMERA FORMA NORMAL",primeraFormaNormal.toString());
    }

    @Test
    public void soyprimeraFormaNormal() throws Exception {
        assertEquals(false,primeraFormaNormal.soyFNBC());
    }

    @Test
    public void soyTerceraFN() throws Exception {
        assertEquals(false,primeraFormaNormal.soyTerceraFN());
    }

    @Test
    public void soySegundaFN() throws Exception {
        assertEquals(false,primeraFormaNormal.soySegundaFN());
    }

    @Test
    public void soyPrimeraFN() throws Exception {
        assertEquals(true,primeraFormaNormal.soyPrimeraFN());
    }

}