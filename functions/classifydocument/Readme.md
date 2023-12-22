# file classifier document

Proyecto con la configuración de la infraestructura como código de un proceso `serveless` para clasificar archivos utilizando un modelo de machine learning de AWS

## Arquitectura

```txt
📦 src
 ┗ 📂 main
   ┗ 📂 java
     ┗ 📂 org.lucas.classify
       ┣ 📂 config
       ┃ ┣ 📜DependencyFactory.java
       ┃ ┗ 📜EnvConfig.java
       ┣ 📂handler
       ┃ ┗ 📜ClassifyFileHandler.java
       ┣ 📂repository
       ┃ ┗ 📜DynamoRepository.java
       ┗ 📂service
         ┣ 📜ComprehendProxy.java
         ┗ 📜TextractProxy.java
```

![Function model architecture](./src/main/resources/diagrams/DA-file-process-aws-v1.svg "Diagrama C1")

## Servicios AWS

Servicios para clasificar archivos y extraer su contenido:

* [S3](https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html)
* [Lambda](https://docs.aws.amazon.com/lambda/latest/dg/getting-started.html)
* [Comprehend](https://docs.aws.amazon.com/comprehend/latest/dg/what-is.html)
* [Textract](https://docs.aws.amazon.com/textract/latest/dg/what-is.html) 
* [DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html) 

## Recursos

| Archivo | Descripción |
|---------|-------------|
|[template.yml](./template.yml)| Plantilla de [AWS CloudFormation](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/Welcome.html) para desplegar la configuración de los servicios a utilizar |
|[deploy.sh](./deploy.sh)| Script para desplegar una plantilla utilizando [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/using-sam-cli.html) |
|[upload.sh](./upload.sh)| Script para cargar archivos para su clasificación y extracción de datos |
|[cleanup.sh](./cleanup.sh) | Script para eliminar una plantilla |
