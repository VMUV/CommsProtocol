#include "stdafx.h"
#include "CppUnitTest.h"
#include "SerializeUtilities.h"

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

	};
}