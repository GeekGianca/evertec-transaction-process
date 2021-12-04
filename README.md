# Evertec prueba
## Procesos de transaccion sobre

![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/ptp.png?raw=true)

Como usuario final, descargas una aplicación en la que puedes generar pagos para un comercio x de diferentes artículos con diferentes valores. Para generar un pago, debes proporcionar a la aplicación nombre, dirección de correo electrónico y número de celular, además (teniendo en cuenta que el medio de pago de la app es tarjeta de crédito) debes ingresar el número de la tarjeta, la fecha de vencimiento y el código de seguridad.
Una vez procedes a realizar el pago, debes visualizar un resumen con la información del pago realizado y el estado de la transacción. Además, tienes una opción donde puedes visualizar los pagos que has realizado con la información más relevante como referencia, monto, datos de la tarjeta utilizada y estado de la transacción, y puedes eliminar la visualización de cualquiera de ellos cuando así lo quieras.
La aplicación utiliza la pasarela de pagos Placetopay para el procesamiento de la transacción a través de una integración con REST.
Los pasos que son necesarios para generar un pago a través de este medio son los siguientes:

1. Hacer la solicitud de pago al servicio Procesamiento de transacción (processTransaction) con los datos requeridos
2. Una vez se realice el pago y vea el resumen, se pueden presentar 3 casos:
   a. El pago fue realizado
   b. El pago se encuentra pendiente
   c. El pago fue rechazado

## Requerimientos

Debes crear una aplicación que contenga las siguientes vistas:
1. Debe usar Kotlin como lenguaje.
2. Debe usar Fragments
3. Debe tener una vista para generar pago
4. Una vista donde se visualicen los pagos
5. Debe tener una vista donde se pueda obtener todos los datos para realizar el pago
6. Una vista con el resumen de la transacción
7. Debe tener una vista con la lista de los últimos pagos generados (usar RecyclerView)

De la prueba se evaluarán los siguientes ítems:
1. Arquitectura de la app
2. Uso de patrones de diseño
3. Consumo de servicios web
4. Inyección de dependencias
5. Uso de mejores prácticas de codificación
6. Buen manejo de control de versiones (Git)
7.
URL Base del servicio:
https://dev.placetopay.com/rest/

## Arquitectura
La arquitectura de la aplicacion esta bajo MVVM y MVI, arquitecturas que proporcionan un manejo de estado de eventos sobre la aplicacion, mantenible y con control sobre los errores del app.
La arquitectura tambien tiene Clean Architecture y bases de SOLID.

### Componente CORE
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/core.png?raw=true)
Contiene las clases utilizadas dentro de todo el proyecto, esto implica, componentes de acciones, interfaces de ejecucion, objetos comunes, manejo de datos, y clases genericas.

### Componente DATA
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/data.png?raw=true)
Contiene la configuracion de la Inyeccion de dependencias, la administracion de la base de datos local, el mapeo de objetos, y los repositorios utilizados en el proyecto.

### Componente DOMAIN
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/domain.png?raw=true)
Contiene el o los clientes que se usan dentro del proyecto, los modoles pueden ser generalmente modulos, pero para efectos de prueba se configuraron como clases normales(omitan el request y response folder).
Los casos de uso que en este caso son las interfaces que implementan los repositorios.

### Componente PRESENTATION
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/presentation.png?raw=true)
Contiene las vistas del proyecto, todas las partes visuales y los manejos de estados dentro del proyecto, adaptadores y demas.

## Patrones de diseño utilizados
- Singleton
- Adapter
- Builder
- Entre otros

## Consumo de servicios WEB
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/web_consume.png?raw=true)
El consumo de servicios WEB se hizo atraves de Retrofit, utilizando corrutinas y clases que permiten obtener los estados y ejecutar procesos de manera segura.

## Inyeccion de dependencias
Para la inyeccion de dependencias se ejecuto con Dagger + Hilt, una libreria muy potente que facilita la usabilidad dentro de proyectos Android y la da mayor escalabilidad al codigo.

## Manejo de codigo
Dentro del proyecto, y su revision, se daran cuenta que el control de objetos, flujo de datos, y manejo de la informacion, estan completamente organizados, de manera que pueden controlar los errores y agregar nuevas funcionalidades sin mucha complejidad.


# Ejecucion y pruebas
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/1.png?raw=true)
Aunque la vista inicial es un dummy de un resumen de compra, debe quedar claro, que la informacion obtenida en esa vista esta guardada localmente y se obtienen.
Los botones de agregar mas y quitar son funcionales, y se actualizan localmente.
El boton de cambiar opcion de entrega pasa lo mismo, cambia de manera aleatoria y altera el costo final de la transaccion.
El medoto de pago muestra la opcion actual de la tarjeta seleccionada, en caso que no exista un metodo de pago se puede agregar uno nuevo y mostrara la tarejta seleccionada.
Las cuotas de la tarjeta se actualizan local y se obtienen del servicio rest de la ruta de placetopay.
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/2.png?raw=true)
En caso que las opciones de pago no esten configuradas correctamente al darle pagar mostrara un Toast que dice que debe verificar la transaccion y que lo intente nuevamente.
#Vista tarjetas agregadas
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/3.png?raw=true)
Muestra una tarjeta vacia y una opcion de agregar una nueva tarjeta.
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/4.png?raw=true)
Al darle agregar podemos ingresar todos los campos, que estan validados para efectos de crear una tarjeta con datos, y al tenerlos todos configurados correctamente, nos devolvera a la vista de tarjetas con la nueva tarjeta creada.
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/5.png?raw=true)
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/6.png?raw=true)
Al seleccionarla nos mostrara los datos de la tarjeta en la tarjeta de muestra.
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/7.png?raw=true)
Esta tarjeta seleccionada es la que se muestra en la vista de checkout.
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/8.png?raw=true)
Al seleccionar todos los campos en el checkout y procedemos a pagar, nos mostrara el resumen de la transaccion con su estado y los datos.
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/9.png?raw=true)
Al darle a la X nos llevara a la vista principal, la llamo principal porque en un proyecto completo la vistade checkout seria la ultima en todo el proceso, pero en este caso intento mostrarles el flujo por cuestiones de tiempo desde dicha vista.
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/10.png?raw=true)
En esta vista con un BottomNavigation, muestro las listas de tarjetas y las transacciones realizadas.
![alt text](https://github.com/GeekGianca/evertec-transaction-process/blob/main/doc/11.png?raw=true)
Si seleccionamos una de las transacciones de la lista, nos llevara al resumen anterior.
La idea en este punto era consumirlo desde el web service y actualizar la operacion localmente, luego por cuestiones de tiempo no lo hice.
Cabe resaltar que todas las rutas estan bajo el patron de guardado local y consumo, que notaran dentro del proyecto.

Dev: Gian Carlos Cuello
