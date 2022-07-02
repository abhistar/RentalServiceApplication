package repository;

import model.Branch;

import java.util.HashMap;
import java.util.Map;

public class BranchRepository {
    private static final Map<String, Branch> branchMap = new HashMap<>();

    public boolean saveBranch(Branch branch){
        if(branchMap.containsKey(branch.getName()))
            return false;
        branchMap.put(branch.getName(), branch);
        return true;
    }

    public Branch getBranchByName(String branchName){
        return branchMap.get(branchName);
    }
}
