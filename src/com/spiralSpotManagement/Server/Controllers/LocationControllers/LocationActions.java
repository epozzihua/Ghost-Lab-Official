package com.spiralSpotManagement.Server.Controllers.LocationControllers;
import com.spiralSpotManagement.Server.DbController.CloudStorageConnectionHandler;
import com.spiralSpotManagement.Server.Model.LocationModel;
import com.spiralSpotManagement.Server.Model.RequestBody;
import com.spiralSpotManagement.Server.Model.ResponseStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
public class LocationActions {

    public ResponseStatus registerLocation(LocationModel location)throws Exception{
        String location_id = UUID.randomUUID().toString();

        try{
            String par_id = location.getParent_id()==null || location.getParent_id().trim()==""? "": location.getParent_id();
            String sql = "INSERT INTO locations(" +
                    "location_id,level_id,parent_id,location_name,location_GPS,description)" +
                    " VALUES(?,?,?,?,?,?)";

            Connection connection = new CloudStorageConnectionHandler().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, location_id);
            stmt.setString(2, location.getLevel_id());
            stmt.setString(3, location.getParent_id());
            stmt.setString(4, location.getLocation_name());
            stmt.setString(5, location.getLocation_GPS());
            stmt.setString(6, location.getDescription());
            int inserted_rec = stmt.executeUpdate();
            if(inserted_rec == 1){
                return new ResponseStatus(200,"CREATED","Location registered");
            }
            if(connection != null){
//                conn.close();
                return new ResponseStatus(500,"SERVER ERROR","Insertion failed, try or contact System Administrator");
            }

        }catch(Exception e){
            return new ResponseStatus(300,"EXCEPTIONAL ERROR",e.getMessage());
        }
        return new ResponseStatus(200,"CREATED","Location  registered");
    }

    /*
     *location management class. Method updating for updating given location
     * @author Felix DUSENGIMANA
     * @powered by Rwanda Coding Academy
     * instructor Donatien MASHENGESHO
     * @since  04-02-2021
     * @param data {Hashmap} for new data to update existing ones.
     * return boolean to indicated the success or fail to update.
     *
     */

    public  ResponseStatus UpdateLocation (LocationModel location){

        HashMap<String,String> updateLocationData = new HashMap<>();

        updateLocationData.put("location_id",location.getLocation_id());
        updateLocationData.put("parent_id",location.getParent_id());
        updateLocationData.put("level_id",location.getLevel_id());
        updateLocationData.put("location_name",location.getLocation_name());
        updateLocationData.put("location_GPS",location.getLocation_GPS());
        updateLocationData.put("description",location.getDescription());

        Iterator dataIterator = updateLocationData.entrySet().iterator();
        StringBuilder attr = new StringBuilder();
        String cond = "",query="";

        //while loop for looping through hashMap to check the attributes a user wants to update
        while (dataIterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)dataIterator.next();
            if (mapElement.getValue()!=null){
                /*get location id to update*/
                if(mapElement.getKey() =="location_id"){
                    cond = "WHERE "+mapElement.getKey()+"='"+mapElement.getValue()+"'";
                }else{
                    attr.append(" ").append(mapElement.getKey()).append("=").append("'").append(mapElement.getValue()).append("',");
                }
            }
        }

        try{
            Connection connection = new CloudStorageConnectionHandler().getConnection();

            if(attr.length()==0){
                return  new ResponseStatus(400,"BAD REQUEST","Please enter at least one key to update.");
            }else{
                String withoutLastComma = attr.substring( 0, attr.length( ) - ",".length( ) );
                query +="UPDATE locations SET "+withoutLastComma+" "+cond;
                System.out.println(query);

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                int updated = preparedStatement.executeUpdate();
                if(updated==1){
                    return  new ResponseStatus(200,"UPDATED","Location updated successfully.");
                }else {
                    return  new ResponseStatus(400,"BAD REQUEST","Error while updating.");
                }
            }
        }catch (Exception e){
            return  new ResponseStatus(300,"EXCEPTION ERROR",e.getMessage());
        }
    }
//    OTHER METHODS TO GO HERE
//    ---------------------------------------


}
