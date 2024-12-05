# Servicio de Moderación de Contenido

Microservicio para la moderación automática de contenido utilizando Azure Content Moderator y Text Analytics.

## Características

- Moderación de texto usando Azure Text Analytics
- Almacenamiento de resultados en PostgreSQL
- API RESTful documentada con Swagger
- Arquitectura basada en estrategias para diferentes tipos de contenido
- Configuración flexible de umbrales de moderación
- Monitoreo mediante Spring Actuator
- Containerización con Docker

## Requisitos Previos

- Java 21
- Maven 3.x
- Docker y Docker Compose
- Cuenta de Azure con servicios de Content Moderator

## Configuración

### Variables de Entorno

```properties
AZURE_ENDPOINT=https://moderationreaccon.cognitiveservices.azure.com/
AZURE_KEY=your-azure-key
```

### Base de Datos

El servicio utiliza PostgreSQL. La configuración se puede ajustar en `application.properties` o mediante variables de entorno.

## Ejecución

### Usando Maven

```bash
cd moderation-service
mvn clean package
java -jar target/moderation-service-0.0.1-SNAPSHOT.jar
```

### Usando Docker Compose

```bash
cd moderation-service
docker-compose up --build
```

## API Endpoints

### Moderación de Contenido

POST `/api/v1/moderation/moderate`
```json
{
  "contentId": "123",
  "content": "Texto a moderar",
  "contentType": "TEXT"
}
```

Respuesta:
```json
{
  "contentId": "123",
  "approved": true,
  "confidenceScore": 0.95,
  "category": "SAFE",
  "reason": "Content is safe"
}
```

### Otros Endpoints

- GET `/api/v1/moderation/history/{contentId}`: Historial de moderación
- GET `/api/v1/moderation/category/{category}`: Moderaciones por categoría
- GET `/api/v1/moderation/type/{contentType}`: Moderaciones por tipo
- GET `/actuator/health`: Estado del servicio

## Documentación

- Swagger UI: http://localhost:8082/swagger-ui.html
- API Docs: http://localhost:8082/api-docs

## Monitoreo

El servicio expone endpoints de Actuator:
- `/actuator/health`: Estado del servicio
- `/actuator/info`: Información del servicio
- `/actuator/metrics`: Métricas del servicio

## Arquitectura

### Componentes Principales

- `ModerationController`: API REST
- `ModerationService`: Lógica de negocio
- `ModerationStrategy`: Interfaz para estrategias de moderación
- `TextModerationStrategy`: Implementación para moderación de texto
- `ModerationResult`: Modelo de datos

### Base de Datos

```sql
CREATE TABLE moderation_results (
    id BIGSERIAL PRIMARY KEY,
    content_id VARCHAR NOT NULL,
    content_type VARCHAR NOT NULL,
    approved BOOLEAN NOT NULL,
    confidence_score DOUBLE PRECISION NOT NULL,
    category VARCHAR NOT NULL,
    reason VARCHAR,
    created_at TIMESTAMP NOT NULL
);
```

## Seguridad

- Validación de entrada
- Límites de tamaño de contenido
- Tipos de contenido permitidos
- Claves de API seguras

## Contribución

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/amazing-feature`)
3. Commit tus cambios (`git commit -m 'Add amazing feature'`)
4. Push a la rama (`git push origin feature/amazing-feature`)
5. Abre un Pull Request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.
# moderation-service
