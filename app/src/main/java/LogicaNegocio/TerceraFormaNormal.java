package LogicaNegocio;

/**
 * Created by federicolizondo on 25/01/17.
 */
public class TerceraFormaNormal extends FormaNormal {

    private DependenciaFuncional dependenciaFuncional;

    public TerceraFormaNormal(DependenciaFuncional DF)
    {
        super();
        this.dependenciaFuncional = DF;
    }

    @Override
    public String JustificaMiFN() {

        return "Esta en 3ra Forma Normal :\nPor la dependencia Funcional "+dependenciaFuncional.toString()+". ";
    }

    @Override
    public String toString() {
        return "TERCERA FORMA NORMAL";
    }

    @Override
    public boolean soyFNBC() {
        return false;
    }

    @Override
    public boolean soyTerceraFN() {
        return true;
    }

    @Override
    public boolean soySegundaFN() {
        return false;
    }

    @Override
    public boolean soyPrimeraFN() {
        return false;
    }
}
