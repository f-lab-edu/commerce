pipeline {
  agent any

  stages {
    stage('clone github') {
      steps {
        checkout([
          $class           : 'GitSCM',
          branches         : [[name: '*/dev']],
          extensions       : [],
          userRemoteConfigs: [[
                                credentialsId: 'github',
                                url          : 'https://github.com/f-lab-edu/commerce'
                              ]]
        ])
      }
      post {
        success {
          echo 'Success Clone'
        }
        failure {
          error 'Fail Clone'
        }
      }
    }

    stage('build gradle'){
      steps{
        sh 'echo build start'
        sh './gradlew clean bootJar'
      }
    }

    stage('test gradle'){
        steps{
          sh 'echo test start'
          sh './gradlew test'
        }
    }

    stage('build docker') {
      steps {
        withDockerRegistry(credentialsId: 'docker', url: '') {
          script{
            def appImage = docker.build("jinhoa52/commerce-app")
            appImage.push()
            def serverImage = docker.build("jinhoa52/nginx")
            serverImage.push()
          }
        }
      }
    }

    stage('publish on ssh'){
      steps{
        sshPublisher(
          publishers: [
            sshPublisherDesc(
              configName: 'commerce-server',
              transfers: [
                sshTransfer(
                  cleanRemote: false,
                  excludes: '',
                  execCommand: 'sh server_start.sh',
                  execTimeout: 1200000,
                  flatten: false,
                  makeEmptyDirs: false,
                  noDefaultExcludes: false,
                  patternSeparator: '[, ]+',
                  remoteDirectory: '',
                  remoteDirectorySDF: false,
                  removePrefix: '',
                  sourceFiles: ''
                )
              ],
              usePromotionTimestamp: false,
              useWorkspaceInPromotion: false,
              verbose: false
            )
          ]
        )
      }
    }
  }
}