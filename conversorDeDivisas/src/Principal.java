import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class Principal {

    public static void main(String[] args) throws IOException, InterruptedException {
       // Objeto para manejar la cancelación del proceso
        Secundaria cancelacion = new Secundaria();

        // Variables para almacenar la opción seleccionada y los valores de conversión
        String busqueda = "";
        String divisa = "";
        int numero;
        String input;
        String[] opciones = {"Conversor de moneda"};
        String[] opcionTipoDeCambio = {"De pesos a Dolar", "De pesos a Euro", "De pesos a Libras Esterlinas",
                "De pesos a Yen", "De pesos a Won Coreano", "De Dolar a pesos", "De Euro a pesos",
                "De Libras Esterlinas a pesos", "De Yen a pesos", "De Won Coreano a pesos"};

        // Bucle principal para mantener el programa en ejecución hasta que se cancele
        while (!cancelacion.confirmacion) {
            // Mostrar menú principal para seleccionar el tipo de conversión
            String MenuPrincipal = (String) JOptionPane.showInputDialog(null, "Selecciona una opcion de conversacion",
                    "Menu", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (MenuPrincipal == null) {
                // Cancelar si se cierra el cuadro de diálogo
                cancelacion.cancelar();
                break;
            }

            // Mostrar menú para elegir la conversión específica
            String MenuDivisas = (String) JOptionPane.showInputDialog(null, "Elija la moneda a la que deseas convertir tu dinero",
                    "Monedas", JOptionPane.QUESTION_MESSAGE, null, opcionTipoDeCambio, opcionTipoDeCambio[0]);

            if (MenuDivisas == null) {
                // Cancelar si se cierra el cuadro de diálogo
                cancelacion.cancelar();
                break;
            }

            // Establecer la búsqueda y la divisa basado en la selección del usuario
            if (MenuDivisas.equals("De pesos a Dolar")) {
                busqueda = "MXN/USD/";
                divisa = " Dolares";
            } else if (MenuDivisas.equals("De pesos a Euro")) {
                busqueda = "MXN/EUR/";
                divisa = " Euros";
            } else if (MenuDivisas.equals("De pesos a Libras Esterlinas")) {
                busqueda = "MXN/GBP/";
                divisa = " Libras Esterlinas";
            } else if (MenuDivisas.equals("De pesos a Yen")) {
                busqueda = "MXN/JPY/";
                divisa = " Yenes";
            } else if (MenuDivisas.equals("De pesos a Won Coreano")) {
                busqueda = "MXN/KRW/";
                divisa = " Wones";
            } else if (MenuDivisas.equals("De Dolar a pesos")) {
                busqueda = "USD/MXN/";
                divisa = " pesos";
            } else if (MenuDivisas.equals("De Euro a pesos")) {
                busqueda = "EUR/MXN/";
                divisa = " pesos";
            } else if (MenuDivisas.equals("De Libras Esterlinas a pesos")) {
                busqueda = "GBP/MXN/";
                divisa = " pesos";
            } else if (MenuDivisas.equals("De Yen a pesos")) {
                busqueda = "JPY/MXN/";
                divisa = " pesos";
            } else if (MenuDivisas.equals("De Won Coreano a pesos")) {
                busqueda = "KRW/MXN/";
                divisa = " pesos";
            }

            boolean inputValue = false;

            // Bucle para solicitar un número válido y realizar la conversión
            while (!inputValue) {
                input = JOptionPane.showInputDialog("Ingresa un numero entero");
                if (input == null) {
                    // Cancelar si se cierra el cuadro de diálogo
                    cancelacion.cancelar();
                    break;
                }
                try {
                    // Convertir la entrada a un número entero
                    numero = Integer.parseInt(input);
                    inputValue = true;

                    // Construir la URL de consulta para obtener la tasa de cambio
                    String consulta = "https://v6.exchangerate-api.com/v6/154c417bb0c901a0f0d5d2cb/pair/" + busqueda + numero;

                    // Realizar la solicitud HTTP para obtener la respuesta JSON
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(consulta))
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // Analizar la respuesta JSON utilizando Gson
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                            .create();

                    TypeToken<Map<String, String>> mapType = new TypeToken<Map<String, String>>() {};

                    String json = response.body();
                    Map<String, String> stringMap = gson.fromJson(json, mapType);
                    String conversionResultado = stringMap.get("conversion_result");

                    // Mostrar el resultado de la conversión al usuario
                    JOptionPane.showMessageDialog(null, "Usted tiene $" + conversionResultado + divisa, null,
                            JOptionPane.INFORMATION_MESSAGE, null);

                    // Preguntar al usuario si desea continuar
                    int opcion = JOptionPane.showConfirmDialog(null, "¿Desea continuar?", "Confirmacion",
                            JOptionPane.YES_NO_CANCEL_OPTION);

                    if (opcion == JOptionPane.NO_OPTION || opcion == JOptionPane.CANCEL_OPTION) {
                        // Cancelar si el usuario elige no continuar
                        cancelacion.cancelar();
                    }

                } catch (NumberFormatException e) {
                    // Mostrar mensaje de error si la entrada no es un número válido
                    JOptionPane.showMessageDialog(null, "Error: ingrese un valor valido");
                }
            }
        }
    }
}





