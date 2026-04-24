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
