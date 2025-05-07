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
                    bat 'mkdir out && for /R src %f in (*.java) do javac -d out "%f"'
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
