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
       withCredentials([usernamePassword(credentialsId: 'c5ecbc27-69b3-4cbf-b232-02979e31177e', usernameVariable:'ansibleUser', passwordVariable: 'ansibleUserPassword')])
       def your_folder_repo = 'ansible'
                   sh "sshpass -p ${ansibleUserPassword} ssh ${ansibleUser}@localhost 'mkdir -p /home/${ansibleUser}/${your_folder_repo}'"
                   sh "sshpass -p ${ansibleUserPassword} scp -r * ${ansibleUser}@localhost:/home/${ansibleUser}/${your_folder_repo}"
                   sh """sshpass -p ${ansibleUserPassword} ssh -o StrictHostKeyChecking=no ${ansibleUser}@localhost 'cd /home/${ansibleUser}/${your_folder_repo}' && pwd"""
//&& ansible-playbook -i inventories/development/hosts microservice-initialization-apmm.yml --extra-vars "JAR_BUILD_TYPE=$JAR_BUILD_TYPE JAR_FILENAME=$JAR_FILENAME"
    }


//  }




}
