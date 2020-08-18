package com.shans.introduceme.information;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ForceReply {
    public Map<Long, String> usersWaiting = new HashMap<>();
}
