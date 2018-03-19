package comms.protocol.java;

public enum ValidPacketTypes 
{
	test_packet(0),
	motus_1_raw_data_packet(1),
	end_valid_packet_types(2);
	
	private final int value;
    private ValidPacketTypes(int value) 
    {
        this.value = value;
    }

    public int getValue() 
    {
        return value;
    }
	//will add more packet types when we start using the gyroscope/other hardware on the android device
}
