package comms.protocol.java;

public class Rotation_Vector_RawDataPacket extends AndroidDataPacket {
	private static int numberOfValues = 4;

	public Rotation_Vector_RawDataPacket() {
		super(ValidPacketTypes.rotation_vector_raw_data_packet, numberOfValues);
	}

	public Rotation_Vector_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.rotation_vector_raw_data_packet, numberOfValues, payload);
	}

	public Rotation_Vector_RawDataPacket(DataPacket packet) throws Exception {
		super(ValidPacketTypes.rotation_vector_raw_data_packet, numberOfValues, packet);
	}

	public Quaternion GetQuat() {
		Quaternion rtn = new Quaternion();
		AndroidSensor sensor = DeSerialize();
		float[] vals = sensor.GetValues();
		rtn.x = vals[0];
		rtn.y = vals[1];
		rtn.z = vals[2];
		rtn.w = vals[3];
		return rtn;
	}

}