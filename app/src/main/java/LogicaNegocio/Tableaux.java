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
        pasosTableaux.add(new PasoTableaux(tabla,null,new ArrayList<Coordenada>()));
    }

    private void calcularPasos()
    {
        int tamAtributos = atributos.size();
        int tamEsquema = esquema.getEsquemas().size();
        int tamDependecia = dependenciaFuncionales.size();
        boolean hayCambios = true;

        String[][] tabla = new String[tamEsquema][tamAtributos];
        String[][] tablaAux = pasosTableaux.get(0).getPaso();

        for(int i = 0; i < tamEsquema ; i++)
        {
            for(int j = 0;j<tamAtributos;j++)
            {
                tabla[i][j] = tablaAux[i][j];
            }
        }

        while (hayCambios && !lineaCompleta)
        {
            hayCambios = false;
            int indexDF = 0;

            while(indexDF<tamDependecia && !lineaCompleta )
            {
                HashMap<String,String> valoresReemplazo = new HashMap<>();

                DependenciaFuncional df = dependenciaFuncionales.get(indexDF);
                indexDF++;

                ArrayList<String> determinante = df.getDeterminante();
                String determinado = df.getDeterminado().get(0);
                ArrayList<Coordenada> cambios = new ArrayList<>();

                ArrayList<Integer> posicionDeterminante = new ArrayList<>();
                int posicionDeterminado = atributos.indexOf(determinado);

                for(String elemento:determinante)
                {
                    posicionDeterminante.add(atributos.indexOf(elemento));
                }

                for(int indexEsquema=0;indexEsquema<tamEsquema;indexEsquema++)
                {
                    ArrayList<String> componenteDeterminante = new ArrayList<>();
                    for(Integer indexDeterminante : posicionDeterminante)
                    {
                        componenteDeterminante.add(tabla[indexEsquema][indexDeterminante]);
                    }

                    String componenteDeterminado = tabla[indexEsquema][posicionDeterminado];


                    if(valoresReemplazo.containsKey(componenteDeterminante.toString()) )
                    {

                        String hashDeterminado = valoresReemplazo.get( componenteDeterminante.toString() ) ;

                        if(  (componenteDeterminado.contains("A") && hashDeterminado.contains("b") )  )
                        {
                            hayCambios = true;

                            valoresReemplazo.remove(componenteDeterminante.toString());
                            valoresReemplazo.put(componenteDeterminante.toString(),componenteDeterminado);

                            cantidadAxFila[indexEsquema] +=1;
                            cambios.add(new Coordenada(indexEsquema,posicionDeterminado));
                            tabla[indexEsquema][posicionDeterminado] = componenteDeterminado;

                            for(int i = 0;i<indexEsquema;i++) {
                                ArrayList<String> componenteDeterminanteAux = new ArrayList<>();
                                for (Integer index : posicionDeterminante) {
                                    componenteDeterminanteAux.add(tabla[i][index]);
                                }
                                if (componenteDeterminante.containsAll(componenteDeterminanteAux)  )
                                {
                                    if(!componenteDeterminado.toString().equals(tabla[i][posicionDeterminado]))
                                    {
                                        cambios.add(new Coordenada(i, posicionDeterminado));
                                        tabla[i][posicionDeterminado] = componenteDeterminado;
                                        cantidadAxFila[indexEsquema]++;
                                    }
                                }
                            }
                        }
                        else
                        {

                            if(!hashDeterminado.equals(componenteDeterminado))
                            {

                                hayCambios = true;

                                if( componenteDeterminado.contains("b") && hashDeterminado.contains("A"))
                                {
                                    cambios.add(new Coordenada(indexEsquema,posicionDeterminado));
                                    tabla[indexEsquema][posicionDeterminado] = hashDeterminado;
                                    cantidadAxFila[indexEsquema]++;
                                }
                                else
                                {
                                    //valoresReemplazo.remove(componenteDeterminante.toString());
                                    //valoresReemplazo.put(componenteDeterminante.toString(),componenteDeterminado);
                                    cambios.add(new Coordenada(indexEsquema,posicionDeterminado));
                                    tabla[indexEsquema][posicionDeterminado] = hashDeterminado;
                                }
                            }
                        }
                    }
                    else
                        valoresReemplazo.put(componenteDeterminante.toString(),componenteDeterminado);

                }//Fin Esquemas

                tablaAux = new String[tamEsquema][atributos.size()];
                for(int i = 0;i<tamEsquema;i++ )
                {
                    for(int j=0;j<atributos.size();j++)
                    {
                        tablaAux[i][j]=tabla[i][j];
                    }
                }

                PasoTableaux pasoTableaux = new PasoTableaux(tablaAux,df,cambios);
                pasosTableaux.add(pasoTableaux);

                /*for(Integer cantidadFila : cantidadAxFila)
                {
                    if(lineaCompleta != true)
                    {
                        lineaCompleta = ( cantidadFila == atributos.size() );
                    }
                    else
                    {
                        lineaCompleta = true;
                    }

                }*/

                for(int esq =0;esq<tamEsquema;esq++)
                {
                    if(!lineaCompleta )
                    {
                        int fila = 0;
                        while( fila<tamAtributos && tabla[esq][fila].contains("A"))
                        {
                            fila++;
                        }
                        lineaCompleta = !(fila <tamAtributos);
                    }
                }

                if(lineaCompleta == true)
                    lineaCompleta = true;
            }
            
        }


    }

    public int darFilas()
    {
        return esquema.getEsquemas().size();
    }

    public int darColumnas()
    {
        return atributos.size();
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

    public int cantidadDePasos(){return  pasosTableaux.size();}

}
