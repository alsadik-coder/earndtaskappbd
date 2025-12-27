package com.sadik.earntask.Models;
import java.util.ArrayList;
import java.util.List;

public class MicroTask {

    // Task info (from server)
    public String id;
    public String name;
    public String shortInfo;
    public String reward;
    public String requirements;

    // User submission (local)
    public List<String> screenshotUris; // multiple screenshots
    public String userNote;              // optional note

    public MicroTask(String id,
                     String name,
                     String shortInfo,
                     String reward,
                     String requirements) {

        this.id = id;
        this.name = name;
        this.shortInfo = shortInfo;
        this.reward = reward;
        this.requirements = requirements;

        this.screenshotUris = new ArrayList<>();
        this.userNote = "";
    }
}
