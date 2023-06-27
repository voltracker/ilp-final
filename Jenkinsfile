pipeline {
  agent any

  tools {
    maven "Maven"
  }

  stages {
    stage("build"){
      steps {
        sh "mvn compile"
      }
    }

    stage("test"){
      steps {
        sh "mvn test"
      }
    }

    stage("deploy"){
      steps {
        sh "mvn package"
      }
    }
  }
}
