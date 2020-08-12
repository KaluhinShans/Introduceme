package com.shans.introduceme.information;

import lombok.Data;

import java.util.List;

@Data
public class PersonInformation {
    private String name = "Kaluhin Shans";
    private int age = 17;
    private String location = "Ukraine, Kharkiv";

    private String gitHubProfile = "MadgelS";

    private List<Project> projects;

    public String getCaption(){
        StringBuilder result = new StringBuilder();
        result.append("Name: ").append(name).append("\n");
        result.append("Age: ").append(age).append("\n");
        result.append("Location: ").append(location).append("\n");
        return result.toString();
    }


}
