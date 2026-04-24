# PARTE 1. Comprension y analisis del caso

## 1. Problema de integracion
La clinica necesita integrar informacion de admisiones entre sistemas que no se conectan en tiempo real. El problema principal es recibir datos de forma confiable, validarlos y separar registros validos de invalidos sin perder trazabilidad.

## 2. Justificacion del estilo
El estilo File Transfer es razonable porque desacopla sistemas, funciona por lotes, reduce dependencia de conectividad continua y permite una operacion simple por carpetas.

## 3. Riesgos y limitaciones
1. Mayor latencia frente a APIs en tiempo real.
2. Menor granularidad de errores por transaccion.
3. Mayor riesgo de duplicados o reprocesamiento si no se controla bien el archivo fuente.
4. Menor trazabilidad nativa si no se agregan controles adicionales.
5. Seguridad mas manual (permisos, cifrado y auditoria).

# PARTE 2. DiseÃ±o de la solucion File Transfer

## 1. Carpetas involucradas
- /data/input
- /data/output
- /data/archive
- /data/error

## 2. Flujo del archivo
1. El CSV llega a /data/input.
2. Camel lo consume desde file:data/input.
3. Se valida el contenido.
4. Si es valido: se envia a /data/output y se archiva en /data/archive con timestamp.
5. Si es invalido: se envia a /data/error.

## 3. Validaciones
- Cabecera exacta: patient_id,full_name,appointment_date,insurance_code
- Cada fila debe tener 4 columnas.
- No se permiten campos vacios.
- appointment_date con formato YYYY-MM-DD.
- insurance_code permitido: IESS, PRIVADO, NINGUNO.

## 4. Criterio valido/invalido
- Valido: cumple todas las reglas.
- Invalido: incumple al menos una regla.

## 5. Trazabilidad y reproceso
- Logs de inicio, resultado y fin por archivo.
- Archivado de validos con timestamp.
- Prevencion de reproceso con delete=true al consumir input.

# PARTE 3. Implementacion con Apache Camel

## Resumen de implementacion

### Arquitectura
- MainApp: inicia Camel.
- FileIntegrationRoute: define el flujo.
- ValidatorProcessor: valida CSV.

### Flujo principal
- Lee CSV desde data/input con file: y delete=true.
- Valida contenido.
- Valido -> data/output + copia en data/archive con timestamp.
- Invalido -> data/error.

### Reglas clave
- Uso de file: en entrada y salida.
- Enrutamiento con choice / when / otherwise.
- Header isValidCsv controla el flujo.
- Logs de inicio, resultado y fin.

### Evidencia tecnica
- FileIntegrationRoute extiende RouteBuilder.
- Se usa from("file:data/input?include=.*\\.csv&delete=true").
- Se usa to("file:data/output"), to("file:data/archive?...timestamp...") y to("file:data/error").
- ValidatorProcessor valida cabecera, columnas, vacios, fecha e insurance_code.

# PARTE 4. Evolucion futura del proceso mediante API

## 4.1 Propuesta de mejora
La mejora recomendada es mover la recepcion de pre-registros a una API REST para reducir latencia, mejorar seguridad y tener trazabilidad por solicitud. El esquema con archivos puede mantenerse para lotes o contingencia.

## 4.2 Diseno de API

Recurso principal:
- pre-registros

Endpoints:
1. POST /api/v1/pre-registros
- Metodo: POST
- Request JSON: patient_id, full_name, appointment_date, insurance_code
- Response 201: id, mensaje, estado
- Errores: 400, 409, 500

2. GET /api/v1/pre-registros/{id}
- Metodo: GET
- Response 200: detalle del pre-registro
- Errores: 404, 500

## 4.3 Contrato OpenAPI (parcial)

```yaml
openapi: 3.0.0
info:
	title: API de Integracion - Clinica SaludVital
	version: 1.0.0
servers:
	- url: http://localhost:8080/api/v1
paths:
	/pre-registros:
		post:
			summary: Registrar pre-registro
			responses:
				'201': { description: Creado }
				'400': { description: Solicitud invalida }
	/pre-registros/{id}:
		get:
			summary: Consultar pre-registro
			responses:
				'200': { description: OK }
				'404': { description: No encontrado }
```

