package lt.viko.eif.dgenzuras.libraryapp.communication;

import lt.viko.eif.dgenzuras.libraryapp.Main;
import lt.viko.eif.dgenzuras.libraryapp.database.DBloader;
import lt.viko.eif.dgenzuras.libraryapp.model.Account;
import lt.viko.eif.dgenzuras.libraryapp.model.Book;
import lt.viko.eif.dgenzuras.libraryapp.model.Reader;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;


/**
 * PacketSender class communicates between the system and the client.
 *
 * @author dainius.genzuras@stud.viko.lt
 * @since 1.0
 * @see Main
 */


public class PacketSender {

    public void SendPacket() {
        ServerSocket server = null;

        try {
            System.out.println();
            System.out.println("Starting TCP server on port 9094...");

            try {
                server = new ServerSocket(9094);

                System.out.println("Server successfully started, awaiting connection...");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Socket socket = server.accept();

            System.out.println("Client connected, creating data...");

            Reader reader1 = new Reader(0, "Dainius", "Genzuras", 1001);
            Account account1 = new Account(0, "Saiyandeffa", "gibbi352");
            Book book1 = new Book(00140, "Java for dummies", "Jack 'o Niel", 2005, "Learning", 6);
            Book book2 = new Book(05542, "World's birds", "Kendrick Nomad", 2015, "Nature", 3);
            Book book3 = new Book(78009, "Carpentry 101", "Jack Adams", 2003, "Building", 1);
            Book book4 = new Book(56801, "Car engine guy", "Nicolas Cage", 2020, "Engineering", 20);

            reader1.setAccount(account1);
            reader1.getBookList().add(book1);
            reader1.getBookList().add(book2);
            reader1.getBookList().add(book3);
            reader1.getBookList().add(book4);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            StringWriter data = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(Reader.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(reader1, data);

            System.out.println("Data created, validating...");

            if(!validate(data.toString())) {
                System.exit(1);
            }

            System.out.println("Data validated, sending packet...");

            out.println(data.toString());

            System.out.println("Packet sent successfully!");

            socket.close();

            server.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Reader FetchPacket() {
        System.out.println();
        System.out.println("Connecting to TCP socket on port 9094...");

        Socket socket;

        try {
            socket = new Socket("localhost", 9094);
        } catch (Exception e) {
            System.out.println("Exception has occurred whilst attempting to connect! Attaching exception...");
            System.out.println(e.toString());

            return null;
        }

        System.out.println("Socket successfully connected! Listening for data");

        String data = "";

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            int a;

            while((a = in.read()) != -1) data += (char)a;
        } catch(Exception e) {
            System.out.println("Finished reading data. Starting to parse");
        }

        System.out.println("Data received:");
        System.out.println(data);

        Reader reader = null;

        if(!validate(data)) {
            System.exit(1);
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Reader.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            reader = (Reader) unmarshaller.unmarshal(new ByteArrayInputStream(data.getBytes()));
        } catch (Exception e) {
            System.out.println("Exception has occurred whilst reading data! Attaching exception...");
            System.out.println(e.toString());
        }

        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Saving to DB:");
        DBloader.SaveReader(reader);

        return reader;
    }

    private boolean validate(String xmlFile) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(new File(getResource("schema.xsd")));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlFile)));
            return true;
        } catch (SAXException | IOException e) {
            e.printStackTrace();

            System.out.println("XML validation failed... Transfer terminating");
            return false;
        }
    }

    private String getResource(String filename) throws FileNotFoundException {
        URL resource = getClass().getClassLoader().getResource(filename);
        Objects.requireNonNull(resource);

        return resource.getFile();
    }
}
