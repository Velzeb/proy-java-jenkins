pipeline {
    agent any

    stages {
        stage('Preparar') {
            steps {
                bat 'if not exist bin mkdir bin'
            }
        }

        stage('Compilar') {
            steps {
                bat 'dir src /s /b *.java > sources.txt'
                bat 'javac -cp ".;librerias\\*" -d bin @sources.txt'
            }
        }

        stage('Empaquetar') {
            steps {
                bat 'jar cfe app.jar sistemaventa.SistemaVenta -C bin .'
            }
        }

        stage('Finalizado') {
            steps {
                echo '✅ Build completo.'
            }
        }
    }
}
