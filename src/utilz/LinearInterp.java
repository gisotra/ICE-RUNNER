package utilz;

public class LinearInterp {
    
    public static float lerp(float a, float b, float t){
        return a + (b - a) * t;
    }
    /*
    Essa função realiza a interpolação linear entre 2 valores, poderada
    pelo valor passado em t, proporcionalmente 
    
    se t = 0, retorna a
    se t = 1, retorna b
    https://www.youtube.com/watch?v=jvPPXbo87ds&t=140s
    */
    
}
