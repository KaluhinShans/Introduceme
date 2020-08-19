package com.shans.introduceme.information;

import lombok.Data;

import java.util.List;

@Data
public class PersonInformation {
    private static String name = "Kaluhin Shans";
    private static int age = 17;
    private static String location = "Ukraine, Kharkiv";
    public static String number = "+380936535707";
    public static String email = "kaluhinshans@gmail.com";
    public static String gitHubProfile = "MadgelS";
    public static String linkedInProfile = "kaluhinshans";
    public static String telegram = "Shansoon";

    private static List<Project> projects = GitParser.getProjectsFromGit(gitHubProfile);

    public static String getCaption(){
        StringBuilder result = new StringBuilder();
        result.append("Name: ").append("*"+name+"*").append("\n");
        result.append("Age: ").append(age).append("\n");
        result.append("Number: ").append(number).append("\n\n");
        result.append("Telegram: ").append("[chat](https://t.me/"+ telegram + ")").append("\n");
        result.append("LinkedIn: ").append("[profile](https://www.linkedin.com/in/"+ linkedInProfile + ")").append("\n");
        result.append("GitHub: ").append("[profile](https://github.com/"+ gitHubProfile + ")").append("\n");
        result.append("Email: ").append(email).append("\n");
        return result.toString();
    }

    public static String getProjects(){
        StringBuilder result = new StringBuilder();
        for (Project p: projects) {
            if (p.isError()){return "No such profile";}
            result.append("Project: ").append("*" + p.getName()+"*").append("\n");
            result.append("GitHub: ").append("[repository](" + p.getGitHubURL() + ")").append("\n");
            result.append("Description:\n").append("  "+p.getDescription()).append("\n\n\n");
        }
        if (projects.size() == 0){return "Profile haven't public repositories";}
        return result.toString();
    }
}
