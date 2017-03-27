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

    public String[][] imprimirEsquema(final ArrayList<String> Atributos,final Esquemas esquemas)
    {
        int sizeFila = esquemas.getEsquemas().size()+1;
        int sizeColumna = Atributos.size()+1;
        String[][] resultado = new String[sizeFila][sizeColumna];

        resultado[0][0]="";
        for(int columna = 0;columna<sizeColumna-1;columna++)
        {
            resultado[0][columna+1] = Atributos.get(columna);
        }

        int fila = 1;
        for(ArrayList<String> esquema : esquemas.getEsquemas())
        {
            resultado[fila++][0] = esquema.toString();
        }

        for (fila = 0;fila<sizeFila-1;fila++)
        {
            for (int columna = 0;columna<sizeColumna-1;columna++)
            {
                resultado[fila+1][columna+1] = paso[fila][columna];
            }
        }

        return resultado;
    }

    public DependenciaFuncional getDependenciaFuncional() {
        return dependenciaFuncional;
    }

    public ArrayList<Coordenada> getCambios() {
        return cambios;
    }
}

