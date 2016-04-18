package ch.zhaw.robopatrol;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Schoggi on 18.04.2016.
 */
public class XodusDAOTest {
    final private String inputKey = "key someTesttext";
    private String inputValue = "value someTesttext";
    private XodusDAO db;

    @Before
    public void setupDB(){
        db = new XodusDAO("UnitTestDB");
    }

    @Test
    public void putAndGetTest(){
        db.putToDatabase(inputKey, inputValue);
        assertEquals("Database inputValue should be equal to output", inputValue, db.getByKey(inputKey));
    }

    @Test(expected = NullPointerException.class)
    public void getByInvalidKey(){
        db.getByKey("invalidKey");
    }

    @Test(expected = NullPointerException.class)
    public void deleteTest(){
        db.deleteByKey(inputKey);
        db.getByKey(inputKey);
    }
}
