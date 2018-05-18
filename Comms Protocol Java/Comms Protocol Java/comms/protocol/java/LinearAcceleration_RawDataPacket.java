package comms.protocol.java;

public class LinearAcceleration_RawDataPacket extends AndroidDataPacket {

	private static int numberOfValues = 3;

	public LinearAcceleration_RawDataPacket() {
		super(ValidPacketTypes.linear_acceleration_raw_data_packet, numberOfValues);
	}

	public LinearAcceleration_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.linear_acceleration_raw_data_packet, numberOfValues, payload);
	}

	public LinearAcceleration_RawDataPacket(DataPacket packet) throws Exception {
		super(ValidPacketTypes.linear_acceleration_raw_data_packet, numberOfValues, packet);
	}
}
