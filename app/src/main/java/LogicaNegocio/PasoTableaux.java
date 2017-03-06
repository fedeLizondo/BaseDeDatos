package LogicaNegocio;

import java.util.ArrayList;

/**
 * Created by federicolizondo on 05/03/17.
 */

public class PasoTableaux  {

    private String[][] paso;
    private DependenciaFuncional dependenciaFuncional;
    private ArrayList<Coordenada> cambios;

    public PasoTableaux(String[][] paso, DependenciaFuncional dependenciaFuncional, ArrayList<Coordenada> cambios) {
        this.paso = paso;
        this.dependenciaFuncional = dependenciaFuncional;
        this.cambios = cambios;
    }

    public String[][] getPaso() {
        return paso;
    }

    public DependenciaFuncional getDependenciaFuncional() {
        return dependenciaFuncional;
    }

    public ArrayList<Coordenada> getCambios() {
        return cambios;
    }
}

