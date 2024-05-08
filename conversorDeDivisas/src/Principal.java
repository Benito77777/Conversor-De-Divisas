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
        Conversor de divisas

        Es el primer desafío Back-End desarrollado en Java del programa ONE (Oracle Next Education)

        Este proyecto permite al usuario ingresar un valor para hacer una conversión de una divisa a otra, estas son las divisas admitidas: De pesos(MXN) a Dolar De pesos(MXN) a Euro De pesos(MXN) a Libras Esterlinas De pesos(MXN ) a Yen De pesos(MXN) a Won Coreano

        Y viceversa: De Dólar a pesos(MXN) De Euro a pesos(MXN) De Libras Esterlinas a pesos(MXN) De Yen a pesos(MXN) De Won Coreano a pesos(MXN)

                Lo primero que se encuentra al ejecutar el programa es la selección de conversores (En este caso solo está habilitado el conversor de divisas) luego de eso el programa le pedirá que elija la conversión de divisas que desea obtener, una vez elejido pedirá que ingrese la cantidad(pesos, dolares, euros, etc) para poder hacer la conversión, en caso de ingresar una letra o algún símbolo le saltara un mensaje con la leyenda "Error: ingrese un valor válido", no pasará de ahí hasta que se ingrese un numero admitido, una vez ingresador se mostrara el resultado de su conversión, al final se abrirá una interfaz para continuar o salir del programa, también es importante saber que si se pulsa cancelar en cualquier momento el programa se dará por finalizado.

                Se utiliza el consumo de una API para las tasas de cambio, haciendo uso del protocolo HTTP para acceder al API y obtener los datos de la conversión y con ayuda de obtener API Gson para convertir los datos JSON que se recibieron a objetos en Java y de esta forma poder manipular los datos, y para la interfaz se utiliza la clase JOpcionPane de la biblioteca javax.swing.
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





