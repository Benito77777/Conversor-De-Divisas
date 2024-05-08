Conversor de divisas

Es el primer desafío Back-End desarrollado en Java del programa ONE (Oracle Next Education)

Este proyecto permite al usuario ingresar un valor para hacer una conversión de una divisa a otra, estas son las divisas admitidas: 

De pesos(MXN) a Dolar De pesos(MXN) a Euro De pesos(MXN) a Libras Esterlinas De pesos(MXN ) a Yen De pesos(MXN) a Won Coreano

Y viceversa: De Dólar a pesos(MXN) De Euro a pesos(MXN) De Libras Esterlinas a pesos(MXN) De Yen a pesos(MXN) De Won Coreano a pesos(MXN)

Lo primero que se encuentra al ejecutar el programa es la selección de conversores (En este caso solo está habilitado el conversor de divisas) luego de eso el programa le pedirá que elija la conversión de divisas que desea obtener, una vez elejido pedirá que ingrese la cantidad(pesos, dolares, euros, etc) para poder hacer la conversión, en caso de ingresar una letra o algún símbolo le saltara un mensaje con la leyenda "Error: ingrese un valor válido", no pasará de ahí hasta que se ingrese un numero admitido, una vez ingresador se mostrara el resultado de su conversión, al final se abrirá una interfaz para continuar o salir del programa, también es importante saber que si se pulsa cancelar en cualquier momento el programa se dará por finalizado.

Se utiliza el consumo de una API para las tasas de cambio, haciendo uso del protocolo HTTP para acceder al API y obtener los datos de la conversión y con ayuda de obtener API Gson para convertir los datos JSON que se recibieron a objetos en Java y de esta forma poder manipular los datos, y para la interfaz se utiliza la clase JOpcionPane de la biblioteca javax.swing.
