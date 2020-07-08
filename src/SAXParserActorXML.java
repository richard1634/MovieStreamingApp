
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Connection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SAXParserActorXML extends DefaultHandler {

    List<stars> myEmpls;
    private int starID_int = 0;

    private String tempVal;

    //to maintain context
    private stars tempEmp;

    public SAXParserActorXML() {
        myEmpls = new ArrayList<stars>();
    }

    public void runExample() {
        parseDocument();
        printData();
    }

    private void parseDocument() {
        Connection connection = null;
        int starID_int = 0;
        try {
			connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	        String query = "SELECT stars.id from stars ORDER BY stars.id DESC LIMIT 1";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
    			String starID = resultSet.getString("stars.id");
    			starID_int = Integer.parseInt(starID.substring(2)); 
    			starID_int +=1;  //increment star id to get new one
			}}
		catch(Exception e) {}
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("actors63.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {
  //      PrintWriter out = response.getWriter();
        Connection connection = null;
    	try {
			connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

	        Iterator<stars> it = myEmpls.iterator();
	        while (it.hasNext()) {
	        	//This block will insert into the Database
	        	Class.forName("com.mysql.jdbc.Driver").newInstance();	
	        	
		        String update_query = "INSERT IGNORE INTO stars (id,name,birthYear) VALUES (?,?,?) ";
				PreparedStatement update_statement = connection.prepareStatement(update_query);
				
				// the if and else are for handling cases where for the null, you can't insert null into the database so i just made it into a string and inserted that instead
				String id= "";
				String name= "";
				int year = 0;

				if (it.next().getId() ==null ) {
					id = "null";
					update_statement.setString(1, id);
				}
				else {
					id = it.next().getId();
					update_statement.setString(1, id);
				}
				

				name = it.next().getName();
				update_statement.setString(2, name);
				if (it.next().getBirthYear() == 0) {
					year = 0;
					update_statement.setInt(3, year);
				}
				else {
					year = it.next().getBirthYear();
					update_statement.setInt(3, year);
				}
				System.out.println(id);
				System.out.println(name);
				System.out.println(year);


				update_statement.executeUpdate();
				System.out.println("Insertion Successful<br>");
            }
    	}
    	catch (Exception e) {
    		e.printStackTrace();

    	}
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("actor")) {
            //create a new instance of employee
            tempEmp = new stars();
            }
        }
    

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("actor")) {
            //add it to the list
            myEmpls.add(tempEmp);
            tempEmp.setId("nm"+Integer.toString(starID_int));
        }
        else if (qName.equalsIgnoreCase("stagename")) {
        	try {
            	tempEmp.setName(tempVal);}
        	catch(Exception e){
        		tempEmp.setName("null");
        	}
        } 
        //need to catch catch incorrect years. Some years are like 19XX and those I just set to 0
        else if (qName.equalsIgnoreCase("dob")) {
        	try {
            	tempEmp.setBirthYear(Integer.parseInt(tempVal.trim()));}
        	catch(Exception e){
        		tempEmp.setBirthYear(0);
        	}
        }
        starID_int += 1;
    }

    public static void main(String[] args) {
        SAXParserActorXML spe = new SAXParserActorXML();
        spe.runExample();
    }

}
