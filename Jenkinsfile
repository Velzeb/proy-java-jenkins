pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Jenkins ya hace esto automáticamente, pero lo dejamos claro
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[url: 'https://github.com/Velzeb/proy-java-jenkins.git']]
                ])
            }
        }

        stage('Compilar') {
            steps {
                echo 'Compilando proyecto...'
                // Asumiendo que usas javac y tu código está en src/
                sh 'javac -d out $(find src -name "*.java")'
            }
        }

        stage('Empaquetar') {
            steps {
                echo 'Empaquetando...'
                sh 'jar -cvf app.jar -C out .'
            }
        }

        stage('Finalizado') {
            steps {
                echo 'Build completado correctamente.'
            }
        }
    }
}
