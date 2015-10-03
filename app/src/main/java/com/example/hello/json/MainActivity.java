package com.example.hello.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    private Button Save=null;
    private Button Read=null;
    private File file=null;
    private static final String FILENAME="json.txt";
    private StringBuffer info=null;
    private String bookName[]=new String[]{"android","i love android"};
    private String author[]=new String[]{"@@","@@"};
    private String publisher[] = new String[]{"home","hometown"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Save=(Button)super.findViewById(R.id.SAVE);
        Read=(Button)super.findViewById(R.id.READ);
        Save.setOnClickListener(new OnClickListener(){
            public void onClick(View v)
            {
                PrintStream ps=null;
                JSONArray bookArray=new JSONArray();
                for(int i=0;i<bookName.length;i++){
                    JSONObject object=new JSONObject();
                    try{
                        object.put("bookname",MainActivity.this.bookName[i]);
                        object.put("author",MainActivity.this.author[i]);
                        object.put("publisher",MainActivity.this.publisher[i]);
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                    bookArray.put(object);
                }
                if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                String path=Environment.getExternalStorageDirectory().toString()
                        +File.separator+"hehe"
                       + File.separator
                        +FILENAME;
                file=new File(path);
                if(!file.getParentFile().exists())
                    file.getParentFile().mkdir();
                try{
                    ps=new PrintStream(new FileOutputStream(file));
                    ps.print(bookArray.toString());

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }finally{
                    if(ps!=null)
                        ps.close();
                }
            }
        });
        Read.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                info=new StringBuffer();
                if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                    return;
                }
                String path = Environment.getExternalStorageDirectory().toString()+File.separator+"hehe"+File.separator+FILENAME;
                file = new File(path);
                if(!file.exists()){
                    Toast.makeText(getApplicationContext(),"XML file not exists",Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    InputStream input = new FileInputStream(file);
                    StringBuffer buffer = new StringBuffer();
                    byte[] b = new byte[4096];
                    int n;
                    while ((n = input.read(b)) != -1) {
                        buffer.append(new String(b,0,n+0));
                    }
                    List<Map<String,Object>>books = MainActivity.this.parseJson(buffer.toString());
                    Iterator<Map<String,Object>> iter=books.iterator();
                    while(iter.hasNext()){
                        Map<String,Object> map = iter.next();
                        info.append(map.get("bookname")).append("☆☆☆\n");
                        info.append(map.get("author")).append("☆☆☆\n");
                        info.append(map.get("publisher")).append("☆☆☆\n");
                    }

                    }
                catch(Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),info.toString(),Toast.LENGTH_SHORT).show();
                }

        });
        }
    public List<Map<String,Object>> parseJson(String s)throws Exception{
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        JSONArray array=new JSONArray(s);
        for(int i=0;i<array.length();i++)
        {
            Map<String,Object> map=new HashMap<String,Object>();
            JSONObject object=array.getJSONObject(i);
            map.put("bookname",object.getString("bookname"));
            map.put("author",object.getString("author"));
            map.put("publisher",object.getString("publisher"));
            list.add(map);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
