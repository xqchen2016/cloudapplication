package edu.zju.sa.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.spi.resource.Singleton;
import edu.zju.sa.dao.BOSClient;
import edu.zju.sa.dao.BOSClientFactory;
import lovfri.lib.web.rest.WebResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Singleton
public class HomeResource {
	private BOSClient bosClient = BOSClientFactory.getClient();

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * @return
	 */
	@GET
	@Path("index")
	@Produces(MediaType.APPLICATION_JSON)
	public Response index() {
		WebResult result = new WebResult();
		try {
			result.setD(bosClient.listFiles());
		}catch (Exception ex){
			result.setS(1);
			result.setM(ex.getMessage());
			ex.printStackTrace();
		}
		return Response.ok().entity(gson.toJson(result)).build();
	}
}
