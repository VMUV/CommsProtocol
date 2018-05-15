package comms.protocol.java;

public class Motus_1_RawDataPacket extends DataPacket {
	public Motus_1_RawDataPacket() {
		super(ValidPacketTypes.motus_1_raw_data_packet, (short) 18, new byte[18]);
	}

	public Motus_1_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.motus_1_raw_data_packet, (short) 18, new byte[18]);
		Serialize(payload);
	}

	public Motus_1_RawDataPacket(DataPacket packet) throws Exception {
		if ((packet.getPacketType() != ValidPacketTypes.motus_1_raw_data_packet) || (packet.getExpectedLen() != 18)) {
			this.setPacketType(ValidPacketTypes.motus_1_raw_data_packet);
			this.setExpectedLen((short) 18);
			this.setPayload(new byte[this.getExpectedLen()]);
		} else {
			this.setPacketType(packet.getPacketType());
			this.setExpectedLen(packet.getExpectedLen());
			Serialize(packet.getPayload());
		}
	}

	public void Serialize(byte[] payload) throws Exception {
		if (payload.length != this.getExpectedLen())
			throw new Exception();

		this.setPayload(payload);
	}

	public void Serialize(short[] payload) throws Exception {
		if (payload.length != (this.getExpectedLen() / 2))
			throw new Exception();

		byte[] bytePayload = new byte[payload.length * 2];
		try {
			int indexToInsertElement = 0;
			for (int i = 0; i < payload.length; i++)
				indexToInsertElement = SerializeUtilities.BufferShortInToByteArray(payload[i], bytePayload,
						indexToInsertElement, Endianness.little_endian);

			this.setPayload(bytePayload);
		} catch (Exception e) {
		}
	}

	public short[] DeSerialize() {
		short[] rtn = new short[this.getExpectedLen() / 2];
		int byteIndex = 0;
		byte[] bytePayload = this.getPayload();
		try {
			for (int i = 0; i < rtn.length; i++) {
				byte[] tmp = new byte[] { bytePayload[byteIndex++], bytePayload[byteIndex++] };
				rtn[i] = SerializeUtilities.ConvertByteArrayToShort(tmp, Endianness.little_endian);
			}
		} catch (IndexOutOfBoundsException e1) {
		}
		return rtn;
	}
}
