package home.inventory.beans;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import liquibase.Contexts;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hsqldb.DatabaseManager;

/**
 *
 * @author BRudowski
 */
@ApplicationScoped
public class DatabaseBean {

    private EntityManagerFactory emf;

    public DatabaseBean() {
    }

    @Produces
    @TransactionScoped
    public EntityManager em() {
        if (emf == null) {
            configureDb();
        }
        return emf.createEntityManager();
    }

    @PreDestroy
    public void dispose() {
        emf.close();
        DatabaseManager.closeDatabases(0);
    }
    
    public void close(@Disposes EntityManager em) {
        em.close();
    }

    @PostConstruct
    public void init() {
        configureDb();
    }
    private void configureDb() {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir")).getParent().resolve("inventory").toAbsolutePath();
        String dbUrl = "jdbc:hsqldb:file:" + basePath.resolve("db").toString() + "/inventory;"
                + "hsqldb.cache_rows=10000;hsqldb.nio_data_file=false;hsqldb.default_table_type=cached;hsqldb.tx=mvcc";
        Properties props = new Properties();
        props.setProperty("javax.persistence.jdbc.driver", "org.hsqldb.jdbc.JDBCDriver");
        props.setProperty("javax.persistence.jdbc.url", dbUrl);
        props.setProperty("javax.persistence.jdbc.user", "sa"); //TODO: revisit user/pass requirements
        props.setProperty("javax.persistence.jdbc.password", "");
        emf = Persistence.createEntityManagerFactory("INVENTORYPU", props);
        buildTables(emf);
    }

    private void buildTables(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            getLiquibase(em.unwrap(Connection.class)).update((Contexts)null);
            em.getTransaction().commit();
        } catch (LiquibaseException ex) {
            //TODO: Replace with a real logging statement
            ex.printStackTrace();
        } finally {
            em.close();
        }

    }

    private Liquibase getLiquibase(Connection conn) throws LiquibaseException {
        return new Liquibase("inventory.db.changelog.xml",
                new ClassLoaderResourceAccessor(getClass().getClassLoader()),
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn)));
    }
}
