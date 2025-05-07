pipeline {
    agent any

    stages {
        stage('Preparar carpetas') {
            steps {
                bat 'mkdir out'
            }
        }

        stage('Compilar') {
            steps {
                bat 'javac -cp "librerias/*" -d out src/**/*.java'
            }
        }

        stage('Empaquetar') {
            steps {
                bat 'jar cfe app.jar sistemaventa.SistemaVenta -C out . -C librerias .'
            }
        }

        stage('Ejecutar') {
            steps {
                bat 'java -cp "app.jar;librerias/*" sistemaventa.SistemaVenta'
            }
        }

        stage('Finalizado') {
            steps {
                echo 'Build completo.'
            }
        }
    }
}
