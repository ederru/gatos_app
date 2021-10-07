import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        int opcionMenu = -1;
        String botones[] = {
                "1. Ver Gatos" , "2. Ver Favoritos" ,"3. Salir"
        };
        do {

            //Menu principal
            String opcion =(String) JOptionPane.showInputDialog(null, "Gatitos Java",
                    "Menu Principal", JOptionPane.INFORMATION_MESSAGE, null, botones, botones[0]);

            //Validamos la opcion que selecciona el usuario
            for (int i = 0; i < botones.length; i++) {
                if(opcion.equals(botones[i])) {
                    opcionMenu = i;
                }
            }

            switch (opcionMenu) {
                case 0:
                    GatosService.verGatos();
                    break;
                case 1:
                    Gatos gato = new Gatos();
                    GatosService.verFavoritos(gato.getApikey());
                    break;
                default:
                    break;
            }


        }while(opcionMenu != 1);

    }
}
