pipeline {
    agent any

    stages {
        stage('Clonar código') {
            steps {
                git 'https://github.com/Velzeb/proy-java-jenkins.git'
            }
        }

        stage('Compilar') {
            steps {
                sh 'javac -d bin src/*.java'
            }
        }

        stage('Empaquetar') {
            steps {
                sh 'jar cf app.jar -C bin .'
            }
        }

        stage('Finalizado') {
            steps {
                echo 'Build completo.'
            }
        }
    }
}
