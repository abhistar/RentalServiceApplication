package repository;

import model.Branch;

import java.util.HashMap;
import java.util.Map;

public class BranchRepository {
    private static Map<String, Branch> branchMap = new HashMap<>();

    public static boolean saveBranch(Branch branch){
        if(branchMap.containsKey(branch.getName()))
            return false;
        branchMap.put(branch.getName(), branch);
        return true;
    }

    public static Branch getBranchByName(String branchName){
        return branchMap.get(branchName);
    }
}
