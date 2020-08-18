package com.shans.introduceme.information;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitParser {
    private static Document doc;

    public GitParser(){

    }

    public static List<Project> getProjectsFromGit(){
        try {
            doc = Jsoup.connect("https://github.com/" + PersonInformation.gitHubProfile + "?tab=repositories")
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        List<Project> projects = new ArrayList();
        Elements repoList = doc.select("div#user-repositories-list");
        for (Element e: repoList.select("li")) {
            Project p = new Project();
            p.setName(e.select("a[itemprop=name codeRepository]").text());
            p.setDescription(e.select("p[itemprop=description]").text());
            p.setGitHubURL("https://github.com" + e.select("a[itemprop=name codeRepository]").attr("href"));
            projects.add(p);
        }
        return projects;
    }
}
