import java.io.IOException;

import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.NifSelector;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;

public class NetDevice {

    static PcapNetworkInterface setNetworkDevice() {
        PcapNetworkInterface device = null;
        //Utilizes pcap4j's device selector
        try {
            device = new NifSelector().selectNetworkInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return device;
    }


    static PcapHandle getHandle() {
        //Handles device selection and lack thereof
        PcapNetworkInterface device = setNetworkDevice();
        if(device == null) {
            System.out.println("No Device Selected, exiting.");
            System.exit(1);
        }
        System.out.println(device);

        //Takes snapshot length in bytes, mode and OS timeout (in millis)
        PcapHandle handle;
        try {
            handle = device.openLive(65536, PromiscuousMode.PROMISCUOUS, 50);
        } catch (PcapNativeException e) {
            handle = null;
            e.printStackTrace();
        }
        return handle;

    }
}
