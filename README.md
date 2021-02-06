# Prueba Android Studio - Agenda con Login
***
Creadores:
- Esteban Ríos
- Marvin Zambrano

Herramientas utilizadas:
- Android Studio
- Cloud Firestore / Firebase
- Authentication Firebase

El proyecto debe constar de:
- Inicio de sesión
- Registro de usuario
- Cerrar sesión
- Ingresar tarea
- Actualizar tarea
- Eliminar tarea

Video de Manual Técnico y de Uso en el siguiente enlace:

## Desarrollo de la aplicación
***
- Interfaces de la Aplicación.

- Explicación de los metodos implementados
  
  - Librerías de uso  
  
    ![Librerías](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Librerias_Agenda.JPG):  
    
    El archivo muestra todas las dependencias necesarias para realizar el *CRUD* respectivo. Primero se llaman a los *widgets* que son elementos de la interfaz con la que se interacturan y los más importantes el servicio de *Google* que nos conecta al *Firestore* de *Firebase*. Y un elemento de java que funcionara como *array* para almacenar los datos.
    
  - Variables de la Agenda  
  
    ![Viariables](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Variables_Agenda.JPG)  
    
    Las variables nacen a partir de la llamada de las liberias de la imagen anterior, y estás ayudan a interactuar ya sea para enviar o capturar datos con los botones, cajas de textos y elementos externos como el *Firestore*.
    
## Inicio de Sesión y Registro
***
  - Metodo Inicial de autenticación  
  
    ![Inicio](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/OnCreate_Autenticación.JPG)  
    
    Después de definir las variables de la clase, siempre hay un proceso inicial que identifica todos los elementos visuales por el *Id* que se definió en el archivo *.xml* y posteriormente llema el proceso setup que almacena las funciones de inicio de sesion y registro.
    
  - Método Iniciar Sesion  
  
    ![Login](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Metodo_IniciarSesion.JPG)  
    
    Desde un botón de inicio de sesión que tiene un metodo OnClickListener que escucha y envía una petición cada que se interactúa con el mismo. El cual contiene un método específico OnClick que toma un View o vista y que utiliza 2 variables que primero validan que los campos (text) esten llenos y que en la interfaz ya se controla que el tipo de texto que se ingresa. Posteriormente se llama al método de FirebaseAuth que se los conoce como _singInWithEmailAndPassword_ y donde van los 2 parámetros que se tomaron inicialmente en las variales email y password respectivamente.  
    
    Finalizado este proceso se emite un método _OnComplete_ el cual toma una valor booleano del inicio de sesión y si es verdadero, captura la variable correo que tomara el email de la persona que inicio sesión o se registra y nos redirige a la pantalla de la Agenda de tareas. si no se cumple le proceso se lanza una alerta.
    
  - Método Registro  
  
    ![Registro](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Metodo_Registrar.JPG)  
    
    Desde un proceso privado y void, se inicia cuando se interactua con el botón registro el cua aloja un método OnClick donde se llama a las variables email y password que primero validan que los campos (text) esten llenos y que en la interfaz ya se controla que el tipo de texto que se ingresa. Si el caso se cumple se llama al método externo de *FirebaseAuth* que se los conoce como _createUserEmailAndPassword_ y donde van los 2 parámetros que se tomaron inicialmente en las variales email y password respectivamente.  
    
    Finalizado este proceso se emite un método OnComplete el cual toma una valor booleano del registro y si es verdadero, captuta el Uid del usuario y el correo el cual se envía a la colección de "users", con los datos capturados al momento de registro. En el caso que no se complete el registro se emite una alerta.
    
  - Método de alerta y redirección  
  
    ![Registro](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Metodo_Alerta_y_Redireccion.JPG)  
    
    El primer proceso muestra la caja de texto con el mensaje que no se pudo inicar o autenticar el usuario, se compone de 3 partes (el título, el mesaje y los botones y al finarlizar se lanza el proceso _show()_.  
    
    El segundo, toma como parámetro el email del usuario que inicio sesión y se llama a la función que llama la Clase AgendaActivity.

# Agenda de tareas.
***
  - Método Incial de agenda  
  
    ![Agenda](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/OnCreate_Agenda_Enviar.JPG)  
    
    Después de definir las variables de la clase, siempre hay un proceso inicial que identifica todos los elementos visuales por el Id que se definió en el archivo .xml y posteriormente se obtiene el parametro enviado de la clase anterior, el cual se envía en el método setup que aloja todas las funciones. Y se define el método principal de la clase que escucha cualquier interación con el Botón enviar(send) el que contiene un proceso onClick que aloja el método addTask y que lleva los parametros (tarea, correo, fecha, hora). Al final hay un método getTask y con un parámetro correo, el cual filtrara los mensajes según el usuario logeado.
    
  - Metodos de botones Capturar, actualizar, eliminar y cerrar sesión 
  
  
    ![Botón](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/OnCreate_Agenda_Botones_Capturar_Actualizar_Eliminar.JPG)  
    
    Se define el método que escucha cualquier interación con el Botón tareas(task) el que contiene un proceso onItemClick el cual tomará los valores de la lista de datos que se presentan en la lista(vector o _AdapterView_) de tareas creadas. Toma los parámetros (task - date - time), este proceso los ubicará en los campos respectivos para ser actualizados con el Botón actualizar(update) que lleva el párametro correo(email), para actualizarlo segun el usuario logeado.  
    
    Para el botón Eliminar(delete) se tiene un proceso que escucha cualquier interacción y se llema el método onClick que realiza una llamada a la función _deleteTask()_.  
    
    Para el botón logout(logout) se realiza el mismo proceso hasta llamr a la función onClick, el cual llema el proceso externo de FirebaseAuth con el método signOut(), y el regreso a la pantalla pricipal con el método _onBackPressed()_.
    
   - Método Setup y Agregar tarea  
   
     ![AddTask](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Metodo_Setup_y_Agregar_Tarea.JPG)  
     
     Para el primer método Setup() se le ingresa un titulo y el correo del usuario que esta logeado.  
     
     Para la añadir una tarea (_addTask_) se receptan los parametros definidos inicialmente en el primer método (addTask - email - addDate, addtime). Con los parámetros definidos se crea una instancia Tarea(Task), donde se añaden todos los siguientes datos (UUID - task - email - date - time) y se envian a la colección ("tasks") y con un retorno de Success para pasar al siguiente método que al ser verdadero se limpiaran los campos respectivos.
     
  - Método Obtener Tareas  
  
    ![AddTask](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Metodo_Obtener_Tareas.JPG)  
    
    Ingresando a la colleccion "tasks" con un metodo onEvent que tiene dos parámetros que ingresan los valores de una consulta que hay en la colección. Se limpia la lista de tareas iniciales y mediante un for que recorre la colección y se obtiene los documentos de la colección. y se guardan en un documentos de objetos que se recorrera de uno en uno evaluando si el correo que tiene es igual al que esta en el documento y se añade unicamente los docuemntos que cumplen con la colección. Se instancia un nuevo adaptador para vectores (_ArrayAdapter_) y a la variabe definida se le inserta el ArrayAdapter.
    
  - Actualizar tareas  
  
    ![UpdateTask](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Metodo_Actualizar_Tarea.JPG)  
    
    Primero para identificar la tarea se debe usar el UUID y se accede a la colección "tasks" con un metodo onEvent que tiene dos parámetros que ingresan los valores de una consulta que hay en la colección. Se obtiene los documentos de la colección, y se reccorre cada uno de estos. Con el UUID se hace una comparación con cada documento y al momento de obtener el documento completo se asignan a todos los campos de la interfaz. Se realizan las modificaciones y estas se agregan con el metodo update del campo correspondiente y con el valor se lo obtiene del field o text.  
    
  - Eliminar tareas  
  
    ![DeleteTask](https://github.com/EstebanRios99/prueba-android/blob/master/Capturas/Metodo_Eliminar_Tarea.JPG)  
    
    Primero para identificar la tarea se debe usar el UUID y se accede a la colección "tasks" con un metodo onEvent que tiene dos parámetros que ingresan los valores de una consulta que hay en la colección. Se obtiene los documentos de la colección, y se reccorre cada uno de estos. Con el UUID se hace una comparación con cada documento y al momento de obtener el documento completo se llama al metodo _delete()_ para borrar la coleccion.
    
   Los datos ingresados se muestran en la Apicación Movil y se guardan en el Cloud Firebase.
