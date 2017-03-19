package lpi.server.soap;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "LoginFault", targetNamespace = "http://soap.server.lpi/")
public class LoginFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private LoginException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public LoginFault(String message, LoginException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public LoginFault(String message, LoginException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: lpi.server.soap.LoginException
     */
    public LoginException getFaultInfo() {
        return faultInfo;
    }

}
