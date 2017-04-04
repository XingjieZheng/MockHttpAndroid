package com.xingjiezheng.plugin;

import org.gradle.api.Plugin
import org.gradle.api.Project

class LogPlugin implements Plugin<Project> {
    void apply(Project project) {
        println "========================";
        println "hello gradle plugin!";
        println "========================";
        project.task('testPlugin') << {
            println "test plugin successfully"
        }
    }
}
