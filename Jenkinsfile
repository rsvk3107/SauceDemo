
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Environment') {
            steps {
                bat 'java -version'
                bat '"C:\\apache-maven-3.9.16\\bin\\mvn.cmd" -version'
            }
        }

        stage('Run Selenium Tests') {
            steps {
                bat '"C:\\apache-maven-3.9.16\\bin\\mvn.cmd" -B clean test'
            }
        }
    }

    post {
        always {
            junit(
                testResults: 'target/surefire-reports/TEST-*.xml',
                allowEmptyResults: true
            )
        }
    }
}
