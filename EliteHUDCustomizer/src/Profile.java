
import java.util.Objects;


public class Profile {
    private String title;
    private String xml;
        
    public Profile (String title, String xml) {
        this.title = title;
        this.xml = xml;
    }

    public String getTitle() {
        return title;
    }

    public String getXml() {
        return xml;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.title);
        hash = 79 * hash + Objects.hashCode(this.xml);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Profile other = (Profile) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.xml, other.xml)) {
            return false;
        }
        return true;
    }    
    
}
