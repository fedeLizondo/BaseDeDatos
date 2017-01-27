package LogicaNegocio;

/**
 * Created by federicolizondo on 25/01/17.
 */
public abstract class FormaNormal {



    public abstract String JustificaMiFN();

    @Override
    public abstract String toString() ;

    public abstract boolean soyFNBC();

    public abstract boolean soyTerceraFN();

    public abstract boolean soySegundaFN();

    public abstract boolean soyPrimeraFN();


    public FormaNormal obtenerFormaNormal()
    {
       return  null;
    }


}
