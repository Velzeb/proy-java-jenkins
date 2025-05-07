pipeline {
    agent any

    stages {
        stage('Preparar') {
            steps {
                bat 'if not exist out mkdir out'
            }
        }

        stage('Compilar') {
            steps {
                bat 'javac -cp ".;librerias\\*" -d out src\\sistemaventa\\*.java'
            }
        }

        stage('Empaquetar') {
            steps {
                bat 'jar cfe app.jar sistemaventa.SistemaVenta -C out .'
            }
        }

        stage('Ejecutar') {
            steps {
                bat 'java -cp "app.jar;librerias\\*" sistemaventa.SistemaVenta'
            }
        }

        stage('Finalizado') {
            steps {
                echo '✅ Build y ejecución completos.'
            }
        }
    }
}
