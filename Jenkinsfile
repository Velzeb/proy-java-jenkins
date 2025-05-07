pipeline {
    agent any

    stages {
        stage('Preparar directorio') {
            steps {
                bat '''
                    rmdir /s /q out
                    mkdir out
                '''
            }
        }

        stage('Compilar') {
            steps {
                bat '''
                    for /R src %%f in (*.java) do javac -cp "librerias/*;out" -d out "%%f"
                '''
            }
        }

        stage('Empaquetar JAR') {
            steps {
                bat '''
                    echo Main-Class: sistemaventa.SistemaVenta > manifest.txt
                    jar cfm app.jar manifest.txt -C out .
                    del manifest.txt
                '''
            }
        }

        stage('Ejecutar') {
            steps {
                bat 'java -jar app.jar'
            }
        }

        stage('Finalizado') {
            steps {
                echo '✅ Compilación y ejecución completadas correctamente.'
            }
        }
    }
}
