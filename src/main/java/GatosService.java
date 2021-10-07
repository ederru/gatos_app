import com.google.gson.Gson;
import com.squareup.okhttp.*;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GatosService {

    public static void verGatos() throws IOException {
        //Vamos a traer datos de la API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").method("GET", null).build();
        Response response = client.newCall(request).execute();

        String elJason = response.body().string();

        //cortar los corchete
        elJason = elJason.substring(1, elJason.length());
        elJason = elJason.substring(0, elJason.length() - 1);

        //crear objeto de la clase gson
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJason, Gatos.class);

        //Redimensionar imagen
        Image image = null;

        try {
            URL url = new URL(gatos.getUrl());

            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
            ImageIcon fondoGato = new ImageIcon(bufferedImage);


            if (fondoGato.getIconWidth() > 800) {
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }

            String menu = "Opciones: \n"
                    + "1. Ver otra imagen\n"
                    + "2. Favorti\n"
                    + "3. Volver\n";

            String botones[] = {
                    "ver ptra imagen", "favorito", "volver"
            };
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato,
                    JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);

            int seleccion = -1;

            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    seleccion = i;
                }
            }

            switch (seleccion) {
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }


        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void favoritoGato(Gatos gato) {

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\": \"" + gato.getId() + "\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gato.getApikey())
                    .build();
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public static void verFavoritos(String apiKey) {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("GET", null)
                    .addHeader("x-api-key", apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            //Guardamos el string con la respuesta
            String elJason = response.body().string();

            //Creamos el objeto gson
            Gson gson = new Gson();

            GatosFavoritos gatosArray[] = gson.fromJson(elJason, GatosFavoritos[].class);

            if (gatosArray.length > 0) {
                int min = 1;
                int max = gatosArray.length;
                int aleatorio = (int) Math.random() * ((max - min) + 1) + min;
                int indice = aleatorio - 1;

                GatosFavoritos gatoFav = gatosArray[indice];
                //Redimensionar imagen
                Image image = null;

                try {
                    URL url = null;
                    try {
                        url = new URL(gatoFav.getImage().getUrl());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.addRequestProperty("User-Agent", "");
                    BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
                    ImageIcon fondoGato = new ImageIcon(bufferedImage);


                    if (fondoGato.getIconWidth() > 800) {
                        Image fondo = fondoGato.getImage();
                        Image modificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                        fondoGato = new ImageIcon(modificada);
                    }

                    String menu = "Opciones: \n"
                            + "1. Ver otra imagen\n"
                            + "2. Eliminar Favorto\n"
                            + "3. Volver\n";

                    String botones[] = {
                            "ver ptra imagen", "Eliminar Favorito", "volver"
                    };
                    String id_gato = gatoFav.getId();
                    String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato,
                            JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);

                    int seleccion = -1;

                    for (int i = 0; i < botones.length; i++) {
                        if (opcion.equals(botones[i])) {
                            seleccion = i;
                        }
                    }

                    switch (seleccion) {
                        case 0:
                            verFavoritos(apiKey);
                            break;
                        case 1:
                            borrarFavorito(gatoFav);
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void borrarFavorito(GatosFavoritos gatoFavorito) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/" + gatoFavorito.getId())
                    .method("DELETE", body)
                    .addHeader("x-api-key", gatoFavorito.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
        }catch (IOException e) {
            System.out.println(e);
        }
    }

}
