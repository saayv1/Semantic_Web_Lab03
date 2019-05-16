package jena.examples.rdf ;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;


/** Tutorial 2 resources as property values
 */
public class Lab3_2 extends Object {
	
	public static class ExecutionTimer {
		  private long start;
		  private long end;

		  public ExecutionTimer() {
		    reset();
		    start = System.currentTimeMillis();
		  }

		  public void end() {
		    end = System.currentTimeMillis();
		  }

		  private long duration(){
		    return (end-start);
		  }
		  
		  public void printDuration()
		  {
			  System.out.println(this.duration()/1000+"." +this.duration()%10 + " seconds");
		  }

		  public void reset() {
		    start = 0;  
		    end   = 0;
		  }
	}
    
      public static void main (String args[]) throws IOException {
  		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        
        Model modelo = ModelFactory.createDefaultModel();
        InputStream is = FileManager.get().open("Monterey.rdf");
        ExecutionTimer t = new ExecutionTimer();
        modelo.read(is,null);
        t.end();
        t.printDuration();
        
        String q = "select ?p ?o  where {\r\n" + 
        		" {\r\n" + 
        		"  <http://urn.monterey.org/incidents#incident1214> ?p ?o .\r\n" + 
        		"  FILTER(!isBlank(?o)).\r\n" + 
        		" }\r\n" + 
        		" UNION\r\n" + 
        		" {\r\n" + 
        		"  {\r\n" + 
        		"   <http://urn.monterey.org/incidents#incident1214> ?p1 ?s .\r\n" + 
        		"   FILTER(isBlank(?s)).\r\n" + 
        		"   ?s ?p ?o.\r\n" + 
        		"   FILTER(!isBlank(?o))\r\n" + 
        		"  }\r\n" + 
        		" UNION\r\n" + 
        		"  {\r\n" + 
        		"   <http://urn.monterey.org/incidents#incident1214> ?p1 ?s1 .\r\n" + 
        		"   FILTER(isBlank(?s1)).  \r\n" + 
        		"   ?s1 ?p2 ?s.\r\n" + 
        		"   FILTER(isBlank(?s)).\r\n" + 
        		"   ?s ?p ?o.\r\n" + 
        		"  }\r\n" + 
        		" }\r\n" + 
        		"} \r\n" + 
        		"\r\n" + 
        		"limit 100";
        
        Query query = QueryFactory.create(q);
        QueryExecution qe = QueryExecutionFactory.create(query, modelo);
        ResultSet results = qe.execSelect();
        FileOutputStream fs = new FileOutputStream("Lab3_2_vns170230.xml");
        ResultSetFormatter.outputAsXML(fs, results);
        qe.close();
        fs.close();
        is.close();

      }
}