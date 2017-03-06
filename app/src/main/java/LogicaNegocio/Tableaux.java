package LogicaNegocio;

import java.util.ArrayList;

/**
 * Created by federicolizondo on 05/03/17.
 */

public class Tableaux {

    private ArrayList<PasoTableaux> pasosTableaux;
    private Esquemas esquema;
    private ArrayList<String> atributos;
    private ArrayList<DependenciaFuncional> dependenciaFuncionales;
    private int[] cantidadAxFila;
    private boolean lineaCompleta;

    public Tableaux(Esquemas esquema, ArrayList<String> atributos, ArrayList<DependenciaFuncional> dependenciaFuncionales) {
        this.esquema = esquema;
        this.atributos = atributos;
        this.dependenciaFuncionales = dependenciaFuncionales;
        lineaCompleta = false;
        cantidadAxFila = new int[esquema.getEsquemas().size()];
        pasosTableaux = new ArrayList<>();

        inicializarTableaux();
        calcularPasos();
    }


    private void inicializarTableaux()
    {
        int tamAtributos = atributos.size();
        int tamSubEsquema = esquema.getEsquemas().size();
        int indexAtributo = 0;
        int indexEsquema = 0;
        String[][] tabla = new String[tamSubEsquema][tamAtributos];

        for (ArrayList<String> se : esquema.getEsquemas()) {
            for ( indexAtributo = 0; indexAtributo < tamAtributos; indexAtributo++) {
                //Si el subEsquema contiene un atributo se carga con A
                if (se.contains(atributos.get(indexAtributo))) {
                    tabla[indexEsquema][indexAtributo] = " A" + (indexAtributo + 1) + " ";
                    cantidadAxFila[indexEsquema] += 1;
                    //Verifico si tengo una Fila Compuestas Por A
                    if (cantidadAxFila[indexEsquema] == tamSubEsquema)
                        lineaCompleta = true;
                } else
                    tabla[indexEsquema][indexAtributo] = "b" + (indexEsquema + 1) + "," + (indexAtributo + 1);
            }
            indexEsquema++;
        }
        pasosTableaux.add(new PasoTableaux(tabla,null,null));
    }

    private void calcularPasos()
    {
        //TODO TERMINAR DE IMPLEMENTAR LOS CAMBIOS
        boolean hayCambios = true;
        String[][] tabla = primerPaso().getPaso();

        while ( hayCambios && !lineaCompleta )
        {
            hayCambios = false;

            for(DependenciaFuncional df:dependenciaFuncionales)
            {
                for (int indexEsquema = 0; indexEsquema < esquema.getEsquemas().size(); indexEsquema++)
                {

                    for (int indexAtributo = 0; indexAtributo < atributos.size(); indexAtributo++) {

                    }//FIN DE ATRIBUTOS
                }//FIN DE ESQUEMAS
            }//FIN DE DEPENDENCIAS FUNCIONALES

        }//FIN DEL WHILE


    }


    public PasoTableaux getPaso(int index)
    {
        return pasosTableaux.get(index);
    }

    public PasoTableaux ultimoPaso()
    {
        return pasosTableaux.get(pasosTableaux.size()-1);
    }

    public PasoTableaux primerPaso()
    {
        return pasosTableaux.get(0);
    }

    public boolean tengoPerdidaDeInformacion()
    {
        return !lineaCompleta;
    }

}
