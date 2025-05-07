pipeline {
    agent any

    environment {
        ANT_HOME = '/path/to/ant'  // Asegúrate de que la ruta sea correcta si usas una instalación de Ant en Jenkins
        PATH = "${env.ANT_HOME}/bin:${env.PATH}"  // Incluye Ant en el PATH
    }

    stages {
        stage('Preparar directorio') {
            steps {
                // Limpiar el directorio de trabajo antes de comenzar
                deleteDir()
            }
        }

        stage('Compilar') {
            steps {
                script {
                    // Ejecutar Ant para compilar el proyecto
                    // Asegúrate de que el archivo build.xml esté en la raíz del proyecto
                    sh 'ant -f build.xml'
                }
            }
        }

        stage('Empaquetar JAR') {
            steps {
                script {
                    // Verifica si el JAR se empaquetó correctamente. Ajusta la ruta según el nombre del archivo generado.
                    sh 'jar tf dist/app.jar'
                }
            }
        }

        stage('Ejecutar') {
            steps {
                // Ejecutar el archivo JAR generado
                sh 'java -jar dist/app.jar'
            }
        }

        stage('Finalizado') {
            steps {
                echo '✅ Compilación y ejecución completadas correctamente.'
            }
        }
    }
}
