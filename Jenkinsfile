pipeline {
    agent {
        kubernetes {
            inheritFrom 'default'
            yaml """
            apiVersion: v1
            kind: Pod
            spec:
              containers:
              - name: maven
                image: maven:3-eclipse-temurin-17-alpine
                command:
                - cat
                tty: true
            """
        }
    }
    stages {
        stage('Checkout') {
            steps {
                container('maven') {
                    // Download the ZIP file
                    sh 'wget https://codeload.github.com/Gamerboy59/RocketBoots/zip/refs/heads/master -O RocketBoots.zip'
                    // Unzip the file
                    sh 'unzip RocketBoots.zip'
                }
            }
        }
        stage('Build and Archive') {
            steps {
                container('maven') {
                    dir('RocketBoots-master') { // change directory to the unzipped project
                        // Run Maven build with goals from pom.xml
                        sh 'mvn'
                        // Archive the build artifacts
                        archiveArtifacts artifacts: 'target/RocketBoots*.jar', allowEmptyArchive: true
                        // Create fingerprints for the archived artifacts
                        fingerprint 'target/RocketBoots*.jar'
                    }
                }
            }
        }
    }
}
