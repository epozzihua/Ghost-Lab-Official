package com.spiralSpotManagement.Server.Controllers.SpotControllers;

import com.spiralSpotManagement.Server.Controllers.UserModuleControllers.UsersActions;
import com.spiralSpotManagement.Server.Model.RequestBody;
import com.spiralSpotManagement.Server.Model.ResponseStatus;
import com.spiralSpotManagement.Server.Model.SpotCategory;
import com.spiralSpotManagement.Server.Model.User;

import java.util.ArrayList;
import java.util.List;
/*
        @author : NTWARI Egide
        @Description: spot category controller for synchronizing the spot methods
 */

public class SpotCategoryController {
    public List<Object> mainMethod(RequestBody requestBody)throws Exception{
        String action = requestBody.getAction();
        List<Object> usersObject = new ArrayList<>();
        switch (action){
            case "register":
                ResponseStatus registeredStatus = new SpotCategoryActions().addNewSpotCategory((SpotCategory) requestBody.getObject());
                usersObject.add((Object) registeredStatus);
                return  usersObject;

            case "login":
                ResponseStatus loggedInStatus = new UsersActions().loginUser((User) requestBody.getObject());
                usersObject.add((Object) loggedInStatus);
                return  usersObject;

            default:
        }

        return null;
    }
}
