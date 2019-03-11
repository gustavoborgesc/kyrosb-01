package br.com.kyros.backend;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import br.com.kyros.backend.controller.Home;

public class ClienteServiceTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(Home.class)
	}
	
	@Test
	public void cadastrarClienteTest() {
		Response response = target("v1/cliente").request()
		        .post(Entity.json("{\"nome\":\"Beltrano de Tal\",\"cpf\":\"22222222222\"}"));
		assertEquals("Http Response should be 201 ", Status.CREATED.getStatusCode(), response.getStatus());
		    assertThat(response.readEntity(String.class), containsString("Cliente Cadastrado com sucesso"));
	}
	
}
