package com.gurilunnan.champs;

import com.gurilunnan.champs.model.Employee;
import com.gurilunnan.champs.persistence.JDBCTemplate;

import javax.ws.rs.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.*;

/**
 * @author Glenn Bech
 */

@Path("/")
public class BeaconService {


    static final int NOT_FOUND = 404;

    @GET
    @Produces("text/plain")
    @Path("/beacon/{beacon}")
    public Employee location(final @PathParam("beacon") long beaconId) {

        final BeaconWrapper beaconWrapper = new BeaconWrapper();
        JDBCTemplate t = new JDBCTemplate() {
            @Override
            public void execute(Connection conn) throws SQLException {
                PreparedStatement statement = conn.prepareStatement("SELECT ID, LAT,LON,DESCRIPTION, CREATIONDATE  FROM BEACON WHERE BEACON.ID = ?");
                statement.setLong(1, beaconId);
                ResultSet rs = statement.executeQuery();
                if (!rs.first()) {
                    throw new WebApplicationException(NOT_FOUND);
                }
                long id = rs.getLong("ID");
                float lat = rs.getFloat("LAT");
                float lon = rs.getFloat("LON");
                Date date = rs.getDate("CREATIONDATE");
                String description = rs.getString("DESCRIPTION");
               //  b = new (id, lon, lat, description, date);
               // beaconWrapper.setBeacon(b);
                rs.close();
            }
        };
        t.execute();
        return beaconWrapper.getBeacon();
    }

    @XmlRootElement
    static class BeaconWrapper {

        Employee beacon;

        BeaconWrapper(Employee beacon) {
            this.beacon = beacon;
        }

        public BeaconWrapper() {
        }

        public Employee getBeacon() {
            return beacon;
        }

        public void setBeacon(Employee beacon) {
            this.beacon = beacon;
        }
    }


}

