pipeline {
  agent any

  stages {
    stage('clone github') {
      steps {
        checkout([
          $class           : 'GitSCM',
          branches         : [[name: '*/dev']],
          extensions       : [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'commerce']],
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

    stage('build'){
      steps{
        dir("${env.WORKSPACE}/commerce") {
          sh './gradlew bootJar'
        }
      }
    }

    stage('test gradle'){
      steps{
        dir("${env.WORKSPACE}/commerce") {
          sh './gradlew test'
        }
      }
    }

    stage('build docker') {
      steps {
        withDockerRegistry(credentialsId: 'docker', url: '') {
          script{
            def appImage = docker.build("jinhoa52/commerce-app", ". -f ./commerce/Dockerfile --no-cache")
            appImage.push()
            def serverImage = docker.build("jinhoa52/commerce-nginx",". -f ./commerce/nginx/Dockerfile --no-cache")
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