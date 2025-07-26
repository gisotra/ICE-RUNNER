package gamestates;

public enum Gamestate {
    /*
    Classe especial: Enumerador 
    Aqui vai ter várias constantes 
    Os elementos de uma enumeração por padrão são considerados Static e Final
    
    Se eu tentar instanciar usando new algum objeto do tipo enumerador vai dar 
    erro de compilação
    */
    MENU, 
    TUTORIAL, 
    ABOUT, 
    PLAYING, 
    GAME_OVER, 
    MULTIPLAYER_MENU,
    HOSTING, //botão "criar servidor"
    WAITING, //botão "jogar como cliente"
    PLAYING_ONLINE,
    LOCAL_MULTIPLAYER,
    END;
    
    public static Gamestate state = MENU;
}
