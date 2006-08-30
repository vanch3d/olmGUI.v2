package toulmin;

import java.util.Hashtable;

public class ToulminSubClaim extends ToulminClaim 
{
    String dimension = null;
    String value = null;

    public Object toXMLRPC() 
    {
        Hashtable tab = new Hashtable();
        if(dimension!=null) tab.put("CLAIM_DIMENSION",new String(dimension));
        if(value!=null) tab.put("CLAIM_VALUE",new String(value));
        return tab;
    }

    public static ToulminClaim fromXMLRPC(Object obj,Toulmin toulmin) 
    {
        ToulminSubClaim claim = null;
        if (obj instanceof Hashtable) 
        {
            Hashtable tab = (Hashtable) obj;
            claim = new ToulminSubClaim();
            Object attr = tab.get("CLAIM_DIMENSION");
            if (attr!=null) claim.dimension = (String)attr;
            attr = tab.get("CLAIM_VALUE");
            if (attr!=null) claim.value = (String)attr;
        }
        return claim;
    }


    /**
     * @return Returns the dimension.
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * @param dimension The dimension to set.
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

}
