package comms.protocol.java;

public class Gyro_RawDataPacket extends AndroidDataPacket {
	private static int numberOfValues = 3;

	public Gyro_RawDataPacket() {
		super(ValidPacketTypes.gyro_raw_data_packet, numberOfValues);
	}

	public Gyro_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.gyro_raw_data_packet, numberOfValues, payload);
	}

	public Gyro_RawDataPacket(DataPacket packet) throws Exception {
		super(ValidPacketTypes.gyro_raw_data_packet, numberOfValues, packet);
	}
	
	public XYZSensor GetAxisRotation() {
		AndroidSensor sensor = DeSerialize();
		return new XYZSensor(sensor.GetValues());
	}
}