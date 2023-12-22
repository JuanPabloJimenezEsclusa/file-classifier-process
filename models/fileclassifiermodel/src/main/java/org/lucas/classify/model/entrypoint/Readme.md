# Classification

> Contiene código que permite crear un `Endpoint` del modelo para uso síncrono y un `Job` para uso asíncrono

## Detalles

| Archivo | Descripción |
|-------|-------------| 
| Config.java | Configuración de parámetros |
| EndpointCreator.java | Crear un `endpoint` que luego sea invocado de forma sincrónica para clasificar archivos |
| EndpointCleanup.java | Eliminar un `endpoint` |
| JobCreator.java | Crear un `job` para clasificar de forma asíncrona archivos |
| JobDescriptor.java | Consultar el estado de un `job` |
| JobReader.java | Consultar el estado de un `job` |
