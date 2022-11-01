pipeline {
    agent any

    stages {
        stage('github clone') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/dev']],
                    extensions: [],
                    userRemoteConfigs: [[
                        credentialsId: 'repo-and-hook',
                        url: 'https://github.com/f-lab-edu/commerce'
                    ]]
                ])
            }
        }

        stage('build'){
            steps{
                sh '''
                    echo build start
                    ./gradlew clean bootJar
                '''
            }

        }

        stage('test'){
            steps{
                sh '''
                    echo test start
                    ./gradlew test
                '''
            }
        }

        stage('publish on ssh'){
            steps{
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: ' commerce-server',
                            transfers: [
                                sshTransfer(
                                    cleanRemote: false,
                                    excludes: '',
                                    execCommand: 'sh /root/init_server.sh',
                                    execTimeout: 120000,
                                    flatten: false,
                                    makeEmptyDirs: false,
                                    noDefaultExcludes: false,
                                    patternSeparator: '[, ]+',
                                    remoteDirectory: '/commerce/deploy',
                                    remoteDirectorySDF: false,
                                    removePrefix: 'build/libs',
                                    sourceFiles: 'build/libs/*.jar'
                                )
                            ],
                            usePromotionTimestamp: false,
                            useWorkspaceInPromotion: false,
                            verbose: true
                        )
                    ]
                )
            }
        }
    }
}
