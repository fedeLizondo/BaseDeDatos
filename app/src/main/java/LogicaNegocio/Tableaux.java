package LogicaNegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

    public Tableaux(final Esquemas esquema,final ArrayList<String> atributos,final ArrayList<DependenciaFuncional> dependenciaFuncionales) {
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
                    if (cantidadAxFila[indexEsquema] == tamAtributos)
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

        boolean hayCambios = true;
        String[][] tabla = primerPaso().getPaso();
        int sizeEsquema = esquema.getEsquemas().size();
        int sizeDependenciaFuncional = dependenciaFuncionales.size();
        int indexDF = 0;
        while ( hayCambios && !lineaCompleta )
        {
            hayCambios = false;

            while (indexDF<sizeDependenciaFuncional && !lineaCompleta)
            //for(DependenciaFuncional df:dependenciaFuncionales)
            {
                DependenciaFuncional df = dependenciaFuncionales.get(indexDF);
                indexDF++;

                //TODO VERIFICAR SI 2 LISTAS CON LOS MISMOS VALORES LO TOMAN COMO LISTA DISTINTAS
                //SI ES ASI ,CONVERTIR LA LISTAS EN UN STRING
                HashMap<ArrayList<String>, String> valoresReemplazo = new HashMap<ArrayList<String>, String>();

                ArrayList<Coordenada> cambios = new ArrayList<>();

                ArrayList<String> Determinante = df.getDeterminante();
                ArrayList<String> Determinado = df.getDeterminado();

                ArrayList<Integer> posicionDeterminante = new ArrayList<>();
                for(String elemento:Determinante)
                {
                    posicionDeterminante.add(atributos.indexOf(elemento));
                }

                int posicionDeterminado = atributos.indexOf(Determinado.get(0));

                for (int indexEsquema = 0; indexEsquema < sizeEsquema ; indexEsquema++)
                {
                    ArrayList<String> componenteDeterminante = new ArrayList<>();

                    for(Integer index:posicionDeterminante)
                    {
                        componenteDeterminante.add(tabla[indexEsquema][index]);
                    }
                    String componenteDeterminado = tabla[indexEsquema][posicionDeterminado];

                    if(valoresReemplazo.containsKey(componenteDeterminante))
                    {
                       hayCambios = true;

                       String componenteDeterminadoHash = valoresReemplazo.get(componenteDeterminante);
                       if(!componenteDeterminadoHash.equals(componenteDeterminado))
                       {
                           if(componenteDeterminado.contains("A") && componenteDeterminadoHash.contains("b"))
                           {
                               valoresReemplazo.remove(componenteDeterminante);
                               valoresReemplazo.put(componenteDeterminante,componenteDeterminado);

                               for(int i = 0;i<indexEsquema;i++) {
                                   ArrayList<String> componenteDeterminanteAux = new ArrayList<>();
                                   for (Integer index : posicionDeterminante) {
                                       componenteDeterminanteAux.add(tabla[indexEsquema][index]);
                                   }
                                   if (componenteDeterminante.containsAll(componenteDeterminanteAux))
                                   {
                                       cambios.add(new Coordenada(i,posicionDeterminado));
                                       tabla[indexEsquema][posicionDeterminado] = componenteDeterminado;
                                       cantidadAxFila[indexEsquema]++;
                                   }
                               }
                           }
                           else
                           {
                               cambios.add(new Coordenada(indexEsquema,posicionDeterminado));
                               tabla[indexEsquema][posicionDeterminado] = componenteDeterminadoHash;
                               cantidadAxFila[indexEsquema]++;

                           }
                       }
                    }
                    else
                    {
                        valoresReemplazo.put(componenteDeterminante,componenteDeterminado);
                    }
                    //ArrayList<String> esq = esquema.getEsquemas().get(indexEsquema);
                }//FIN DE ESQUEMAS



                PasoTableaux pasoTableaux = new PasoTableaux((String[][]) tabla.clone(),df,cambios);
                pasosTableaux.add(pasoTableaux);

                for(Integer cantidadFila : cantidadAxFila)
                {
                    if(lineaCompleta != true)
                    {
                        lineaCompleta = ( cantidadFila == atributos.size() );
                    }
                }

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
