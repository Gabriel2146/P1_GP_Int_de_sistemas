# ExamenP1Integracion

Proyecto Java con Apache Camel para integrar archivos CSV de admisiones medicas usando un patron File Transfer.

## Requisitos
- Windows PowerShell
- Java instalado (recomendado Java 21 o superior)
- Dependencias de Maven descargadas en el repositorio local (`.m2`)

Nota: en este entorno no hay comando `mvn` en PATH. Por eso se incluye ejecucion directa con Java.

## Estructura de carpetas
- `data/input`: entrada de archivos CSV
- `data/output`: salida de archivos validos
- `data/archive`: historico de validos con timestamp
- `data/error`: archivos invalidos

## Formato esperado del CSV
Cabecera exacta:

```csv
patient_id,full_name,appointment_date,insurance_code
```

Reglas por fila:
- 4 columnas obligatorias
- sin campos vacios
- `appointment_date` con formato `YYYY-MM-DD`
- `insurance_code` permitido: `IESS`, `PRIVADO`, `NINGUNO`

## Como ejecutar (sin Maven en PATH)
Desde la raiz del proyecto, ejecutar en PowerShell:

```powershell
$java = 'C:\Users\USUARIO\.jdks\openjdk-23.0.1\bin\java.exe'; $cp = @('target\classes') + (Get-ChildItem -Path $env:USERPROFILE\.m2\repository\org\apache\camel -Filter *.jar -Recurse | ForEach-Object FullName) + (Get-ChildItem -Path $env:USERPROFILE\.m2\repository\org\slf4j -Filter *.jar -Recurse -ErrorAction SilentlyContinue | ForEach-Object FullName); & $java '-cp' ($cp -join ';') 'org.example.MainApp'
```

## Como ejecutar (si Maven esta instalado)

```powershell
mvn compile exec:java -Dexec.mainClass=org.example.MainApp
```

## Prueba rapida
1. Coloca un CSV de prueba en `data/input`.
2. Observa la consola.
3. Revisa resultados:
- valido: `data/output` y copia en `data/archive`
- invalido: `data/error`


