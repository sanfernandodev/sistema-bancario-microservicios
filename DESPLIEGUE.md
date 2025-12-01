# 📦 Guía de Despliegue

## Despliegue Local con Docker Compose

### Requisitos

- Docker 20.10+ 
- Docker Compose 2.0+
- 2GB RAM disponible (mínimo)
- Puertos 3306, 5672, 8080, 8081 disponibles

### Paso 1: Clonar el repositorio

```bash
git clone <repository-url>
cd banco-microservicios
```

### Paso 2: Compilar (Opcional - Docker lo hace automáticamente)

```bash
./startup.sh build
```

O manualmente:

```bash
cd cliente-persona-service && mvn clean package && cd ..
cd cuenta-movimiento-service && mvn clean package && cd ..
```

### Paso 3: Iniciar con Docker Compose

```bash
# Opción 1: Usando el script
./startup.sh start

# Opción 2: Directamente con Docker
docker-compose up -d
```

### Paso 4: Verificar que los servicios están corriendo

```bash
# Verificar contenedores
docker-compose ps

# Verificar logs
docker-compose logs -f
```

### Paso 5: Validar funcionamiento

```bash
# Cliente-Persona Service
curl http://localhost:8080/api/clientes

# Cuenta-Movimiento Service
curl http://localhost:8081/api/cuentas

# RabbitMQ Management
open http://localhost:15672  # usuario: guest, password: guest
```

## Estructura de Contenedores

```
banco-network (bridge)
├── mysql:8.0 (puerto 3306)
│   └── Base de datos: banco_sistema
├── rabbitmq:3.12-management (puerto 5672, 15672)
│   └── Broker de mensajes
├── cliente-persona-service (puerto 8080)
│   └── Spring Boot App
└── cuenta-movimiento-service (puerto 8081)
    └── Spring Boot App
```

## Configuración de Volúmenes

```yaml
mysql_data:       # Datos persistentes de MySQL
  - Location: /var/lib/docker/volumes/proyecto-devsu_mysql_data

rabbitmq_data:    # Datos de RabbitMQ
  - Location: /var/lib/docker/volumes/proyecto-devsu_rabbitmq_data
```

## Variables de Entorno

### Cliente-Persona Service
```properties
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/banco_sistema
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
SPRING_RABBITMQ_HOST=rabbitmq
SERVER_PORT=8080
```

### Cuenta-Movimiento Service
```properties
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/banco_sistema
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
SPRING_RABBITMQ_HOST=rabbitmq
SERVER_PORT=8081
```

## Comandos Útiles

### Gestión de Servicios

```bash
# Iniciar
docker-compose up -d

# Parar
docker-compose down

# Parar y eliminar volúmenes (CUIDADO: elimina BD)
docker-compose down -v

# Reiniciar
docker-compose restart

# Reconstruir imágenes
docker-compose build --no-cache
```

### Logs

```bash
# Ver todos los logs
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f cliente-persona-service
docker-compose logs -f mysql

# Últimas N líneas
docker-compose logs --tail=100 -f
```

### Base de Datos

```bash
# Acceder a MySQL
docker-compose exec mysql mysql -uroot -proot banco_sistema

# Ejecutar consultas
docker-compose exec mysql mysql -uroot -proot banco_sistema -e "SELECT * FROM cliente;"

# Hacer backup
docker-compose exec mysql mysqldump -uroot -proot banco_sistema > backup.sql

# Restaurar backup
docker-compose exec -T mysql mysql -uroot -proot banco_sistema < backup.sql
```

### RabbitMQ

```bash
# Acceder al Management Console
# URL: http://localhost:15672
# Usuario: guest
# Contraseña: guest

# Verificar estado
docker-compose exec rabbitmq rabbitmq-diagnostics -q ping
```

## Despliegue en Producción (Recomendaciones)

### Docker Swarm

```bash
# Inicializar swarm
docker swarm init

# Desplegar stack
docker stack deploy -c docker-compose.yml banco

# Ver servicios
docker service ls

# Ver logs
docker service logs banco_cliente-persona-service
```

### Kubernetes

Crear manifiestos YAML equivalentes:

```yaml
---
apiVersion: v1
kind: Service
metadata:
  name: cliente-persona-service
spec:
  selector:
    app: cliente-persona
  ports:
    - port: 8080
      targetPort: 8080
  type: LoadBalancer
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cliente-persona-deployment
spec:
  replicas: 2
  selector:
    matchLabels:
      app: cliente-persona
  template:
    metadata:
      labels:
        app: cliente-persona
    spec:
      containers:
      - name: cliente-persona
        image: cliente-persona:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:mysql://mysql:3306/banco_sistema
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
```

## Monitoreo

### Verificar salud de servicios

```bash
# Cliente-Persona
curl -s http://localhost:8080/actuator/health | jq .

# Cuenta-Movimiento
curl -s http://localhost:8081/actuator/health | jq .
```

### Métricas

```bash
# Cliente-Persona
curl -s http://localhost:8080/actuator/metrics | jq .

# Cuenta-Movimiento
curl -s http://localhost:8081/actuator/metrics | jq .
```

## Backup y Recuperación

### Backup completo

```bash
# Backup de BD
docker-compose exec mysql mysqldump -uroot -proot --all-databases > backup_completo.sql

# Backup de volúmenes
docker cp banco-mysql:/var/lib/mysql ./mysql_backup
```

### Restauración

```bash
# Restaurar BD
docker-compose exec -T mysql mysql -uroot -proot < backup_completo.sql

# Restaurar volúmenes
docker cp ./mysql_backup banco-mysql:/var/lib/mysql
```

## Troubleshooting

### Servicio no responde

```bash
# Verificar si el contenedor está corriendo
docker-compose ps

# Ver logs de error
docker-compose logs --tail=50 cliente-persona-service

# Reiniciar contenedor
docker-compose restart cliente-persona-service
```

### Problemas de conexión a BD

```bash
# Verificar conexión a MySQL
docker-compose exec mysql mysql -uroot -proot -e "SELECT 1"

# Ver configuración de red
docker network inspect proyecto-devsu_banco-network
```

### Puerto en uso

```bash
# Encontrar proceso usando puerto
lsof -i :8080

# Cambiar puerto en docker-compose.yml
# Modificar: "8080:8080" a "8090:8080"
```

## Performance Tuning

### MySQL

En `docker-compose.yml`:

```yaml
services:
  mysql:
    command: --max_connections=1000 --max_allowed_packet=256M
    environment:
      MYSQL_INNODB_BUFFER_POOL_SIZE: 1G
```

### Spring Boot

En `application.properties`:

```properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.jdbc.fetch_size=50
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

## Escalado

### Escalas horizontales

```bash
# Aumentar réplicas (con Swarm)
docker service scale banco_cliente-persona-service=3

# Con Docker Compose (crear nuevo compose con múltiples instancias)
```

## Seguridad en Producción

- [ ] Cambiar credenciales de MySQL
- [ ] Cambiar credenciales de RabbitMQ
- [ ] Usar HTTPS/TLS
- [ ] Configurar firewall
- [ ] Implementar Spring Security
- [ ] Usar secretos en lugar de variables de entorno
- [ ] Actualizar imágenes base regularmente
- [ ] Implementar rate limiting
- [ ] Habilitar logging y monitoreo

---

**Última actualización**: Diciembre 2025
