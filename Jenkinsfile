pipeline {
    agent any

    triggers {
        pollSCM('*/5 * * * *')
    }

    stages {
        stage('Build') {
            steps {
                gradlew('clean', 'classes', 'build')
            }            
        }
        stage('Unit Tests') {
            steps {
                gradlew('test')
            }
            post {
                always {
                    junit '**/build/test-results/test/TEST-*.xml'
                }
            }
        }
        
 
        
        stage('Integration Tests') {
                    steps {
                        gradlew('intTest')
                    }
                    post {
                        always {
                            junit '**/build/test-results/intTest/TEST-*.xml'
                        }
                    }
        }
        stage("Review integration test?") {
            steps {
                script {
                    env.RELEASE_SCOPE = input message: 'User input required', ok: 'Release!',
                            parameters: [choice(name: 'RELEASE_SCOPE', choices: 'patch\nminor\nmajor', description: 'What is the release scope?')]
                }
                echo "${env.RELEASE_SCOPE}"
            }
        }
        stage('Assemble') {
            steps {
                 stash includes: '**/build/libs/*.jar', name: 'app'
                 stash includes: '**/build/reports/*.csv', name: 'TestResultsSummary'
                 
            }
        }
        stage('Promotion') {
            steps {
                timeout(time: 1, unit:'DAYS') {
                    input 'Deploy to Production?'
                }
            }
        }        
    }
     
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}