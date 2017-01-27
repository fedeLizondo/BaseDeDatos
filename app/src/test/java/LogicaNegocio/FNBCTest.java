package LogicaNegocio;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 27/01/17.
 */
public class FNBCTest {

    private static FNBC fnbc = new FNBC();
    @Test
    public void justificaMiFN() throws Exception {
        assertEquals("Esta en Forma Normal de Boyce-Codd.",fnbc.JustificaMiFN() );
    }

    @Test
    public void toStrings() throws Exception {
        assertEquals("FORMA NORMAL DE BOYCE-CODD",fnbc.toString());
    }

    @Test
    public void soyFNBC() throws Exception {
        assertEquals(true,fnbc.soyFNBC());
    }

    @Test
    public void soyTerceraFN() throws Exception {
        assertEquals(false,fnbc.soyTerceraFN());
    }

    @Test
    public void soySegundaFN() throws Exception {
        assertEquals(false,fnbc.soySegundaFN());
    }

    @Test
    public void soyPrimeraFN() throws Exception {
        assertEquals(false,fnbc.soyPrimeraFN());
    }

}