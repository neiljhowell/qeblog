import groovy.json.JsonSlurper

def PowerShell(psCmd) {
    psCmd=psCmd.replaceAll("%", "%%")
    bat "powershell.exe -NonInteractive -ExecutionPolicy Bypass -Command \"\$ErrorActionPreference='Stop';[Console]::OutputEncoding=[System.Text.Encoding]::UTF8;$psCmd;EXIT \$global:LastExitCode\""
}

pipeline {
    agent { label 'master' }

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
            }
         }
         stage('Publish') {
            steps {
                try {
                PowerShell(". 'C:/Jenkins/Scripts/qe-blog-rss-push.ps1'")
                } catch (err) {
                    "Something went wrong or nothing to push"
                }
            }
         }
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
    }
}
