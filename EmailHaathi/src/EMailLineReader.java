
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;

public class EMailLineReader {
	
	private static final String delimiter = "Message-ID:";
	private static final int DEFAULT_BUFFER_SIZE = 64 * 1024;
	private int bufferSize = DEFAULT_BUFFER_SIZE;
	private InputStream in;
	private byte[] buffer;
	// the number of bytes of real data in the buffer
	private int bufferLength = 0;
	// the current position in the buffer
	private int bufferPosn = 0;
	private String prevRecord;
	private int prevRecordSize;
	private static final byte CR = '\r';
	private static final byte LF = '\n';

	/**
	 * Create a line reader that reads from the given stream using the default
	 * buffer-size (64k).
	 * 
	 * @param in
	 *            The input stream
	 * @throws IOException
	 */
	public EMailLineReader(InputStream in) {
		this(in, DEFAULT_BUFFER_SIZE);
	}

	
	/**
	 * Create a line reader that reads from the given stream using the given
	 * buffer-size.
	 * 
	 * @param in
	 *            The input stream
	 * @param bufferSize
	 *            Size of the read buffer
	 * @throws IOException
	 */
	public EMailLineReader(InputStream in, int bufferSize) {
		this.in = in;
		this.bufferSize = bufferSize;
		this.buffer = new byte[this.bufferSize];
	}

	/**
	 * Create a line reader that reads from the given stream using the
	 * <code>io.file.buffer.size</code> specified in the given
	 * <code>Configuration</code>.
	 * 
	 * @param in
	 *            input stream
	 * @param conf
	 *            configuration
	 * @throws IOException
	 */
	public EMailLineReader(InputStream in, Configuration conf) throws IOException {
		this(in, conf.getInt("io.file.buffer.size", DEFAULT_BUFFER_SIZE));
	}

	/**
	 * Close the underlying stream.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		in.close();
	}

	public int createEmailRecords(Text value, int maxLineLength, int max)
			throws IOException {
		// TODO Auto-generated method stub
		value.clear();
		Text newLine = new Text();
		StringBuffer buffer = new StringBuffer();
		
		int size = 0;
		int totalSize = 0;
		
		boolean empty = true;

		// Message header from the last iteration
		if (prevRecord != null && !prevRecord.equals("")) {
			buffer.append(prevRecord);
			totalSize += prevRecordSize;
		}
		
		//do while loop until the end of file
		do {
			//read new lines from the input stream
			size = readLine(newLine, maxLineLength, max);
			
			
			if(empty && newLine.toString().trim().length() > 0){
				empty = false;
			}

			// If the line contains the delimiter
			if (newLine.find(delimiter) >= 0) {
				// if this is the first message that is being read
				if (prevRecord == null || prevRecord.equals("")) {
					prevRecord = newLine.toString();
					prevRecordSize = size;
				} else {
					String headerLine = newLine.toString();
					//not necessary the header begins from the delimiter
					//the part before the delimiter becomes the tail for the previous message
					
					String tailofPrevious = headerLine.substring(0,
							headerLine.indexOf(delimiter));
					buffer.append(tailofPrevious);
					totalSize += tailofPrevious.length();
					
					//set the new record to the previous record  
					prevRecord = headerLine.substring(
							headerLine.indexOf(delimiter),
							headerLine.length());
					prevRecordSize = prevRecord.length();
					break;
				}
			}
			
			//if does not contain delimeter just keep on appending it to the buffer 
			if (buffer.length() > 0) {
				buffer.append(" ");
			}
			//append it to the buffer and update size
			buffer.append(newLine.toString());
			totalSize += size;
		} while (size > 0);
		
		if(empty){
			value.clear();
			totalSize = totalSize - prevRecordSize;
		}else{
			//value = new Text(sb.toString());
			value.set(buffer.toString());
		}
				//returns the offset of the new record in the file 
				return totalSize;
	}

	/**
	 * Read one line from the InputStream into the given Text. A line can be
	 * terminated by one of the following: '\n' (LF) , '\r' (CR), or '\r\n'
	 * (CR+LF). EOF also terminates an otherwise unterminated line.
	 * 
	 * @param str
	 *            the object to store the given line (without newline)
	 * @param maxLineLength
	 *            the maximum number of bytes to store into str; the rest of the
	 *            line is silently discarded.
	 * @param maxBytesToConsume
	 *            the maximum number of bytes to consume in this call. This is
	 *            only a hint, because if the line cross this threshold, we
	 *            allow it to happen. It can overshoot potentially by as much as
	 *            one buffer length.
	 * 
	 * @return the number of bytes read including the (longest) newline found.
	 * 
	 * @throws IOException
	 *             if the underlying stream throws
	 */
	public int readLine(Text str, int maxLineLength, int maxBytesToConsume)
			throws IOException {
		/*
		 * We're reading data from in, but the head of the stream may be already
		 * buffered in buffer, so we have several cases: 1. No newline
		 * characters are in the buffer, so we need to copy everything and read
		 * another buffer from the stream. 2. An unambiguously terminated line
		 * is in buffer, so we just copy to str. 3. Ambiguously terminated line
		 * is in buffer, i.e. buffer ends in CR. In this case we copy everything
		 * up to CR to str, but we also need to see what follows CR: if it's LF,
		 * then we need consume LF as well, so next call to readLine will read
		 * from after that. We use a flag prevCharCR to signal if previous
		 * character was CR and, if it happens to be at the end of the buffer,
		 * delay consuming it until we have a chance to look at the char that
		 * follows.
		 */
		str.clear();
		int txtLength = 0; // tracks str.getLength(), as an optimization
		int newlineLength = 0; // length of terminating newline
		boolean prevCharCR = false; // true of prev char was CR
		long bytesConsumed = 0;

		do {
			int startPosn = bufferPosn; // starting from where we left off the
										// last time

			// set buffer length
			if (bufferPosn >= bufferLength) {
				startPosn = bufferPosn = 0;
				if (prevCharCR)
					++bytesConsumed; // account for CR from previous read

				// read some bytes
				bufferLength = in.read(buffer);
				if (bufferLength <= 0)
					break; // EOF
			}

			// traverse to find a new line
			for (; bufferPosn < bufferLength; ++bufferPosn) { // search for
																// newline
				if (buffer[bufferPosn] == LF) {
					newlineLength = (prevCharCR) ? 2 : 1;
					++bufferPosn; // at next invocation proceed from following
									// byte
					break;
				}
				if (prevCharCR) { // CR + notLF, we are at notLF
					newlineLength = 1;
					break;
				}
				prevCharCR = (buffer[bufferPosn] == CR);
			}

			// read length
			int readLength = bufferPosn - startPosn;
			if (prevCharCR && newlineLength == 0)
				--readLength; // CR at the end of the buffer
			bytesConsumed += readLength;
			int appendLength = readLength - newlineLength;
			if (appendLength > maxLineLength - txtLength) {
				appendLength = maxLineLength - txtLength;
			}
			if (appendLength > 0) {
				str.append(buffer, startPosn, appendLength);
				txtLength += appendLength;
			}
		} while (newlineLength == 0 && bytesConsumed < maxBytesToConsume);
		// System.out.println("appended buffer: " + str);

		if (bytesConsumed > (long) Integer.MAX_VALUE)
			throw new IOException("Too many bytes before newline: "
					+ bytesConsumed);
		return (int) bytesConsumed;
	}

	/**
	 * Read from the InputStream into the given Text.
	 * 
	 * @param str
	 *            the object to store the given line
	 * @param maxLineLength
	 *            the maximum number of bytes to store into str.
	 * @return the number of bytes read including the newline
	 * @throws IOException
	 *             if the underlying stream throws
	 */
	public int readLine(Text str, int maxLineLength) throws IOException {
		return readLine(str, maxLineLength, Integer.MAX_VALUE);
	}

	/**
	 * Read from the InputStream into the given Text.
	 * 
	 * @param str
	 *            the object to store the given line
	 * @return the number of bytes read including the newline
	 * @throws IOException
	 *             if the underlying stream throws
	 */
	public int readLine(Text str) throws IOException {
		return readLine(str, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

}
