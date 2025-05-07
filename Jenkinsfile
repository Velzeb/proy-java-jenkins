pipeline {
    agent any
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Limpiar') {
            steps {
                bat 'if exist out rmdir /S /Q out'
                bat 'if exist app.jar del app.jar'
                bat 'mkdir out'
            }
        }

        stage('Compilar') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    bat '''
                    mkdir out
                    
                    REM Compilar primero las clases base y modelos
                    javac -cp ".;punto-de-venta-Java-y-Mysql-main\\librerias\\*" -d out punto-de-venta-Java-y-Mysql-main\\src\\Modelo\\*.java
                    
                    REM Compilar reportes después de los modelos
                    javac -cp ".;punto-de-venta-Java-y-Mysql-main\\librerias\\*;out" -d out punto-de-venta-Java-y-Mysql-main\\src\\Reportes\\*.java
                    
                    REM Compilar las vistas que dependen de modelos y reportes
                    javac -cp ".;punto-de-venta-Java-y-Mysql-main\\librerias\\*;out" -d out punto-de-venta-Java-y-Mysql-main\\src\\Vista\\*.java
                    
                    REM Compilar la clase principal del sistema al final
                    javac -cp ".;punto-de-venta-Java-y-Mysql-main\\librerias\\*;out" -d out punto-de-venta-Java-y-Mysql-main\\src\\sistemaventa\\*.java
                    '''
                }
            }
        }

        stage('Verificar archivos compilados') {
            steps {
                bat 'dir /s /b out > compiled_files.txt'
                script {
                    def files = readFile('compiled_files.txt').trim()
                    if (files.isEmpty()) {
                        error "No se generaron archivos compilados"
                    }
                }
            }
        }

        stage('Verificar clase principal') {
            steps {
                script {
                    def exists = bat(script: 'if exist out\\sistemaventa\\SistemaVenta.class echo CLASS_EXISTS', returnStdout: true).contains('CLASS_EXISTS')
                    if (!exists) {
                        error "ERROR: Clase principal no encontrada."
                    }
                }
            }
        }

        stage('Empaquetar') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    // Copiar las librerías al directorio de salida para incluirlas en el JAR
                    bat 'mkdir out\\lib'
                    bat 'copy punto-de-venta-Java-y-Mysql-main\\librerias\\*.jar out\\lib\\'
                    
                    // Copiar los recursos de imágenes también
                    bat 'mkdir out\\Img'
                    bat 'xcopy /E /I /Y punto-de-venta-Java-y-Mysql-main\\src\\Img out\\Img'
                    
                    // Crear el JAR con la clase principal correcta y un manifest que incluya las librerías
                    bat 'echo Main-Class: sistemaventa.SistemaVenta > manifest.txt'
                    bat 'echo Class-Path: lib/AbsoluteLayout.jar lib/itextpdf-5.5.1.jar lib/jcalendar-1.4.jar lib/jcommon-1.0.23.jar lib/jfreechart-1.0.19.jar lib/mysql-connector-java-8.0.19.jar >> manifest.txt'
                    bat 'jar cfm app.jar manifest.txt -C out . || exit /b 1'
                }
            }
        }

        stage('Verificar JAR') {
            steps {
                bat 'jar tf app.jar > jar_contents.txt'
                script {
                    def jarContents = readFile('jar_contents.txt').trim()
                    if (!jarContents.contains('sistemaventa/SistemaVenta.class')) {
                        error "El JAR no contiene la clase principal"
                    }
                }
            }
        }

        stage('Pruebas') {
            steps {
                // Esta etapa se debe adaptar según tus pruebas
                echo 'Ejecutando pruebas...'
                // Añadir aquí comandos para ejecutar pruebas
            }
        }

        stage('Artefactos') {
            steps {
                // Guardar el JAR como artefacto del build
                archiveArtifacts artifacts: 'app.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Build completado exitosamente.'
        }
        failure {
            echo '❌ El build ha fallado.'
        }
        always {
            echo 'Pipeline finalizado.'
        }
    }
}
