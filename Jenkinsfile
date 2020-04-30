pipeline {
    agent any

    triggers {
        pollSCM('*/5 * * * *')
    }

    stages {
        stage('Build') {
            steps {
            	sh "./gradlew clean classes build -s"
            }            
        }
        stage('Unit Tests') {
            steps {
            	sh "./gradlew test"
             }
            post {
                always {
                    junit '**/build/test-results/test/TEST-*.xml'
                }
            }
        }
        
 
        
        stage('Integration Tests') {
                    steps {
                       sh "./gradlew intTest"                    
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
                    env.RELEASE_SCOPE = input message: 'User input required', ok: 'Reviewed!',
                            parameters: [choice(name: 'RELEASE_SCOPE', choices: 'patch\nminor\nmajor', description: 'What is the release scope?')]
                }
                echo "${env.RELEASE_SCOPE}"
            }
        }
        stage('Assemble') {
            steps {
                 archiveArtifacts artifacts: '**/build/libs/*.jar', '**/build/reports/*.csv'              
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