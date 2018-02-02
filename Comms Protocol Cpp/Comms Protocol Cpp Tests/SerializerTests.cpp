#include "stdafx.h"
#include "CppUnitTest.h"
#include "../Comms Protocol Cpp/SerializeUtilities.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Comms_Protocol_Cpp;

namespace CommsProtocolCppTests
{
	TEST_CLASS(SerializerTests)
	{
	public:

		TEST_METHOD(ConvertInt16ToBytesTestEndianness)
		{
			short testVal = 0x6985;
			vector<unsigned char> bigEndian =
				SerializeUtilities::ConvertInt16ToByteArray(testVal, Endianness::big_endian);
			vector<unsigned char> littleEndian =
				SerializeUtilities::ConvertInt16ToByteArray(testVal, Endianness::little_endian);
			Assert::IsTrue((testVal >> 8) == bigEndian[0]);
			Assert::IsTrue((testVal & 0xFF) == bigEndian[1]);
			Assert::IsTrue((testVal & 0xFF) == littleEndian[0]);
			Assert::IsTrue((testVal >> 8) == littleEndian[1]);
		}

		TEST_METHOD(BufferInt16IntoByteArrayBufferOverrunTest)
		{
			short testVal = 0x1234;
			vector<unsigned char> testBuff(2);
			int i = 0;

			i = SerializeUtilities::BufferInt16InToByteArray(testVal,
				testBuff, i, Endianness::big_endian);
			i = SerializeUtilities::BufferInt16InToByteArray(testVal,
				testBuff, i, Endianness::big_endian);
			i = SerializeUtilities::BufferInt16InToByteArray(testVal,
				testBuff, i, Endianness::big_endian);
			Assert::IsTrue(i == 2);

			i = 0;
			i = SerializeUtilities::BufferInt16InToByteArray(testVal,
				testBuff, i, Endianness::little_endian);
			i = SerializeUtilities::BufferInt16InToByteArray(testVal,
				testBuff, i, Endianness::little_endian);
			i = SerializeUtilities::BufferInt16InToByteArray(testVal,
				testBuff, i, Endianness::little_endian);
			Assert::IsTrue(i == 2);
		}

		TEST_METHOD(BufferInt16IntoByteArrayTest)
		{
			short testVal = 0x3456;
			vector<unsigned char> testBuff(10);
			for (int i = 0; i < testBuff.size();)
			{
				i = SerializeUtilities::BufferInt16InToByteArray(testVal,
					testBuff, i, Endianness::big_endian);
			}

			for (int i = 0; i < testBuff.size();)
			{
				Assert::IsTrue((unsigned char)((testVal >> 8) & 0xFF) == testBuff[i++]);
				Assert::IsTrue((unsigned char)(testVal & 0xFF) == testBuff[i++]);
			}

			for (int i = 0; i < testBuff.size();)
			{
				i = SerializeUtilities::BufferInt16InToByteArray(testVal,
					testBuff, i, Endianness::little_endian);
			}

			for (int i = 0; i < testBuff.size();)
			{
				Assert::IsTrue((unsigned char)(testVal & 0xFF) == testBuff[i++]);
				Assert::IsTrue((unsigned char)((testVal >> 8) & 0xFF) == testBuff[i++]);
			}
		}

		TEST_METHOD(ConvertByteArrayToInt16IllegalByteLen)
		{
			vector<unsigned char> array(1);
			short testval =
				SerializeUtilities::ConvertByteArrayToInt16(array, Endianness::big_endian);
			Assert::IsTrue(0 == testval);

			testval =
				SerializeUtilities::ConvertByteArrayToInt16(array, Endianness::little_endian);
			Assert::IsTrue(0 == testval);
		}
	};
}