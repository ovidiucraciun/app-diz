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
         withCredentials([usernamePassword(credentialsId: 'git_clone_cred	', passwordVariable: 'git_password', usernameVariable: 'git_user')]){
           sh "cp build/libs/* /mnt/csb3b6d9a4ea33ex4761xb9d"
           sh "ls -la /mnt/csb3b6d9a4ea33ex4761xb9d/"
         }
      }

    stage("Kubernetes Cluster Create"){
       withCredentials([usernamePassword(credentialsId: 'git_clone_cred	', passwordVariable: 'git_password', usernameVariable: 'git_user')]){
         timeout(time:720, unit:'SECONDS'){
            sh "az aks create \
               --resource-group aks-cluster \
               --name dizAKSCluster \
               --node-count 1 \
               --enable-addons monitoring \
               --generate-ssh-keys \
               --service-principal 'e71e6f59-3b9e-47a3-b4ef-a5743dce6b22' \
               --client-secret '52e13906-17a5-4954-ad77-dac22e322a90'"
//         sh "az aks get-credentials --resource-group UTCN --name cluster-diz"
         }
       }
    }

}
