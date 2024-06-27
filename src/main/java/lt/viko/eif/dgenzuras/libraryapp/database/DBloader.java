package lt.viko.eif.dgenzuras.libraryapp.database;

import lt.viko.eif.dgenzuras.libraryapp.model.Reader;
import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;

/**
 * DBLoader class saves data to the database.
 *
 * @author dainius.genzuras@stud.viko.lt
 * @since 1.0
 */
public class DBloader {
    public static void SaveReader(Reader reader) {
        org.h2.tools.Server server = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            server = Server.createTcpServer("-tcpPort", "9093").start();
            transaction = session.beginTransaction();

            session.save(reader);

            transaction.commit();
        } catch (SQLException e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {

            if (server != null) {
                server.shutdown();
            }
        }
    }
}
