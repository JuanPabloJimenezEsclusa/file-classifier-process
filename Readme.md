# AWS file classifier process

Proyecto para clasificar archivos por su contenido y extraer sus datos de forma síncrona utilizando servicios de AWS y procesos `serveless`

## Requisitos

* JDK 17/21
* Linux
* AWS CLI, AWS SAM CLI 
* AWS S3, Lambda, Comprehend, Textract

## Contenido

| Carpeta | Descripción |
|---------|-------------|
| [models](./models/fileclassifiermodel)| Contiene proyectos para crear modelos de clasificación personalizados |
| [functions](./functions/classifydocument)| Contiene proyectos para clasificar archivos utilizando un modelo |

## Domain Storytelling

![StoryTelling](./File-process-classifier-v1.dst.svg)

* Contexto:

Un sistema de manejo de documentos, enfrenta el desafío de manejar archivos en formatos variados como PDF, PNG y JPG. La tarea es implementar un proceso automatizado que clasifique cada archivo en categorías predefinidas y extraiga datos clave para su posterior almacenamiento en una base de datos.
 
* Desarrollo:

*La Recepción de Archivos.* Periódicamente, se reciben archivos de diferentes tipos, cada uno con su propio formato y contenido único.

*La Clasificación Automática.* Se implementa un "Clasificador Automático" que utiliza algoritmos para asignar a cada archivo una categoría específica. Este proceso ayuda a organizar y entender mejor el contenido de los archivos.

*La Extracción de Datos Relevantes.* Luego de la clasificación, un "Extractor de Datos" se encarga de analizar cada archivo en busca de información clave. Esto puede incluir palabras claves.

*La Integración en la Base de Datos.* Los resultados de la clasificación y los datos extraídos se almacenan de manera en una "Base de Datos". Este repositorio central permite acceder y utilizar los datos para evaluar el proceso.

* Conclusión:

Este proceso automatizado de clasificación y extracción de datos simplifica la gestión de archivos, permitiendo un acceso rápido y estructurado a la información clave. 

## Restricciones y limitaciones de la solución

- Solo se puede entrenar una página por archivo
- La clasificación sincrónica solo permite evaluar archivos de una página y la cantidad de archivos a la vez es muy limitado
- El procesamiento en lote requiere un trabajo adicional para extraer el resultado del proceso de cada archivo
- Se requiere una resolución de mínima de 150 DPI para procesar imágenes
- La cantidad de texto a extraer es limitada
- Las búsquedas de texto no reconocen acentos 
- Los endpoint que permiten la clasificación sincrónica tienen un costo asociado por hora aunque no se estén utilizando 