package br.com.kyros.backend.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.kyros.backend.entity.ClienteEntity;
import br.com.kyros.backend.http.Cliente;
import br.com.kyros.backend.model.ClienteModel;

/**
 * Root resource (exposed at "v1" path)
 */
@Path("v1")
public class Home {

	private final ClienteModel model = new ClienteModel();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cliente")
	public String cadastrar(Cliente cliente) {
		ClienteEntity entity = new ClienteEntity();

		try {
			entity.setNome(cliente.getNome());
			entity.setCpf(cliente.getCpf());
			entity.setDataAniversario(cliente.getDataAniversario());
			entity.setEmail(cliente.getEmail());
			entity.setTelefone(cliente.getTelefone());

			model.salvar(entity);
			return "Cliente Cadastrado com sucesso";
		} catch (Exception e) {
			return "Erro ao cadastrar um cliente " + e.getMessage();
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cliente")
	public String editar(Cliente cliente) {
		ClienteEntity entity = new ClienteEntity();

		try {
			entity.setId(cliente.getId());
			entity.setNome(cliente.getNome());
			entity.setCpf(cliente.getCpf());
			entity.setDataAniversario(cliente.getDataAniversario());
			entity.setEmail(cliente.getEmail());
			entity.setTelefone(cliente.getTelefone());

			model.alterar(entity);
			return "Cliente alterado com sucesso";
		} catch (Exception e) {
			return "Erro ao alterar um cliente " + e.getMessage();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cliente/{id}")
	public String excluir(@PathParam("id") Long id) {
		try {
			model.excluir(id);
			return "Cliente excluido com sucesso!";
		} catch (Exception e) {
			return "Erro ao excluir o cliente! " + e.getMessage();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cliente/{id}")
	public Cliente getCliente(@PathParam("id") Long id) {
		ClienteEntity entity = model.getCliente(id);
		if (entity != null) {
			return new Cliente(entity.getId(), entity.getNome(),
					entity.getCpf(), entity.getDataAniversario(),
					entity.getEmail(), entity.getTelefone());
		}
		return null;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cliente")
	public List<Cliente> todosClientes() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		List<ClienteEntity> clienteEntities = model.todosClientes();
		for (ClienteEntity entity : clienteEntities) {
			clientes.add(new Cliente(entity.getId(), entity.getNome(), entity
					.getCpf(), entity.getDataAniversario(), entity.getEmail(),
					entity.getTelefone()));
		}
		return clientes;
	}
}
