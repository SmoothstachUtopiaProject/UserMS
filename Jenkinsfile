pipeline {
    agent any

    stages {
        stage('Package') {
            steps {
                echo 'Building..'
                script {
                    sh "mvn clean package"
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Deploying....'
                sh "aws ecr get-login-password --region us-east-1 --profile=default | docker login --username AWS --password-stdin 466486113081.dkr.ecr.us-east-1.amazonaws.com"                
                sh "docker build -t utopiauserms ."
                sh "docker tag utopiauserms:latest 466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/utopiauserms"
                sh "docker push 466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/utopiauserms"
            }
        }
        // stage('Deploy') {
        //    steps {
        //        sh "rm ECSService.yml"
        //        sh "wget https://github.com/SmoothstackUtopiaProject/CloudFormationTemplates/blob/main/ECSService.yml"
        //        sh "aws cloudformation deploy --stack-name UtopiaAirplaneMS --template-file ECSService.yml --parameter-overrides ApplicationName=UtopiaAirplaneMS DBHost=$DB_HOST DBName=$DB_NAME DBPort=$DB_PORT ECRepositoryURI=466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/airplanems:latest SecurityGroupID=$SECURITYGROUPID SubnetID=$SUBNETID"
        //    }
        // }
    }
}