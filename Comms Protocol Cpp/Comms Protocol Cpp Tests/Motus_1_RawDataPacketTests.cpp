#include "stdafx.h"
#include "CppUnitTest.h"
#include "../Comms Protocol Cpp/Motus_1_RawDataPacket.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Comms_Protocol_Cpp;

namespace CommsProtocolCppTests
{
	TEST_CLASS(SerializerTests)
	{
	public:

		TEST_METHOD(Motus_1_PacketTestConstructor)
		{
			Motus_1_RawDataPacket packet;
			Assert::IsTrue(ValidPacketTypes::motus_1_raw_data_packet == packet.getType());
			Assert::IsTrue(18 == packet.getExpectedLen());
		}

		TEST_METHOD(Motus_1_PacketTestSerializeInvalidPayload)
		{
			bool threwException = false;
			Motus_1_RawDataPacket packet;
			vector<unsigned char> invalidBytePayload(12);
			try {
				packet.Serialize(invalidBytePayload);
			}
			catch (int) {
				threwException = true;
			}
			Assert::IsTrue(threwException);
		}

		TEST_METHOD(Motus_1_PacketTestSerializeBytes)
		{
			Motus_1_RawDataPacket packet;
			vector<unsigned char> payload(packet.getExpectedLen());
			vector<short> intPayload(packet.getExpectedLen() / 2);

			int j = 0;
			for (unsigned char i = 0; i < packet.getExpectedLen(); i++)
			{
				if ((i % 2) == 0)
					payload[i] = (unsigned char)j++;
				else
					payload[i] = 0;
			}

			for (int i = 0; i < packet.getExpectedLen() / 2; i++)
				intPayload[i] = (short)i;

			packet.Serialize(payload);
			vector<unsigned char> rtnPayload = packet.getPayload();
			vector<short> rtnIntPayload = packet.DeSerialize();

			Assert::IsTrue(packet.getExpectedLen() == rtnPayload.size());
			Assert::IsTrue(packet.getExpectedLen() / 2 == rtnIntPayload.size());

			for (int i = 0; i < rtnPayload.size(); i++)
				Assert::IsTrue(payload[i] == rtnPayload[i]);

			for (int i = 0; i < rtnIntPayload.size(); i++)
				Assert::IsTrue(intPayload[i] == rtnIntPayload[i]);
		}

		TEST_METHOD(TestMotus1PacketToString)
		{
			char tmpstr[100];
			Motus_1_RawDataPacket packet;
			vector<unsigned char> payload(packet.getExpectedLen());

			int j = 0;
			for (unsigned char i = 0; i < packet.getExpectedLen(); i++)
			{
				if ((i % 2) == 0)
					payload[i] = (unsigned char)j++;
				else
					payload[i] = 0;
			}

			packet.Serialize(payload);
			vector<short> intPayload = packet.DeSerialize();
			sprintf_s(tmpstr, "%d", intPayload[0]);
			string known = tmpstr;

			for (int i = 1; i < intPayload.size(); i++)
			{
				known += ",";
				sprintf_s(tmpstr, "%d", intPayload[i]);
				known += tmpstr;
			}

			Assert::IsFalse(known.compare(packet.ToString()));
		}
	};
}