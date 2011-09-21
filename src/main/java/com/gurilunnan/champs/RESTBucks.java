package com.gurilunnan.champs;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Glenn Bech
 */

@Path("/")
public class RESTBucks {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/beacon/{beacon}")
    public Locations location(@PathParam("beacon") String beaconId) {
        Locations l = new Locations();
        ArrayList<String> allLocations = new ArrayList<String>();
        allLocations.add("St. Petersburg 2");
        allLocations.add("Oslo");
        allLocations.add("Moon");
        l.setAllLocations(allLocations);
        return l;
    }

    @XmlRootElement
    static class Locations {
        private List<String> allLocations;

        Locations() {
        }

        public List<String> getAllLocations() {
            return allLocations;
        }

        public void setAllLocations(List<String> allLocations) {
            this.allLocations = allLocations;
        }
    }

}
