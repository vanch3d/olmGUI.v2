/**
 * @file DlgErrorMsg.java
 */
package config;


/**
 * Defines the various error message generated by the OLM.
 * Each error is associated with a text hopefully informative enough.
 * 
 * @todo There is still a question about what to do when there is an error. The message is
 *       currently displayed in the status bar but that is not totally satisfactory. 
 *       The idea of displaying it in the dialogue pane is not good either.
 *        
 * @author Nicolas Van Labeke
 * @version $Revision: 1.2 $
 */
public class DlgErrorMsg {

    public static final String ERR_BDESC_DOMAIN = "DlgErrorMsg.BDesc.Domain"; //$NON-NLS-1$
    
    public static final String ERR_BDESC_MOTIVAFFECT = "DlgErrorMsg.BDesc.MotAff"; //$NON-NLS-1$
    
    public static final String ERR_BDESC_CAPES = "DlgErrorMsg.BDesc.CAPES";  //$NON-NLS-1$
    
    public static final String ERR_BDESC_MIDLEVEL = "DlgErrorMsg.BDesc.Comp";  //$NON-NLS-1$

    public static final String ERR_BDESC_PROMPT = "DlgErrorMsg.BDesc.Prompt";  //$NON-NLS-1$

    public static final String ERR_CONNECTION = "DlgErrorMsg.Error.Connection";  //$NON-NLS-1$

    public static final String ERR_GETBELIEF = "DlgErrorMsg.Error.GetBelief";  //$NON-NLS-1$
    
    public static final String ERR_MBASE = "DlgErrorMsg.Error.MBase";  //$NON-NLS-1$
}
