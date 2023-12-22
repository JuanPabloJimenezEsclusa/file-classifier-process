# file classifier model

Proyecto para crear vía API, modelos personalizados de clasificación de archivos (PDF, JPG, PNG) con base en categorías y utilizando el servicio AWS Comprehend

## Arquitectura

```txt
📦 src
 ┣ 📂 main
 ┃ ┗ 📂 java
 ┃   ┣ 📂 org.lucas.classify.model.classifier
 ┃   ┃ ┣ 📜 Config.java
 ┃   ┃ ┣ 📜 DocumentClassifierCreator.java
 ┃   ┃ ┣ 📜 DocumentClassifierDescriptor.java
 ┃   ┃ ┣ 📜 DocumentClassifierReader.java
 ┃   ┃ ┗ 📜 DocumentClassifierCleaner.java
 ┃   ┗ 📂 org.lucas.classify.model.entrypoint
 ┃     ┣ 📜 Config.java
 ┃     ┣ 📜 EndpointCreator.java
 ┃     ┣ 📜 EndpointCleanup.java
 ┃     ┣ 📜 JobCreator.java
 ┃     ┣ 📜 JobDescriptor.java
 ┃     ┗ 📜 JobReader.java
 ┗ 📂 test
   ┗ 📂 java
     ┗ 📂 org.lucas.classify.model.entrypoint
       ┗ 📜 ClassifyDocument.java
```

| Package | Descripción |
|---------| ----------- |
| [classifier](src/main/java/org/lucas/classify/model/classifier/) | Contiene clases para crear un modelo personalizado de clasificación de archivos |
| [entrypoint](src/main/java/org/lucas/classify/model/entrypoint/) | Contiene clases para crear puntos de entrada al modelo de clasificación |


![Training model architecture](./src/main/resources/diagrams/DA-file-process-aws-training-v1.svg "Training DA")

## Servicios AWS

Servicios mínimos para crear, entrenar y evaluar un modelo personalizado de clasificación:

* [S3](https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html)
* [Comprehend](https://docs.aws.amazon.com/comprehend/latest/dg/what-is.html)
