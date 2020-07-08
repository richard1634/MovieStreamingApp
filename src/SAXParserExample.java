
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

public class SAXParserExample extends DefaultHandler {

    List<movies> myEmpls;

    private String tempVal;

    //to maintain context
    private movies tempEmp;

    public SAXParserExample() {
        myEmpls = new ArrayList<movies>();
    }

    public void runExample() {
        parseDocument();
        printData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

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
        Connection connection = null;
    	try {
				connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

		        Iterator<movies> it = myEmpls.iterator();
		        while (it.hasNext()) {
		        	//This block will insert into the Database
		        	Class.forName("com.mysql.jdbc.Driver").newInstance();
		        	
			        String update_query = "INSERT IGNORE INTO movies (id,title,year,director) VALUES (?,?,?,?) ";
					PreparedStatement update_statement = connection.prepareStatement(update_query);
					
					// the if and else are for handling cases where for the null, you can't insert null into the database so i just made it into a string and inserted that instead
					String id= "";
					String title= "";
					int year = 0;
					String director ="";
	
					if (it.next().getId() ==null ) {
						id = "null";
						update_statement.setString(1, id);
					}
					else {
						id = it.next().getId();
						update_statement.setString(1, id);
					}
					
					if (it.next().getTitle() == null) {
						title = "null";
						update_statement.setString(2, "null");
					}
					else {
						title = it.next().getTitle();
						update_statement.setString(2, title);
					}
					
					year = it.next().getYear();
					update_statement.setInt(3, year);
					
					if (it.next().getDirector() == null) {
						director = "null";
						update_statement.setString(4, director);
					}
					else {
						director = it.next().getDirector();
						update_statement.setString(4, director);
					}
					System.out.println(title);
					System.out.println(id);
					System.out.println(director);
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
        if (qName.equalsIgnoreCase("film")) {
            //create a new instance of employee
            tempEmp = new movies();
        }
    }
    

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("film")) {
            //add it to the list
            myEmpls.add(tempEmp);
        }

        else if (qName.equalsIgnoreCase("t")) {
            tempEmp.setTitle(tempVal);
            
        } 
        else if (qName.equalsIgnoreCase("fid")) {
        	tempEmp.setId(tempVal);
        }
        //need to catch catch incorrect years. Some years are like 19XX and those I just set to 0
        else if (qName.equalsIgnoreCase("year")) {
        	try {
            	tempEmp.setYear(Integer.parseInt(tempVal.trim()));}
        	catch(Exception e){
        		tempEmp.setYear(0);
        	}
        }
        
        else if (qName.equalsIgnoreCase("dirn")) {
        	if (tempVal == null) {
                tempEmp.setDirector("null");
        	}
        	else {
                tempEmp.setDirector(tempVal);
        	}
        }
    }

    public static void main(String[] args) {
        SAXParserExample spe = new SAXParserExample();
        spe.runExample();
    }
}
