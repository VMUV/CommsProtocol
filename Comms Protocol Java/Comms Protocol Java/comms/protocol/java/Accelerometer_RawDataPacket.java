package comms.protocol.java;

public class Accelerometer_RawDataPacket extends AndroidDataPacket {

	private static int numberOfValues = 3;

	public Accelerometer_RawDataPacket() {
		super(ValidPacketTypes.accelerometer_raw_data_packet, numberOfValues);
	}

	public Accelerometer_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.accelerometer_raw_data_packet, numberOfValues, payload);
	}

	public Accelerometer_RawDataPacket(DataPacket packet) throws Exception {
		super(ValidPacketTypes.accelerometer_raw_data_packet, numberOfValues, packet);
	}
	
	public XYZSensor GetAccelerations() {
		AndroidSensor sensor = DeSerialize();
		return new XYZSensor(sensor.GetValues());
	}
}
