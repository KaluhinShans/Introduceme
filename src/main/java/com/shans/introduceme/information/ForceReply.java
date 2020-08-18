package com.shans.introduceme.information;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ForceReply {
    public Map<Long, String> usersWaiting = new HashMap<>();

    public Map<String, String> usersMessages = new HashMap<>();
}
