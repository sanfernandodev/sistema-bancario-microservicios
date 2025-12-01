#!/bin/bash

# Script de inicio para el Sistema Bancario con Microservicios

set -e

echo "════════════════════════════════════════════════════════════════"
echo "   🏦 Sistema Bancario - Microservicios"
echo "════════════════════════════════════════════════════════════════"
echo ""

# Verificar que Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado"
    exit 1
fi

# Menú de opciones
case "${1:-help}" in
    start)
        echo "🚀 Iniciando servicios..."
        docker-compose up -d
        echo "✅ Servicios iniciados"
        echo ""
        echo "📡 Acceso a servicios:"
        echo "   - Cliente-Persona Service: http://localhost:8080"
        echo "   - Cuenta-Movimiento Service: http://localhost:8081"
        echo "   - RabbitMQ Management: http://localhost:15672"
        echo ""
        echo "⏳ Esperando 30 segundos para que se inicialicen..."
        sleep 30
        echo "✅ Los servicios deberían estar listos"
        ;;
    
    stop)
        echo "🛑 Deteniendo servicios..."
        docker-compose down
        echo "✅ Servicios detenidos"
        ;;
    
    restart)
        echo "🔄 Reiniciando servicios..."
        docker-compose down
        docker-compose up -d
        echo "✅ Servicios reiniciados"
        sleep 20
        ;;
    
    logs)
        echo "📋 Mostrando logs..."
        docker-compose logs -f
        ;;
    
    logs-cliente)
        echo "📋 Logs de Cliente-Persona Service..."
        docker-compose logs -f cliente-persona-service
        ;;
    
    logs-cuenta)
        echo "📋 Logs de Cuenta-Movimiento Service..."
        docker-compose logs -f cuenta-movimiento-service
        ;;
    
    clean)
        echo "🧹 Limpiando contenedores y volúmenes..."
        docker-compose down -v
        echo "✅ Limpieza completada"
        ;;
    
    build)
        echo "🔨 Compilando microservicios..."
        
        echo "   Compilando cliente-persona-service..."
        cd cliente-persona-service
        mvn clean package -DskipTests
        cd ..
        
        echo "   Compilando cuenta-movimiento-service..."
        cd cuenta-movimiento-service
        mvn clean package -DskipTests
        cd ..
        
        echo "✅ Compilación completada"
        ;;
    
    test)
        echo "🧪 Ejecutando pruebas..."
        
        echo "   Pruebas en cliente-persona-service..."
        cd cliente-persona-service
        mvn test
        cd ..
        
        echo "   Pruebas en cuenta-movimiento-service..."
        cd cuenta-movimiento-service
        mvn test
        cd ..
        
        echo "✅ Pruebas completadas"
        ;;
    
    status)
        echo "📊 Estado de los contenedores:"
        docker-compose ps
        ;;
    
    help|*)
        echo "Comandos disponibles:"
        echo ""
        echo "  ./startup.sh start          - Inicia todos los servicios"
        echo "  ./startup.sh stop           - Detiene todos los servicios"
        echo "  ./startup.sh restart        - Reinicia todos los servicios"
        echo "  ./startup.sh logs           - Muestra logs de todos los servicios"
        echo "  ./startup.sh logs-cliente   - Muestra logs del servicio de clientes"
        echo "  ./startup.sh logs-cuenta    - Muestra logs del servicio de cuentas"
        echo "  ./startup.sh clean          - Limpia contenedores y volúmenes"
        echo "  ./startup.sh build          - Compila los microservicios"
        echo "  ./startup.sh test           - Ejecuta las pruebas"
        echo "  ./startup.sh status         - Muestra estado de los contenedores"
        echo "  ./startup.sh help           - Muestra esta ayuda"
        echo ""
        ;;
esac
