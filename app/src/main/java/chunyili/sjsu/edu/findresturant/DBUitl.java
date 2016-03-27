package chunyili.sjsu.edu.findresturant;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jilongs on 3/26/16.
 */
public class DBUitl {
    private Manager manager;
    private Database database;
    private final String DATABASE_NAME = "fav_db";
    private final static String TAG = "DBUtil";
    private ArrayList<String> jsonObjectList;
    public DBUitl(Context context){
        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            Log.d(DATABASE_NAME, "Manager created");
        } catch (IOException e) {
            Log.e(DATABASE_NAME, "Cannot create manager object");
        }

        if (!Manager.isValidDatabaseName(DATABASE_NAME)) {
            Log.e(DATABASE_NAME, "Bad database name");
        }
        try {
            if(manager.getExistingDatabase(DATABASE_NAME)!=null){
                database = manager.getExistingDatabase(DATABASE_NAME);
                Log.d (DATABASE_NAME, "Database exists");
            }else {
                database = manager.getDatabase(DATABASE_NAME);
                Log.d(DATABASE_NAME, "Database created");
            }

        } catch (CouchbaseLiteException e) {
            Log.e(DATABASE_NAME, "Cannot get database");
        }
    }

    public ArrayList<String> getLikes(){
        if(jsonObjectList != null){
            return jsonObjectList;
        }
        Log.d(TAG, "getting likes from db");
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        jsonObjectList = new ArrayList<>();
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            Document doc = row.getDocument();
            JSONObject json = new JSONObject(doc.getProperties());
            try {
                jsonObjectList.add(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.d(TAG, String.valueOf(jsonObjectList.size()));
        return jsonObjectList;
    }

    public void addLike(String id){
        boolean isInDB = false;
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);

        try{
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                Document doc = row.getDocument();
                JSONObject json = new JSONObject(doc.getProperties());
                try {
                    if(json.getString("id").toString().equals(id)){
                        isInDB = true;
                        break;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Cannot write document to database", e);

                }
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        if(isInDB ==false){
            Log.d(TAG, "adding " + id +" to db");
            Map<String,Object> jsonmap =  new Hashtable<>();
            jsonmap.put("id", id);
            Document document = database.createDocument();
            try {
                document.putProperties(jsonmap);
                Log.d(TAG, "added");
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot write document to DB", e);

            }
        }
    }
}
