package utilz;

public class SpriteData {
    /*
    Classe responsável por abrigar os dados do meu XML.
    Eu estou querendo implementar o xml como um "banco de dados" em que eu vou abrigar
    todos os endereços das sprites e carregar eles uma única vez
    */
    
    /*------------ ATRIBUTOS ------------*/
    private String name;
    private String path;
    //nao coloquei width nem height pq eu quero manualmente mexer nas classes

    /*------------ CONSTRUTOR ------------*/
    // Construtor
    public SpriteData(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /*------------ GETTERS E SETTERS ------------*/
    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
