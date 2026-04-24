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

# PARTE 2. Diseno de la solucion File Transfer

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
