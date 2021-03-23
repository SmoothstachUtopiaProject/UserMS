pipeline {
    agent any
    environment {
        COMMIT_HASH="${sh(script:'git rev-parse --short HEAD', returnStdout: true).trim()}"
    }

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
                sh "docker build --tag utopiauserms:$COMMIT_HASH ."
                sh "docker tag utopiauserms:$COMMIT_HASH 466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/userms:$COMMIT_HASH"
                sh "docker push 466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/userms:$COMMIT_HASH"
            }
        }
        stage('Deploy') {
           steps {
               sh "touch ECSService.yml"
               sh "rm ECSService.yml"
               sh "wget https://raw.githubusercontent.com/SmoothstackUtopiaProject/CloudFormationTemplates/main/ECSService.yml"
               sh "aws cloudformation deploy --stack-name UtopiaUserMS --template-file ./ECSService.yml --parameter-overrides ApplicationName=UtopiaUserMS ECRepositoryUri=466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/userms:$COMMIT_HASH DBUsername=$DB_USERNAME DBPassword=$DB_PASSWORD SubnetID=$SUBNET_ID  SecurityGroupID=$SECURITY_GROUP_ID TGArn=$UTOPIA_USERMS_TARGETGROUP --capabilities \"CAPABILITY_IAM\" \"CAPABILITY_NAMED_IAM\""
           }
        }
        stage('Cleanup') {
            steps {
                sh "docker system prune -f"
            }
        }
    }
}
