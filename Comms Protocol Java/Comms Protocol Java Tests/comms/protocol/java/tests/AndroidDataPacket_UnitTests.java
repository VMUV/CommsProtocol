package comms.protocol.java.tests;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import comms.protocol.java.AndroidDataPacket;
import comms.protocol.java.AndroidSensor;
import comms.protocol.java.DataPacket;
import comms.protocol.java.ValidPacketTypes;

public class AndroidDataPacket_UnitTests {

	private byte[] dummyBytePayload = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
	private ValidPacketTypes dummyPacketType = ValidPacketTypes.accelerometer_raw_data_packet;
	private DataPacket dummyDataPacket = new DataPacket(dummyPacketType, (short) dummyBytePayload.length,
			dummyBytePayload);
	private float[] dummyFloatVals = {1.0f,2.0f,3.0f};
	private long dummyTimeStamp = 123456;
	private AndroidSensor dummySensor = new AndroidSensor(dummyFloatVals, dummyTimeStamp);
	
	@Test
	public void TestValidConstructor() {
		AndroidDataPacket packet1 = new AndroidDataPacket(ValidPacketTypes.accelerometer_raw_data_packet, 3);
		assertEquals(ValidPacketTypes.accelerometer_raw_data_packet, packet1.getPacketType());
		assertEquals(20, packet1.getExpectedLen());

		try {
			AndroidDataPacket packet2 = new AndroidDataPacket(ValidPacketTypes.linear_acceleration_raw_data_packet, 3,
					dummyBytePayload);
			assertEquals(ValidPacketTypes.linear_acceleration_raw_data_packet, packet2.getPacketType());
			assertEquals(20, packet2.getExpectedLen());
			assertArrayEquals(dummyBytePayload, packet2.getPayload());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		try {
			AndroidDataPacket packet3 = new AndroidDataPacket(dummyPacketType, 3, dummyDataPacket);
			assertEquals(dummyPacketType, packet3.getPacketType());
			assertEquals(20, packet3.getExpectedLen());
			assertArrayEquals(dummyBytePayload, packet3.getPayload());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void TestInvalidConstructor() throws Exception {
		byte[] badBytePayload = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
		ValidPacketTypes badPacketType = ValidPacketTypes.accelerometer_raw_data_packet;
		DataPacket badDataPacket = new DataPacket(badPacketType, (short) badBytePayload.length, badBytePayload);

		thrown.expect(Exception.class);
		AndroidDataPacket packet2 = new AndroidDataPacket(ValidPacketTypes.linear_acceleration_raw_data_packet, 3, badBytePayload);

		thrown.expect(Exception.class);
		AndroidDataPacket packet3 = new AndroidDataPacket(badPacketType, 3, badDataPacket);
	}
	
	@Test
	public void TestSerialize() {
		AndroidDataPacket packet1 = new AndroidDataPacket(dummyPacketType, 3);
		try {
			packet1.Serialize(dummyBytePayload);
			assertEquals(dummyPacketType, packet1.getPacketType());
			assertEquals(dummyBytePayload.length, packet1.getExpectedLen());
			assertArrayEquals(dummyBytePayload, packet1.getPayload());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		AndroidDataPacket packet2 = new AndroidDataPacket(dummyPacketType, 3);
		try {
			packet2.Serialize(dummySensor);
			assertEquals(dummyPacketType, packet2.getPacketType());
			assertEquals(dummyBytePayload.length, packet2.getExpectedLen());
			assertArrayEquals(dummySensor.GetBytes(), packet2.getPayload());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
