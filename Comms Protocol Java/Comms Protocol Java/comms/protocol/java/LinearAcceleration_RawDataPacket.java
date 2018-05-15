package comms.protocol.java;

public class LinearAcceleration_RawDataPacket extends DataPacket {
	private static int numVals = 3;
	private static int totalLen = (AndroidSensor.NUM_BYTES_PER_FLOAT * numVals) + AndroidSensor.NUM_BYTES_PER_LONG;

	public LinearAcceleration_RawDataPacket() {
		super(ValidPacketTypes.linear_acceleration, (short) totalLen, new byte[totalLen]);
	}

	public LinearAcceleration_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.linear_acceleration, (short) totalLen, new byte[totalLen]);
		Serialize(payload);
	}

	public LinearAcceleration_RawDataPacket(DataPacket packet) throws Exception {
		if ((packet.getPacketType() != ValidPacketTypes.linear_acceleration)
				|| (packet.getExpectedLen() != totalLen)) {
			this.setPacketType(ValidPacketTypes.linear_acceleration);
			this.setExpectedLen((short) totalLen);
			this.setPayload(new byte[this.getExpectedLen()]);
		} else {
			this.setPacketType(packet.getPacketType());
			this.setExpectedLen(packet.getExpectedLen());
			Serialize(packet.getPayload());
		}
	}

	public void Serialize(byte[] payload) throws Exception {
		if (payload.length < this.getExpectedLen())
			throw new Exception();

		this.setPayload(payload);
	}

	public void Serialize(AndroidSensor sensor) throws Exception {
		if (sensor.Length() < numVals)
			throw new Exception();

		this.setPayload(sensor.GetBytes());
	}

	public AndroidSensor DeSerialize() {
		AndroidSensor sensor = new AndroidSensor(numVals);
		sensor.SetBytes(this.getPayload());
		return sensor;
	}
}
