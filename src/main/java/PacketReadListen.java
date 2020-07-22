import org.pcap4j.core.*;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.Packet;

public class PacketReadListen {
    public static void main(String[] args) {

        //Pull Handle for selected device and setup dump
        PcapHandle handle = NetDevice.getHandle();
        PcapDumper dumper = null;
        try {
            dumper = handle.dumpOpen("C:\\Users\\Your Name Here\\Desktop\\packetdump.pcap");
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }

        //Create filter for TCP Port 80
        String filter = "tcp port 80";
        try {
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        } catch (PcapNativeException | NotOpenException e) {

            e.printStackTrace();
        }

        PcapDumper finalDumper = dumper;
        PacketListener listener = new PacketListener() {
            @Override
            public void gotPacket(Packet packet) {
                // Print packet information
                System.out.println(handle.getTimestamp());
                System.out.println(packet);

                // Dump packets to file
                try {
                    finalDumper.dump(packet, handle.getTimestamp());
                } catch (NotOpenException e) {
                    e.printStackTrace();
                }
            }
        };

        // Loop handle, fed to overriding listener's dump with a maximum packet count of 10
        try {
            handle.loop(10, listener);
        } catch (InterruptedException | PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }

        // Print out handle statistics
        PcapStat stats = null;
        try {
            stats = handle.getStats();
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }

        System.out.println("Packets received: " + stats.getNumPacketsReceived());
        System.out.println("Packets dropped: " + stats.getNumPacketsDropped());
        System.out.println("Packets dropped by interface: " + stats.getNumPacketsDroppedByIf());
    }
    }

