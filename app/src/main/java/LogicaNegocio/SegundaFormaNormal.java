package LogicaNegocio;

/**
 * Created by federicolizondo on 25/01/17.
 */
public class SegundaFormaNormal extends FormaNormal {

    private DependenciaFuncional dependenciaFuncional;

    public SegundaFormaNormal(DependenciaFuncional DF)
    {
        super();
        this.dependenciaFuncional = DF;
    }

    @Override
    public String JustificaMiFN() {

        return "Esta en 2da Forma Normal :\nPor la dependencia Funcional "+dependenciaFuncional.toString()+". ";
    }

    @Override
    public String toString() {
        return "SEGUNDA FORMA NORMAL";
    }


    @Override
    public boolean soyFNBC() {
        return false;
    }

    @Override
    public boolean soyTerceraFN() {
        return false;
    }

    @Override
    public boolean soySegundaFN() {
        return true;
    }

    @Override
    public boolean soyPrimeraFN() {
        return false;
    }

    @Override
    public DependenciaFuncional obtenerDependenciaFuncional() {
        return dependenciaFuncional;
    }
}
