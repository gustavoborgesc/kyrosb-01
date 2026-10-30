package br.com.kyros.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Assume;
import org.junit.Test;

import br.com.kyros.backend.entity.ClienteEntity;

/**
 * Garante o artefato corrigido do CVE-2023-22102 e, quando o MySQL local
 * responde, que o Hibernate 5.3 persiste com esse driver.
 */
public class MySqlConnectorTest {

	private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/kyrosb01?userTimezone=true&serverTimezone=UTC";

	@Test
	public void connectorJCorrigidoEstaNoClasspath() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");

		String version = com.mysql.cj.jdbc.Driver.class.getPackage().getImplementationVersion();
		assertNotNull("Implementation-Version do mysql-connector-j", version);
		assertTrue("CVE-2023-22102 exige com.mysql:mysql-connector-j >= 8.2.0, obtido " + version,
				versaoPeloMenos(version, 8, 2, 0));

		String driver = DriverManager.getDriver(JDBC_URL).getClass().getName();
		assertEquals("com.mysql.cj.jdbc.Driver", driver);
	}

	@Test
	public void hibernatePersisteClienteComConnectorJ() {
		Assume.assumeTrue("MySQL local indisponível", mysqlAceitaConexao());

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
		EntityManager em = emf.createEntityManager();
		Long id = null;
		try {
			ClienteEntity cliente = new ClienteEntity();
			cliente.setNome("Teste Connector");
			cliente.setCpf(cpfUnico());
			cliente.setDataAniversario(new Date(0L));
			cliente.setEmail("teste@kyros.local");
			cliente.setTelefone("11999999999");

			em.getTransaction().begin();
			em.persist(cliente);
			em.getTransaction().commit();

			id = cliente.getId();
			assertNotNull(id);

			em.clear();
			ClienteEntity lido = em.find(ClienteEntity.class, id);
			assertEquals("Teste Connector", lido.getNome());
			assertEquals(cliente.getCpf(), lido.getCpf());
		} finally {
			if (id != null && em.isOpen()) {
				if (!em.getTransaction().isActive()) {
					em.getTransaction().begin();
				}
				ClienteEntity salvo = em.find(ClienteEntity.class, id);
				if (salvo != null) {
					em.remove(salvo);
				}
				em.getTransaction().commit();
			}
			em.close();
			emf.close();
		}
	}

	private static boolean mysqlAceitaConexao() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection connection = DriverManager.getConnection(JDBC_URL, "root", "")) {
				return connection.isValid(2);
			}
		} catch (Exception ex) {
			return false;
		}
	}

	private static String cpfUnico() {
		String base = Long.toString(System.nanoTime());
		if (base.length() > 11) {
			return base.substring(base.length() - 11);
		}
		StringBuilder cpf = new StringBuilder();
		for (int i = base.length(); i < 11; i++) {
			cpf.append('0');
		}
		cpf.append(base);
		return cpf.toString();
	}

	static boolean versaoPeloMenos(String version, int major, int minor, int patch) {
		String[] parts = version.split("\\.");
		int atualMajor = Integer.parseInt(parts[0]);
		int atualMinor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
		int atualPatch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
		if (atualMajor != major) {
			return atualMajor > major;
		}
		if (atualMinor != minor) {
			return atualMinor > minor;
		}
		return atualPatch >= patch;
	}
}
