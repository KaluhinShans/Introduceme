package com.shans.introduceme.information;

import lombok.Data;

import java.util.List;

@Data
public class PersonInformation {
    private static String name = "Kaluhin Shans";
    private static int age = 17;
    private static String location = "Ukraine, Kharkiv";
    public static String gitHubProfile = "MadgelS";

    private static List<Project> projects = GitParser.getProjectsFromGit();

    public static String getCaption(){
        StringBuilder result = new StringBuilder();
        result.append("Name: ").append(name).append("\n");
        result.append("Age: ").append(age).append("\n");
        result.append("Location: ").append(location).append("\n");
        return result.toString();
    }

    public static String getProjects(){
        StringBuilder result = new StringBuilder();
        for (Project p: projects) {
            result.append("Project: ").append("*" + p.getName()+"*").append("\n");
            result.append("GitHub: ").append("[link](" + p.getGitHubURL() + ")").append("\n");
            result.append("Description:\n").append("  "+p.getDescription()).append("\n\n");
        }
        return result.toString();
    }


}
