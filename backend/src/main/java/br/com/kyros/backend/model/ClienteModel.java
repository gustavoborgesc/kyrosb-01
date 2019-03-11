package br.com.kyros.backend.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.kyros.backend.entity.ClienteEntity;

public class ClienteModel {

	private final EntityManager entityManager;

	private final EntityManagerFactory entityManagerFactory;

	public ClienteModel() {
		this.entityManagerFactory = Persistence
				.createEntityManagerFactory("myPU");
		this.entityManager = this.entityManagerFactory.createEntityManager();
	}

	public void salvar(ClienteEntity cliente) {
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(cliente);
		this.entityManager.getTransaction().commit();
	}

	public void alterar(ClienteEntity cliente) {
		this.entityManager.getTransaction().begin();
		this.entityManager.merge(cliente);
		this.entityManager.getTransaction().commit();
	}

	public void excluir(Long id) {
		ClienteEntity cliente = this.getCliente(id);

		this.entityManager.getTransaction().begin();
		this.entityManager.remove(cliente);
		this.entityManager.getTransaction().commit();
	}

	public ClienteEntity getCliente(Long id) {
		return this.entityManager.find(ClienteEntity.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ClienteEntity> todosClientes() {
		return this.entityManager.createQuery(
				"SELECT c FROM ClienteEntity c ORDER BY c.nome")
				.getResultList();
	}

}
