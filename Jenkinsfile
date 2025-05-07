pipeline {
    agent any

    stages {

        stage('Preparar carpetas') {
            steps {
                bat '''
                if exist out rmdir /s /q out
                mkdir out
                '''
            }
        }

        stage('Compilar') {
            steps {
                bat '''
                javac -cp "librerias/*" -d out src/sistemaventa/SistemaVenta.java
                for /R src %%f in (*.java) do javac -cp "librerias/*;out" -d out "%%f"
                '''
            }
        }

        stage('Empaquetar') {
            steps {
                bat '''
                echo Main-Class: sistemaventa.SistemaVenta > manifest.txt
                echo. >> manifest.txt
                jar cfm app.jar manifest.txt -C out .
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
                echo '✔️ Proceso completado correctamente.'
            }
        }
    }
}
