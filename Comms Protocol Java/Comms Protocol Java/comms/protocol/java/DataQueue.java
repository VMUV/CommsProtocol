package comms.protocol.java;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

public class DataQueue 
{
	private SimpleQueue<DataPacket> _fifo;
    private int _maxSize;
    
    public DataQueue()
    {
        _maxSize = 64;
        _fifo = new SimpleQueue<DataPacket>(_maxSize);
    }

    public DataQueue(int size)
    {
        _maxSize = size;
        _fifo = new SimpleQueue<DataPacket>(_maxSize);
    }
    
    public int getMaxSize()
    {
    	return _maxSize;
    }
    
    public int getSize()
    {
    	return _fifo.size();
    }
    
    public boolean IsEmpty()
    {
        return (_fifo.size() == 0);
    }
    
    public boolean Add(DataPacket packet)
    {
        boolean rtn = _fifo.size() < _maxSize;
        if (rtn)
            rtn = _fifo.enqueue(packet);

        return rtn;
    }
    
    public DataPacket Get()
    {
        DataPacket rtn = new DataPacket();
        if (_fifo.size() > 0)
        {
            try { rtn = _fifo.dequeue(); }
            catch (NoSuchElementException e) { };
        }

        return rtn;
    }
    
    public int GetStreamable(byte[] stream)
    {
        int index = 0;
        int maxSize = stream.length;
        while ((index < maxSize) && (_fifo.size() > 0))
        {
            DataPacket packet = Get();
            if ((index + packet.getExpectedLen() + DataPacket.NumOverHeadBytes) < maxSize)
                index = packet.SerializeToStream(stream, index);
            else
            {
                Add(packet);
                break;
            }
        }

        return index;
    }
    
    public void ParseStreamable(byte[] stream, int maxSize)
    {
        int index = 0;
        while ((index < maxSize) && (_fifo.size() < _maxSize))
        {
            DataPacket packet = new DataPacket();
            int newIndex = packet.SerializeFromStream(stream, index);

            if (newIndex > index)
                index = newIndex;
            else
                break;

            if (packet.getPacketType().getValue() < ValidPacketTypes.end_valid_packet_types.getValue())
                Add(packet);
        }
    }
}
