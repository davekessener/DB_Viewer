package viewer.literals.language;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.util.Pair;

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
    private String tag_;
    
    public Node()
    {
        next_ = new ArrayList<>();
        tag_ = "";
    }
    
    public String getName() { return name_; }
    public String getValue() { return value_; }
    
    private void setName(String s)
    {
        if(s.contains(":"))
        {
            String[] t = s.split(":");
            tag_ = t[0];
            s = t[1];
        }
        
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
    
    private Map<Pair<String, String>, String> doFlatten()
    {
        Map<Pair<String, String>, String> m = new HashMap<>();
        
        for(Node n : next_)
        {
            Map<Pair<String, String>, String> t = n.doFlatten();
            
            for(Entry<Pair<String, String>, String> tt : t.entrySet())
            {
                m.put(new Pair<>(tt.getKey().getKey(), name_ + "." + tt.getKey().getValue()), tt.getValue());
            }
        }
        
        if(value_ != null)
        {
            m.put(new Pair<>(tag_, name_), value_);
        }
        
        return m;
    }
    
    public Map<String, String> flatten()
    {
        Map<String, String> map = new HashMap<>();
        
        for(Node n : next_)
        {
            Map<Pair<String, String>, String> t = n.doFlatten();
            
            for(Entry<Pair<String, String>, String> e : t.entrySet())
            {
                String tag = "";
                
                if(!e.getKey().getKey().isEmpty())
                {
                    tag = TAGS.get(e.getKey().getKey()).Name + ":";
                }
                
                map.put(tag + e.getKey().getValue(), e.getValue());
            }
        }
        
        return map;
    }
    
    public static Node Read(Reader r)
    {
        return GSON.create().fromJson(r, Node.class);
    }
    
    private static enum Tag
    {
        ERROR("E", "error"),
        BUTTON("B", "button"),
        STRING("S", "string"),
        COLOR("C", "color");
        
        public final String ID;
        public final String Name;
        
        private Tag(String id, String name)
        {
            this.ID = id;
            this.Name = name;
        }
    }
    
    private static final GsonBuilder GSON;
    private static final Map<String, Tag> TAGS;
    
    static
    {
        GSON = new GsonBuilder();
        TAGS = new HashMap<String, Tag>();
        
        GSON.registerTypeAdapter(Node.class, new Node());
        
        for(Tag t : Tag.values())
        {
            TAGS.put(t.ID, t);
        }
    }
}
