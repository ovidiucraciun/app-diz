node('master'){

  def gitUrl='http://github.com/ovidiucraciun/app-diz.git'
  def gitCredential='c55c540f-d6e8-4f2f-b0b5-98d9987905db'

  try{
    stage("SCM"){
       step([$class:'WsCleanup'])
       git branch: BRANCH_NAME, credentialsId: gitCredential, url: gitUrl
    }
    stage("Build"){
       timeout(time:360, unit:'SECONDS'){
          withCredentials([usernamePassword(credentialsId:)])
       }
    }


  }




}
