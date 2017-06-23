package LogicaNegocio;

import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by federicolizondo on 27/01/17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class AdministradoraTest {
    
   private static Administradora admin = Administradora.getInstance();

    @Test
    public void agregarAtributos() throws Exception {

        ArrayList<String> atributos = new ArrayList<String>();
        assertEquals( atributos ,admin.darListadoAtributos() );

        admin.agregarAtributos("");
        assertNotEquals(atributos,admin.darListadoAtributos());

        admin.eliminarAtributo("");

        //VERIFICO QUE PUEDA AGREGAR TODOS LOS ATRIBUTOS

        admin.agregarAtributos("A");
        atributos.add("A");

        assertEquals(atributos,admin.darListadoAtributos());
        atributos.add("B");

        admin.agregarAtributos("B");
        assertEquals(atributos,admin.darListadoAtributos());

        atributos.add("C");
        atributos.add("D");
        atributos.add("E");

        admin.agregarAtributos("C");
        admin.agregarAtributos("D");
        admin.agregarAtributos("E");
        assertEquals(atributos,admin.darListadoAtributos());

    }

    @Test
    public void modificarAtributo() throws Exception {
        ArrayList<String> atributos = new ArrayList<String>(){{
            add("A");add("B");add("C");add("D");add("E");
        }};
        assertEquals(atributos,admin.darListadoAtributos());

        admin.modificarAtributo("A","B");
        assertEquals(atributos,admin.darListadoAtributos());

        admin.modificarAtributo("A","C");
        assertEquals(atributos,admin.darListadoAtributos());

        admin.modificarAtributo("A","D");
        assertEquals(atributos,admin.darListadoAtributos());
    }

    @Test
    public void eliminarAtributo() throws Exception {
        ArrayList<String> atributos = new ArrayList<String>(){{
            add("A");add("B");add("C");add("D");add("E");add("F");
        }};
        admin.agregarAtributos("F");

        assertEquals(atributos,admin.darListadoAtributos());

        admin.eliminarAtributo("M");
        assertEquals(atributos,admin.darListadoAtributos());

        admin.eliminarAtributo("F");
        atributos.remove("F");
        assertEquals(atributos,admin.darListadoAtributos());

    }

    @Test
    public void darListadoAtributos() throws Exception {
        ArrayList<String> atributos = new ArrayList<String>(){{
            add("A");add("B");add("C");add("D");add("E");
        }};
        assertEquals(atributos,admin.darListadoAtributos());
    }

    @Test
    public void agregarDependenciaFuncional() throws Exception {


        assertEquals("[]",admin.darListadoDependenciasFuncional().toString());

        
        admin.agregarDependenciaFuncional(new DFSimple("A","C"));
        admin.agregarDependenciaFuncional(new DFSimple("B","C"));
        admin.agregarDependenciaFuncional(new DFSimple("C","D"));
        admin.agregarDependenciaFuncional(new DFDeterminanteComplejo(
                new ArrayList<String>(){{ add("D");add("E");}},"C"));
        admin.agregarDependenciaFuncional(new DFDeterminanteComplejo(
                new ArrayList<String>(){{ add("C");add("E");}},"A"));

        assertEquals("[A -> C, B -> C, C -> D,  D, E  -> C,  C, E  -> A]",admin.darListadoDependenciasFuncional().toString() );

    }

    @Test
    public void modificarDependenciaFuncional() throws Exception {
        //dmin.agregarDependenciaFuncional(new DFSimple("E","F"));
        //assertEquals("[E -> F]",admin.darListadoDependenciasFuncional().toString());
        //admin.modificarDependenciaFuncional(new DFSimple("E","F"),new DFSimple("E","A"));
        //assertEquals("[E -> A]",admin.darListadoDependenciasFuncional().toString());
    }

    @Test
    public void eliminarDependenciaFuncional() throws Exception {

    }

    @Test
    public void darListadoDependenciasFuncional() throws Exception {

    }

    @Test (timeout=5000)
    public void calcularClavesCandidatas() throws Exception {


        assertEquals("[A, B, C, D, E]",admin.darListadoAtributos().toString());
        assertNotEquals("[]",admin.darListadoDependenciasFuncional().toString());
        assertEquals("[[B, E]]",admin.calcularClavesCandidatas().toString());

        ArrayList<String > aux = new ArrayList<String>();
        aux.add("B");aux.add("E");
        assertEquals("[A, B, C, D, E]",admin.calcularClausura(aux).toString());

    }

    @Test
    public void calcularUniverso() throws Exception {

        //El metodo calcularUniverso lo que hace es , a partir de una clave ver si se obtiene el universo
        Administradora adm = Administradora.getInstanceForTesting();
        ArrayList<String> clave= new ArrayList<>();
        adm.agregarAtributos("a");
        adm.agregarDependenciaFuncional(new DFSimple("a","a"));//a -> a
        clave.add("a");
        //Pruebo el caso en el que tengo  un atributo y una clave compuesta por ese atributo
        assertEquals(true,adm.calcularUniverso(clave));

        adm = Administradora.getInstanceForTesting();
        adm.agregarAtributos("a");
        adm.agregarAtributos("b");
        adm.agregarDependenciaFuncional(new DFSimple("a","a"));
        clave = new ArrayList<>();
        clave.add("a");
        //Pruebo el caso donde tengo mas de un atributo  que deberia ser falso
        assertEquals(false,adm.calcularUniverso(clave));


    }
/*
    @Test
    public void calcularClausura() throws Exception {

    }

    @Test
    public void calcularFormaNormal() throws Exception {

    }

    @Test
    public void tieneDescomposicion3FN() throws Exception {

    }

    @Test
    public void tieneDescomposicionFNBC() throws Exception {

    }

    @Test
    public void calcularDescomposicion3FN() throws Exception {

    }

    @Test
    public void calcularDescomposicionFNBC() throws Exception {

    }

    @Test
    public void calcularFmin() throws Exception {

    }

    @Test
    public void calcularTableaux() throws Exception {

    }

    @Test
    public void isTableauxHayPerdidaDeInformacion() throws Exception {

    }

    @Test
    public void darCambiosTableaux() throws Exception {

    }
*/

}