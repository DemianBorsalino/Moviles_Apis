README

PARTICIPANTES DEL GRUPO : Cao Gabino, Borsalino Demián

Este trabajo consiste en un buscador de libros a partri de la: api https://openlibrary.org/developers/api
Lo interesante de esta api es que es de libre acceso, es decir, no necesito una key para poder acceder a los requests. En este caso utilizamos la busqueda mediante el nombre del libro.

Nuestra app se basa en un buscador de libros digitales que se encuentran en una biblioteca. Este nos devuelve un json con varios tipos de información como el año que salio, si es prestado o los lenguajes que tiene.
Lamentablemente, no contiene descripción de los libros.

Para usar la App, entrás a la pantalla del home donde tiene el botón para ingresar al buscador. Una vez la segunda pantalla, podés ingresar el nombre de algún libro o algún autor. Esta predeterminado "harry potter".
Luego es posible tocar uno de los libros dentro de la lista para acceder a más información. Esta sería nuestra tercera pantalla.

El endpoint que estamos usando es https://openlibrary.org/search.json?q=, donde luego ingresaremos los datos de búsqueda, con los espacios reemplazados con "+" para que la búsqueda 

Para poder usar este archivo y crear la clase de libro, primero vamos a usar un data class con todos sus atributos utilizando Serializable, que va a buscar los atributos dentro del archivo JSON con cierto nombre
y obtendrá la información para agregarla al constructor de la clase libro. Luego utilizamos Parcelize para poder mover estas clases y se mantengan entre activities. Por ultimo, el mapper que permitirá que tenga
dominio de la clase Book. 

Nuestra aquitectura es de la siguente manera

Activity
  DetailACtivity
  HomeActivity
  MainActivity
  Uistate
Model
  libro
  LibroAdapter
  LibroViewModel
  LibroViewModelFactory
Service
  OpenLibraryAPI
  RepositorioLibros
  RetrofitClient
  SearchResponse


Cosas importantes de nuestro código
Manejo de excepciones httpsExcption que nos da los errores de 400 y 500, Y error de IOException que nos da errores de red.
Autocorrect que logra que si se escriben mal ciertos nombres, los busca como si estuvieran bien escritos.
Uso de Glide que nos permite manejar imagenes de a partir de internet, sin necesidad de mantener esa imagenes en nuestros archivos
