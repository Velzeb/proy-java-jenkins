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
                // Verifica el contenido del directorio 'out' para asegurarse de que las clases fueron compiladas correctamente.
                bat 'dir out'
            }
        }

        stage('Empaquetar JAR') {
            steps {
                bat '''
                    echo Main-Class: sistemaventa.SistemaVenta > manifest.txt
                    jar cfm app.jar manifest.txt -C out .
                    del manifest.txt
                '''
                // Verifica que el archivo JAR contiene las clases compiladas.
                bat 'jar tf app.jar'
            }
        }

        stage('Ejecutar') {
            steps {
                // Verifica si el archivo JAR se empaquetó correctamente.
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
