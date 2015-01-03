System.out.println("Hello World");

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.Session;
import org.openntf.domino.Database;
import org.openntf.domino.Document;

Session s = Factory.getSession(SessionType.NATIVE);

def db = s.getDatabase("crossworlds/demo.nsf")

System.out.println(db.title);
System.out.println(db.filePath);

def doc = db.createDocument();

doc.replaceItemValue("form","test");

// doc.save(true,false);
