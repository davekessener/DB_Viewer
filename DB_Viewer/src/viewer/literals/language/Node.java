package viewer.literals.language;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

class Node implements JsonDeserializer<Node>
{
    private List<Node> next_;
    private String name_;
    private String value_;
    
    public Node()
    {
        next_ = new ArrayList<>();
    }
    
    public String getName() { return name_; }
    public String getValue() { return value_; }
    
    private void setName(String s)
    {
        name_ = s;
    }

    @Override
    public Node deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        Node root = new Node();

        if(json.isJsonPrimitive())
        {
            root.value_ = json.getAsString();
        }
        else
        {
            JsonObject o = (JsonObject) json;
            
            for(Entry<String, JsonElement> e : o.entrySet())
            {
                Node n = context.deserialize(e.getValue(), Node.class);
                n.setName(e.getKey());
                root.next_.add(n);
            }
        }
        
        return root;
    }
    
    public Map<String, String> flatten()
    {
        Map<String, String> m = new HashMap<>();
        
        for(Node n : next_)
        {
            Map<String, String> t = n.flatten();
            
            for(Entry<String, String> tt : t.entrySet())
            {
                m.put(name_ == null ? tt.getKey() : name_ + "." + tt.getKey(), tt.getValue());
            }
        }
        
        if(value_ != null)
        {
            m.put(name_, value_);
        }
        
        return m;
    }
    
    public static Node Read(Reader r)
    {
        return GSON.create().fromJson(r, Node.class);
    }

    private static final GsonBuilder GSON;
    
    static
    {
        GSON = new GsonBuilder();
        
        GSON.registerTypeAdapter(Node.class, new Node());
    }
}
