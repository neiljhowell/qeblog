import groovy.json.JsonSlurper

def PowerShell(psCmd) {
    psCmd=psCmd.replaceAll("%", "%%")
    bat "powershell.exe -NonInteractive -ExecutionPolicy Bypass -Command \"\$ErrorActionPreference='Stop';[Console]::OutputEncoding=[System.Text.Encoding]::UTF8;$psCmd;EXIT \$global:LastExitCode\""
}

pipeline {
    master

    stages {
        stage('Update RSS Feed') {
            steps {
                bat 'python test.py'
            }
        }
          stage('Commit') {
            steps {
                bat '''git config user.name "svcselenium"'''
                bat '''git config user.email "robosquad@accruent.com"'''
                bat 'git add .'
                bat '''git commit -m "testing"'''
                //bat 'git push origin HEAD:master --force'
            }
         }
         stage('Publish') {
            steps {
                PowerShell(". 'C:/push.ps1'")
            }
         }
             /*
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
       */
    }
}
