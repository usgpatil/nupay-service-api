
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        timestamps()
    }

    // Deployment stays disabled until you explicitly enable it.
    parameters {
        booleanParam(
            name: 'DEPLOY',
            defaultValue: false,
            description: 'Enable to deploy all NuPay services.'
        )
    }

    environment {
        DB_USERNAME = credentials('nupay-db-username')
        DB_PASSWORD = credentials('nupay-db-password')
        MYSQL_ROOT_PASSWORD = credentials('nupay-db-password')
    }

    stages {
        stage('Welcome') {
            steps {
                echo 'Starting NuPay CI/CD Pipeline'
            }
        }

        stage('Checkout Repositories') {
            steps {
                dir('nupay_serviceAPI') {
                    git branch: 'main',
                        url: 'https://github.com/usgpatil/nupay-service-api.git'
                }

                dir('smfg') {
                    git branch: 'main',
                        url: 'https://github.com/usgpatil/Nupay-Target_Service.git'
                }
            }
        }

        stage('Check Java and Docker') {
            steps {
                bat 'java -version'
                bat 'docker --version'
                bat 'docker info'
            }
        }

        stage('Build and Test Main API') {
            steps {
                dir('nupay_serviceAPI') {
                    bat 'mvnw.cmd -B clean verify'
                }
            }
        }

        stage('Build and Test Target Service') {
            steps {
                dir('smfg') {
                    bat 'mvnw.cmd -B clean verify'
                }
            }
        }

       stage('Validate Docker Compose') {
    steps {
        dir('nupay_serviceAPI') {
            bat 'docker compose config --quiet'
        }
    }
}

        stage('Deploy All Services') {
            when {
                expression { params.DEPLOY }
            }
            steps {
                dir('nupay_serviceAPI') {
                    bat 'docker compose up -d --build'
                    bat 'docker compose ps'
                }
            }
        }
    }

    post {
        success {
            echo 'NuPay CI/CD pipeline completed successfully!'
        }
        failure {
            echo 'NuPay CI/CD pipeline failed. Check Console Output.'
        }
    }
}
