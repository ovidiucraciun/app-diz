node('master'){

  def gitUrl='http://github.com/ovidiucraciun/app-diz.git'
  def gitCredential='c55c540f-d6e8-4f2f-b0b5-98d9987905db'

//  try{
    stage("SCM"){
       step([$class:'WsCleanup'])
       git branch: BRANCH_NAME, credentialsId: gitCredential, url: gitUrl
    }
    stage("Build"){
       timeout(time:360, unit:'SECONDS'){
          withCredentials([usernamePassword(credentialsId: 'git_clone_cred	', passwordVariable: 'git_password', usernameVariable: 'git_user')]){
           sh 'gradle build -x test'
           sh 'ls -al build/libs'
           //sh 'ls -al'
          }
       }
    }

    stage("Deploy"){
       withCredentials([usernamePassword(credentialsId: 'ansible-master', usernameVariable:'ansibleUser', passwordVariable: 'ansibleUserPassword')]){
       def your_folder_repo = 'ansible'
                   sh "sshpass -p ${ansibleUserPassword} ssh ${ansibleUser}@localhost -o StrictHostKeyChecking=no 'mkdir -p /home/${ansibleUser}/${your_folder_repo}'"
                   sh "sshpass -p ${ansibleUserPassword} scp -r * ${ansibleUser}@localhost:/home/${ansibleUser}/${your_folder_repo}"
                   sh """sshpass -p ${ansibleUserPassword} ssh -o StrictHostKeyChecking=no ${ansibleUser}@localhost 'cd /home/${ansibleUser}/${your_folder_repo}' && pwd && whoami"""
//&& ansible-playbook -i inventories /hosts microservice-initialization-apmm.yml --extra-vars "JAR_BUILD_TYPE=$JAR_BUILD_TYPE JAR_FILENAME=$JAR_FILENAME"
       }
    }

      stage("Move artifact"){
//         withCredentials([usernamePassword(credentialsId: 'git_clone_cred	', passwordVariable: 'git_password', usernameVariable: 'git_user')]){
           withCredentials([azureServicePrincipal('jenkins-ad')]){
//           sh "cp build/libs/* /mnt/csb3b6d9a4ea33ex4761xb9d"
           sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
           sh "curl -X POST -d build/libs/* https://csb3b6d9a4ea33ex4761xb9d.file.core.windows.net/artifacts-storage"
//           sh "ls -la /mnt/csb3b6d9a4ea33ex4761xb9d/"
         }
      }

    stage("Kubernetes Cluster Create"){
       withCredentials([azureServicePrincipal('jenkins-ad')]){
         timeout(time:720, unit:'SECONDS'){
        sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
        CLUSTER_NAME = sh(
           script: 'az group exists -n aks-cluster',
           returnStdout: true
        ).trim()
        echo "Cluster exist? -> ${CLUSTER_NAME}"
        if(CLUSTER_NAME == 'false'){
           sh "az aks create \
             --resource-group aks-cluster \
             --name dizAKSCluster \
             --node-count 2 \
             --enable-addons monitoring \
             --generate-ssh-keys \
             --service-principal 'e71e6f59-3b9e-47a3-b4ef-a5743dce6b22' \
             --client-secret '52e13906-17a5-4954-ad77-dac22e322a90'"
           echo "The new aks cluster is up and running!"
           sh "az aks get-credentials --resource-group UTCN --name cluster-diz"
//         sh "kubectl run nodeapp --image=aksdizregistry.azurecr.io/node:v1 --replicas=1 --port=8080"
           sh "kubectl get nodes"
//         sh "kubectl get po"
//         sh "kubectl get rs"
//         sh "echo test"
         }
         if(CLUSTER_NAME == 'true'){
           sh "az aks get-credentials --resource-group UTCN --name cluster-diz"
           sh "kubectl get nodes"
           echo "Cluster already exist and it received credentials"
         }
         }
       }
    }
    stage("Create image"){
       withCredentials([azureServicePrincipal('jenkins-ad')]){
         sh ("ls -al build/libs/")
         sh ("cp build/libs/* .")
//         sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
//         sh ("wget https:csb3b6d9a4ea33ex4761xb9d.file.core.windows.netartifacts-storageapp-diz-0.0.1-SNAPSHOT.jar")
         sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
//         sh "az acr login --name aksdizregistry"
         sh ("""echo "FROM openjdk:8-jre-alpine\n
  COPY app-diz-0.0.1-SNAPSHOT.jar /opt/spring-cloud/lib/\n
  EXPOSE 8080\n
  CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/opt/spring-cloud/lib/app-diz-0.0.1-SNAPSHOT.jar"]" > Dockerfile""")
         sh ("cat Dockerfile")
         sh ("pwd && ls -al")
       }
    }
    stage("Push image to ACR"){
        withCredentials([azureServicePrincipal('jenkins-ad')]){
           sh ("pwd && ls -al")
           sh ("az acr build --image dizertatie/diz-app:v1 --registry aksdizregistry --file Dockerfile .")
        }
    }
    stage("Deployment"){
        withCredentials([azureServicePrincipal('jenkins-ad')]){
           DEPLOY_VAR = sh(
              script: 'kubectl get deploy',
              returnStdout: true
           ).trim()
           if(DEPLOY_VAR){
             sh "(kubectl delete deploy diz-app-deployment)"
           }
           sh "(kubectl run diz-app-deployment --image=aksdizregistry.azurecr.io/dizertatie/diz-app:v1 --replicas=2 --port=8080)"
           sh "(kubectl get deploy && kubectl get pods && kubectl get rs)"
           sh "(kubectl expose deploy diz-app-deployment --port=80 --target-port=8080 --dry-run -o yaml > svc1.yaml)"
           sh "(pwd && ls -al)"
           sh "(touch svc.yaml && sed '9i  type: LoadBalancer' /var/lib/jenkins/workspace/build-appdiz-mp_master/svc1.yaml > /var/lib/jenkins/workspace/build-appdiz-mp_master/svc.yaml)"
//           sh "(gawk -i inplace '{sub(/spec:/,'spec:  type: LoadBalancer')}' /var/lib/jenkins/workspace/build-appdiz-mp_master/svc.yaml)"
//           sh "(awk '{ gsub()}')"
           sh "(cat /var/lib/jenkins/workspace/build-appdiz-mp_master/svc.yaml)"
           sh "(kubectl apply -f svc.yaml)"
           sh "(kubectl get svc)"

        }
    }

}
